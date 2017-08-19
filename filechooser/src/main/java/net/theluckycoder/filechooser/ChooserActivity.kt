package net.theluckycoder.filechooser

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import java.io.File
import java.util.*



class ChooserActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var adapter: FileArrayAdapter? = null
    private lateinit var rootDirPath: String
    private lateinit var currentDir: File
    private var showHiddenFiles = false
    private lateinit var fileExtension: String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.list)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val option = adapter!!.getItem(position)
            if (option != null) {
                if (option.isFolder) {
                    currentDir = File(option.path)
                    load(currentDir)
                } else
                    onFileClick(option)
            }
        }

        val intent = intent

        rootDirPath = getExtraString(intent, Chooser.rootDirPath, rootDirPath)
        currentDir = File(getExtraString(intent, Chooser.startDirPath, rootDirPath))
        showHiddenFiles = intent.getBooleanExtra(Chooser.showHiddenFiles, false)
        fileExtension = getExtraString(intent, Chooser.fileExtension, "")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentPath", currentDir.absolutePath)
        outState.putBoolean("showHiddenFiles", showHiddenFiles)
        outState.putString("fileExtension", fileExtension)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val currentPath = savedInstanceState.getString("currentPath")
        showHiddenFiles = savedInstanceState.getBoolean("showHiddenFiles", false)
        val extension = savedInstanceState.getString("fileExtension")
        if (currentPath != null)
            currentDir = File(currentPath)
        if (extension != null)
            fileExtension = extension
        load(currentDir)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getExtraString(intent: Intent, name: String, defaultValue: String): String {
        return intent.getStringExtra(name) ?: defaultValue
    }

    private fun load(file: File) {
        val dirs = file.listFiles()
        title = file.path.replace(rootDirPath, getString(R.string.file_chooser_device))
        val dirsArray = ArrayList<Option>()
        val filesArray = ArrayList<Option>()

        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT <= 25) {
            Collections.sort(dirsArray)
            Collections.sort(filesArray)
        } else {
            dirsArray.sort()
            filesArray.sort()
        }


        dirsArray.addAll(filesArray)
        if (!file.absolutePath.equals(Environment.getExternalStorageDirectory().absolutePath, ignoreCase = true))
            dirsArray.add(0, Option("Parent Directory", file.parent, true))
        adapter = FileArrayAdapter(this, R.layout.file_view, dirsArray)
        listView.adapter = adapter
    }

    private fun onFileClick(option: Option) {
        val data = Intent()
        data.putExtra(Chooser.resultFilePath, option.path)
        setResult(RESULT_OK, data)
        finish()
    }
}
