package net.theluckycoder.materialchooser

import android.app.Activity
import android.content.Intent
import android.os.Environment


class Chooser(private val activity: Activity,
              private val requestCode: Int,
              private var rootPath: String = Environment.getExternalStorageDirectory().absolutePath,
              private var startPath: String = rootPath,
              private var fileExtension: String = "",
              private var showHiddenFiles: Boolean = true,
              @ChooserType private var chooserType: Int = FILE_CHOOSER) {

    companion object Constants {
        const val CHOOSER_TYPE = "chooserType"
        const val ROOT_DIR_PATH = "rootDirPath"
        const val START_DIR_PATH = "startDirPath"
        const val FILE_EXTENSION = "fileExtension"
        const val SHOW_HIDDEN_FILES = "setShowHiddenFiles"

        const val RESULT_PATH = "resultPath"

        const val FILE_CHOOSER = 0
        const val FOLDER_CHOOSER = 1
    }

    /**
     * Select a file or a folder
     *
     * @param chooserType select between file chooser or directory chooser
     * *                     Default: FILE_CHOOSER
     */
    fun setChooserType(@ChooserType chooserType: Int): Chooser {
        this.chooserType = chooserType
        return this
    }

    /**
     * Set the root directory of the picker
     *
     * @param rootPath the user can't go any higher than this
     * *                    Default: External Storage
     */
    fun setRootPath(rootPath: String): Chooser {
        this.rootPath = rootPath
        return this
    }

    /**
     * Set the start directory of the picker
     *
     * @param startPath where the user starts
     * *                  Default: Root Path
     */
    fun setStartPath(startPath: String): Chooser {
        this.startPath = startPath
        return this
    }

    /**
     * Filter files trough extensions
     *
     * @param extension file extension in string format
     * *                  Example: "txt"
     */
    fun setFileExtension(extension: String): Chooser {
        this.fileExtension = extension
        return this
    }

    /**
     * Show or hide hidden files
     *
     * @param show show files and folders that begin with '.'
     *                     Default: false
     */
    fun setShowHiddenFiles(show: Boolean): Chooser {
        showHiddenFiles = show
        return this
    }

    /**
     * Start the chooser activity
     *
     */
    fun start() {
        val intent = Intent(activity, ChooserActivity::class.java)

        intent.putExtra(ROOT_DIR_PATH, rootPath)
        intent.putExtra(START_DIR_PATH, startPath)
        intent.putExtra(FILE_EXTENSION, fileExtension)
        intent.putExtra(SHOW_HIDDEN_FILES, showHiddenFiles)
        intent.putExtra(CHOOSER_TYPE, chooserType)

        activity.startActivityForResult(intent, requestCode)
    }
}
