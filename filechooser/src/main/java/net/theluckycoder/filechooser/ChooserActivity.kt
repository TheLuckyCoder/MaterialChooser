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
    private var selectFile = true
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
        selectFile = intent.getBooleanExtra(Chooser.SELECT_FILE, true)

        currentDir.mkdirs()
        load(currentDir)

        listView = findViewById(R.id.list)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val option = adapter.getItem(position)
            if (option.isFolder) {
                currentDir = File(option.path)
                load(currentDir)
            } else
                onSelect(option.name)
        }

        if (!selectFile) {
            val selectFolder: Button = findViewById(R.id.button_select_folder)
            selectFolder.visibility = View.VISIBLE
            selectFolder.setOnClickListener({ onSelect(currentDir.absolutePath + "/") })
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPath", currentDir.absolutePath)
        outState.putString(Chooser.FILE_EXTENSION, fileExtension)
        outState.putBoolean(Chooser.SHOW_HIDDEN_FILES, showHiddenFiles)
        outState.putBoolean(Chooser.SELECT_FILE, selectFile)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val currentPath = savedInstanceState.getString("currentPath")
        val extension = savedInstanceState.getString(Chooser.FILE_EXTENSION)
        showHiddenFiles = savedInstanceState.getBoolean(Chooser.SHOW_HIDDEN_FILES, false)
        selectFile = savedInstanceState.getBoolean(Chooser.SELECT_FILE, true)

        currentDir = File(currentPath)
        fileExtension = extension

        load(currentDir)
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
        val dirsArray = ArrayList<Option>()
        val filesArray = ArrayList<Option>()

        for (ff in dirs) {
            val fName = ff.name
            if (ff.canRead()) {
                if (showHiddenFiles) {
                    when {
                        ff.isDirectory -> dirsArray.add(Option(fName, ff.absolutePath, true))
                        fileExtension != "" && ff.name.substring(fName.lastIndexOf(".") + 1, fName.length) == fileExtension -> filesArray.add(Option(fName, ff.absolutePath, false))
                        fileExtension == "" -> filesArray.add(Option(fName, ff.absolutePath, false))
                    }
                } else {
                    if (ff.name.substring(0, 1) != ".") {
                        when {
                            ff.isDirectory -> dirsArray.add(Option(fName, ff.absolutePath, true))
                            fileExtension != "" && ff.name.substring(fName.lastIndexOf(".") + 1, fName.length) == fileExtension -> filesArray.add(Option(fName, ff.absolutePath, false))
                            fileExtension == "" -> filesArray.add(Option(fName, ff.absolutePath, false))
                        }
                    }
                }
            }
        }

        Collections.sort(dirsArray)

        if (selectFile) {
            Collections.sort(filesArray)
            dirsArray.addAll(filesArray)
        }

        if (file.absolutePath != Environment.getExternalStorageDirectory().absolutePath)
            dirsArray.add(0, Option("Parent Directory", file.parent, true))
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
