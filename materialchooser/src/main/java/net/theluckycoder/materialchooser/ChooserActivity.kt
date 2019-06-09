package net.theluckycoder.materialchooser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RestrictTo
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.util.ArrayList

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class ChooserActivity : AppCompatActivity() {

    private val mSwipeRefreshLayout by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
    }
    private val mItemList = ArrayList<FileItem>()
    private val mAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FilesAdapter(mItemList) { item ->
            if (item.isFolder) {
                mCurrentDir = File(item.path)
                updateAdapter()
            } else {
                finishWithResult(item.path)
            }
        }
    }
    private var mShowHiddenFiles = false
    private var mIsFileChooser = true
    private var mRootDirPath = Environment.getExternalStorageDirectory().absolutePath
    private var mCurrentDir = File(mRootDirPath)
    private var mFileExtension = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val extra = intent.extras ?: run {
            finish()
            return
        }
        if (extra.getBoolean(Chooser.USE_NIGHT_THEME, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mAdapter

        mRootDirPath = extra.getString(Chooser.ROOT_DIR_PATH, mRootDirPath)
        val startPath = extra.getString(Chooser.START_DIR_PATH, mRootDirPath)
        if (startPath != mRootDirPath) mCurrentDir = File(startPath)
        mFileExtension = extra.getString(Chooser.FILE_EXTENSION, "")
        mShowHiddenFiles = extra.getBoolean(Chooser.SHOW_HIDDEN_FILES, false)
        mIsFileChooser = extra.getInt(Chooser.CHOOSER_TYPE) == 0

        if (!mIsFileChooser) {
            val selectFolderBtn: Button = findViewById(R.id.btn_select_folder)

            selectFolderBtn.visibility = View.VISIBLE
            selectFolderBtn.setOnClickListener {
                finishWithResult(mCurrentDir.absolutePath + "/")
            }
        }

        savedInstanceState?.let {
            val file = it.getSerializable("current_dir")
            if (file is File) mCurrentDir = file
        }

        mCurrentDir.mkdirs()

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        mSwipeRefreshLayout.setOnRefreshListener { updateAdapter() }

        checkForStoragePermission()
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
    }

    override fun onBackPressed() {
        if (mCurrentDir.absolutePath != mRootDirPath && mCurrentDir.parentFile != null) {
            mCurrentDir = mCurrentDir.parentFile
            updateAdapter()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("current_dir", mCurrentDir)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_chooser, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_chooser_refresh -> {
                updateAdapter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.size < 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e("Storage Permission", "Permission not granted")
                finish()
            } else {
                mCurrentDir.mkdirs()
                updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        mSwipeRefreshLayout.isRefreshing = true
        val lastSize = mItemList.size
        mItemList.clear()
        mAdapter.notifyItemRangeRemoved(0, lastSize)

        mItemList.addAll(getListedFiles())

        if (mCurrentDir.absolutePath != mRootDirPath) {
            val parentFolder = mCurrentDir.parent ?: mRootDirPath

            mItemList.add(0, FileItem(
                name = getString(R.string.chooser_parent_directory),
                path = parentFolder,
                isFolder = true,
                isParent = true)
            )
        }

        mAdapter.notifyItemRangeInserted(0, mItemList.size)
        mSwipeRefreshLayout.isRefreshing = false
    }

    private fun getListedFiles(): List<FileItem> {
        val listedFilesArray: Array<File>? = mCurrentDir.listFiles()
        title = mCurrentDir.absolutePath.replace(Environment.getExternalStorageDirectory().absolutePath,
            getString(R.string.chooser_device))

        listedFilesArray ?: return emptyList()

        val itemList = ArrayList<FileItem>(listedFilesArray.size)

        listedFilesArray.asSequence()
            .filter { it.canRead() }
            .filter { mShowHiddenFiles || !it.name.startsWith(".") }
            .forEach {
                if (it.isDirectory) {
                    itemList.add(FileItem(it.name, it.absolutePath, true))
                } else if (mIsFileChooser && (mFileExtension.isEmpty() || it.extension == mFileExtension)) {
                    itemList.add(FileItem(it.name, it.absolutePath, false))
                }
            }

        itemList.sort()

        return itemList
    }

    private fun finishWithResult(path: String) {
        Intent().apply {
            putExtra(Chooser.RESULT_PATH, path)
            setResult(RESULT_OK, this)
        }
        finish()
    }

    private fun checkForStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) return

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, R.string.chooser_permission_required_desc, Toast.LENGTH_LONG).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.chooser_permission_required)
                .setMessage(R.string.chooser_permission_required_desc)
                .setCancelable(false)
                .setIcon(R.drawable.ic_folder)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
                }.show()
        }
    }
}
