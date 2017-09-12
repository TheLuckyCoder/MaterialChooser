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

    private fun load(file: File) {
        val dirs = file.listFiles()
        title = file.absolutePath.replace(Environment.getExternalStorageDirectory().absolutePath, getString(R.string.file_chooser_device))
        val dirsArray = ArrayList<FileItem>()
        val filesArray = ArrayList<FileItem>()

        if (dirs.isNotEmpty()) {
            for (ff in dirs) {
                val fName = ff.name
                if (ff.canRead()) {
                    if (showHiddenFiles) {
                        when {
                            ff.isDirectory -> dirsArray.add(FileItem(fName, ff.absolutePath, true))
                            fileExtension != "" && ff.name.substring(fName.lastIndexOf(".") + 1, fName.length) == fileExtension -> filesArray.add(FileItem(fName, ff.absolutePath, false))
                            fileExtension == "" -> filesArray.add(FileItem(fName, ff.absolutePath, false))
                        }
                    } else {
                        if (ff.name.substring(0, 1) != ".") {
                            when {
                                ff.isDirectory -> dirsArray.add(FileItem(fName, ff.absolutePath, true))
                                fileExtension != "" && ff.name.substring(fName.lastIndexOf(".") + 1, fName.length) == fileExtension -> filesArray.add(FileItem(fName, ff.absolutePath, false))
                                fileExtension == "" -> filesArray.add(FileItem(fName, ff.absolutePath, false))
                            }
                        }
                    }
                }
            }
        }

        dirsArray.sortedBy { it.name }

        if (isFileChooser) {
            filesArray.sortedBy { it.name }
            dirsArray.addAll(filesArray)
        }

        if (file.absolutePath != Environment.getExternalStorageDirectory().absolutePath)
            dirsArray.add(0, FileItem(getString(R.string.parent_directory), file.parent, true))
        adapter = FilesAdapter(this, R.layout.item_file, dirsArray)
        listView.adapter = adapter
    }

    private fun onSelect(path: String) {
        val intent = Intent()
        intent.putExtra(Chooser.RESULT_PATH, path)
        setResult(RESULT_OK, intent)
        finish()
    }
}
