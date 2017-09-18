package net.theluckycoder.filechooser

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import java.io.File
import java.util.*


@RequiresApi
class ChooserActivity : AppCompatActivity() {

    private var showHiddenFiles = false
    private var isFileChooser = true
    private lateinit var rootDirPath: String
    private lateinit var currentDir: File
    private lateinit var fileExtension: String
    private lateinit var listView: ListView
    private lateinit var adapter: FilesAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent

        rootDirPath = getExtraString(intent, Chooser.ROOT_DIR_PATH, Environment.getExternalStorageDirectory().absolutePath)
        currentDir = File(getExtraString(intent, Chooser.START_DIR_PATH, rootDirPath))
        fileExtension = getExtraString(intent, Chooser.FILE_EXTENSION, "")
        showHiddenFiles = intent.getBooleanExtra(Chooser.SHOW_HIDDEN_FILES, false)
        isFileChooser = intent.getBooleanExtra(Chooser.CHOOSER_TYPE, true)

        listView = findViewById(R.id.list)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val option = adapter.getItem(position)
            if (option.isFolder) {
                currentDir = File(option.path)
                load(currentDir)
            } else
                onSelect(option.name)
        }

        if (!isFileChooser) {
            val selectFolder: Button = findViewById(R.id.button_select_folder)
            selectFolder.visibility = View.VISIBLE
            selectFolder.setOnClickListener({ onSelect(currentDir.absolutePath + "/") })
        }

        currentDir.mkdirs()
        load(currentDir)
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

    private fun getExtraString(intent: Intent, name: String, defaultValue: String): String {
        return intent.getStringExtra(name) ?: defaultValue
    }

    private fun load(currentDir: File) {
        val listedAll: Array<File>? = currentDir.listFiles()
        title = currentDir.absolutePath.replace(Environment.getExternalStorageDirectory().absolutePath, getString(R.string.file_chooser_device))
        val dirsList = ArrayList<FileItem>()
        val filesList = ArrayList<FileItem>()

        if (listedAll != null) {
            if (listedAll.isNotEmpty()) {
                listedAll.filter { it.canRead() }
                        .forEach {
                            if (showHiddenFiles) {
                                when {
                                    it.isDirectory -> dirsList.add(FileItem(it.name, it.absolutePath, true))
                                    fileExtension != "" && it.extension == fileExtension -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                    fileExtension == "" -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                }
                            } else {
                                if (!it.startsWith(".")) {
                                    when {
                                        it.isDirectory -> dirsList.add(FileItem(it.name, it.absolutePath, true))
                                        fileExtension != "" && it.extension == fileExtension -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                        fileExtension == "" -> filesList.add(FileItem(it.name, it.absolutePath, false))
                                    }
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

        if (currentDir.absolutePath != Environment.getExternalStorageDirectory().absolutePath)
            dirsList.add(0, FileItem(getString(R.string.parent_directory), currentDir.parent, true))

        adapter = FilesAdapter(this, dirsList)
        listView.adapter = adapter
    }

    private fun onSelect(path: String) {
        val intent = Intent()
        intent.putExtra(Chooser.RESULT_PATH, path)
        setResult(RESULT_OK, intent)
        finish()
    }
}
