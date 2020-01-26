package net.theluckycoder.materialchooser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.io.File
import java.util.ArrayList

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class ChooserActivity : AppCompatActivity() {

    private val swipeRefreshLayout by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
    }
    private val itemList = ArrayList<FileItem>()
    private val filesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FilesAdapter(itemList) { item ->
            if (item.isFolder) {
                currentDir = File(item.path)
                updateAdapter()
            } else {
                finishWithResult(item.path)
            }
        }
    }
    private lateinit var params: ChooserParams
    private var currentDir = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        params = intent.getParcelableExtra(Chooser.ARG_CHOOSER_PARAMS)
            ?: throw IllegalArgumentException("No Chooser Parameters found")

        if (params.useNightTheme) {
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
        recyclerView.adapter = filesAdapter

        currentDir = File(params.rootPath)

        if (!params.isFileChooser) {
            val selectFolderBtn: Button = findViewById(R.id.btn_select_folder)

            selectFolderBtn.visibility = View.VISIBLE
            selectFolderBtn.setOnClickListener {
                finishWithResult(currentDir.absolutePath + "/")
            }
        }

        savedInstanceState?.let {
            val file = it.getSerializable("current_dir")
            if (file is File) currentDir = file
        }

        currentDir.mkdirs()

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout.setOnRefreshListener { updateAdapter() }

        checkForStoragePermission()
    }

    override fun onResume() {
        super.onResume()
        updateAdapter()
    }

    override fun onBackPressed() {
        if (currentDir.absolutePath != params.rootPath && currentDir.parentFile != null) {
            currentDir = currentDir.parentFile
            updateAdapter()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("current_dir", currentDir)
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
                currentDir.mkdirs()
                updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        swipeRefreshLayout.isRefreshing = true
        val lastSize = itemList.size
        itemList.clear()
        filesAdapter.notifyItemRangeRemoved(0, lastSize)

        itemList.addAll(getListedFiles())

        if (currentDir.absolutePath != params.rootPath) {
            val parentFolder = currentDir.parent ?: params.rootPath

            itemList.add(0, FileItem(
                name = getString(R.string.chooser_parent_directory),
                path = parentFolder,
                isFolder = true,
                isParent = true)
            )
        }

        filesAdapter.notifyItemRangeInserted(0, itemList.size)
        swipeRefreshLayout.isRefreshing = false
    }

    private fun getListedFiles(): List<FileItem> {
        val listedFilesArray: Array<File>? = currentDir.listFiles()
        title = currentDir.absolutePath.replace(Environment.getExternalStorageDirectory().absolutePath,
            getString(R.string.chooser_device))

        listedFilesArray ?: return emptyList()

        var sequence = listedFilesArray.asSequence()
            .filter { it.canRead() }
            .filter { params.showHiddenFiles || !it.name.startsWith(".") }
            .filter { params.isFileChooser || it.isDirectory }

        if (params.isFileChooser) {
            val extensions = params.fileExtensions
            if (!extensions.isNullOrEmpty())
                sequence = sequence.filter { it.isDirectory || extensions.contains(it.extension) }
        }

        return sequence
            .map { FileItem(it.name, it.absolutePath, it.isDirectory) }
            .toList()
            .sorted()
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
                .setIcon(R.drawable.ic_chooser_folder)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
                }.show()
        }
    }
}
