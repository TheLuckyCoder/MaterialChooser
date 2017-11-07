package net.theluckycoder.materialchooser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import java.io.File
import java.util.*


@RequiresApi
class ChooserActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var showHiddenFiles = false
    private var isFileChooser = true
    private var rootDirPath = Environment.getExternalStorageDirectory().absolutePath
    private lateinit var currentDir: File
    private var fileExtension = ""

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rootDirPath = intent.getStringExtra(Chooser.ROOT_DIR_PATH) ?: rootDirPath
        currentDir = File(intent.getStringExtra(Chooser.START_DIR_PATH) ?: rootDirPath)
        fileExtension = intent.getStringExtra(Chooser.FILE_EXTENSION) ?: ""
        showHiddenFiles = intent.getBooleanExtra(Chooser.SHOW_HIDDEN_FILES, false)
        isFileChooser = intent.getIntExtra(Chooser.CHOOSER_TYPE, 0) == 0

        listView = findViewById(R.id.list)
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val option = parent.getItemAtPosition(position) as FileItem
            if (option.isFolder) {
                currentDir = File(option.path)
                load(currentDir)
            } else {
                onSelect(option.path)
            }
        }

        if (!isFileChooser) {
            val selectFolder: Button = findViewById(R.id.button_select_folder)
            selectFolder.visibility = View.VISIBLE
            selectFolder.setOnClickListener({ onSelect(currentDir.absolutePath + "/") })
        }

        currentDir.mkdirs()
        load(currentDir)

        if (checkPermission())
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
    }

    override fun onBackPressed() {
        if (currentDir.absolutePath != rootDirPath) {
            currentDir = currentDir.parentFile
            load(currentDir)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 100) {
            if (grantResults.size < 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e("Storage Permission", "Not Granted")
                finish()
            } else {
                load(currentDir)
            }
        }
    }

    private fun load(dir: File) {
        val listedFilesArray = dir.listFiles()
        title = dir.absolutePath.replace(Environment.getExternalStorageDirectory().absolutePath, getString(R.string.file_chooser_device))
        val dirsList = ArrayList<FileItem>()
        val filesList = ArrayList<FileItem>()

        if (listedFilesArray != null && listedFilesArray.isNotEmpty()) {
            listedFilesArray.filter { it.canRead() }
                    .forEach {
                        if (showHiddenFiles) {
                            when {
                                it.isDirectory -> dirsList.add(FileItem(it.name, it.absolutePath, true))
                                fileExtension != "" && it.extension == fileExtension -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                fileExtension == "" -> filesList.add(FileItem(it.name, it.absolutePath, false))
                            }
                        } else {
                            if (!it.name.startsWith(".")) {
                                when {
                                    it.isDirectory -> dirsList.add(FileItem(it.name, it.absolutePath, true))
                                    fileExtension != "" && it.extension == fileExtension -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                    fileExtension == "" -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                }
                            }
                        }
                    }
        }

        dirsList.sortedBy { it.name }

        if (isFileChooser) {
            filesList.sortedBy { it.name }
            dirsList.addAll(filesList)
        }

        if (dir.absolutePath != Environment.getExternalStorageDirectory().absolutePath)
            dirsList.add(0, FileItem(getString(R.string.parent_directory), dir.parent, true))

        listView.adapter = FilesAdapter(this, R.layout.item_file, dirsList)
    }

    private fun onSelect(path: String) {
        val intent = Intent()
        intent.putExtra(Chooser.RESULT_PATH, path)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result != PackageManager.PERMISSION_GRANTED
    }
}
