package net.theluckycoder.materialchooser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RestrictTo
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.util.ArrayList
import java.util.Collections

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ChooserActivity : AppCompatActivity() {

    private val mItemList = ArrayList<FileItem>()
    private lateinit var mAdapter: FilesAdapter
    private var mShowHiddenFiles = false
    private var mIsFileChooser = true
    private var mRootDirPath = Environment.getExternalStorageDirectory().absolutePath
    private var mCurrentDir = File(mRootDirPath)
    private var mFileExtension = ""

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAdapter = FilesAdapter(mItemList, { item ->
            if (item.isFolder) {
                mCurrentDir = File(item.path)
                updateAdapter()
            } else {
                finishWithResult(item.path)
            }
        })
        mAdapter.setHasStableIds(true)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = mAdapter

        mRootDirPath = intent.getStringExtra(Chooser.ROOT_DIR_PATH) ?: mRootDirPath
        val startPath = intent.getStringExtra(Chooser.START_DIR_PATH) ?: mRootDirPath
        if (startPath != mRootDirPath) mCurrentDir = File(startPath)
        mFileExtension = intent.getStringExtra(Chooser.FILE_EXTENSION) ?: ""
        mShowHiddenFiles = intent.getBooleanExtra(Chooser.SHOW_HIDDEN_FILES, false)
        mIsFileChooser = intent.getIntExtra(Chooser.CHOOSER_TYPE, 0) == 0

        if (!mIsFileChooser) {
            val selectFolderBtn: Button = findViewById(R.id.button_select_folder)

            selectFolderBtn.visibility = View.VISIBLE
            selectFolderBtn.setOnClickListener {
                finishWithResult(mCurrentDir.absolutePath + "/")
            }
        }

        mCurrentDir.mkdirs()
        updateAdapter()

        checkForStoragePermission()
    }

    override fun onBackPressed() {
        if (mCurrentDir.absolutePath != mRootDirPath) {
            mCurrentDir = mCurrentDir.parentFile
            updateAdapter()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == 100) {
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
        val list = getListedFiles()

        if (mCurrentDir.absolutePath != mRootDirPath) {
            val parentFolder = mCurrentDir.parent ?: mRootDirPath
            list.add(0, FileItem(getString(R.string.file_chooser_parent_directory),
                parentFolder, true, true))
        }

        mItemList.clear()
        mItemList.addAll(list)
        mAdapter.notifyDataSetChanged()
    }

    private fun getListedFiles(): ArrayList<FileItem> {
        val listedFilesArray = mCurrentDir.listFiles()
        title = mCurrentDir.absolutePath.replace(Environment.getExternalStorageDirectory().absolutePath,
            getString(R.string.file_chooser_device))
        val dirsList = ArrayList<FileItem>()
        val filesList = ArrayList<FileItem>()

        if (listedFilesArray != null && listedFilesArray.isNotEmpty()) {
            listedFilesArray
                .filter { it.canRead() }
                .forEach {
                    if (!mShowHiddenFiles && it.name.startsWith(".")) return@forEach
                    when {
                        it.isDirectory -> {
                            dirsList.add(FileItem(it.name, it.absolutePath, true))
                        }
                        mFileExtension.isEmpty() -> {
                            filesList.add(FileItem(it.name, it.absolutePath, false))
                        }
                        mFileExtension.isNotEmpty() && it.extension == mFileExtension -> {
                            filesList.add(FileItem(it.name, it.absolutePath, false))
                        }
                    }
                }
        }

        Collections.sort(dirsList)

        if (mIsFileChooser) {
            Collections.sort(filesList)
            dirsList.addAll(filesList)
        }

        return dirsList
    }

    private fun finishWithResult(path: String) {
        Intent().apply {
            putExtra(Chooser.RESULT_PATH, path)
        }.run {
            setResult(RESULT_OK, this)
        }
        finish()
    }

    private fun checkForStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) return

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, R.string.file_chooser_permission_required_desc, Toast.LENGTH_LONG).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.file_chooser_permission_required)
                .setMessage(R.string.file_chooser_permission_required_desc)
                .setCancelable(false)
                .setIcon(R.drawable.ic_folder)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
                }.show()
        }
    }
}
