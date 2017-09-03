package net.theluckycoder.filechooser

import android.app.Activity
import android.content.Intent


class Chooser(private val activity: Activity, private val requestCode: Int) {

    companion object {
        @JvmField val SELECT_FILE = "selectFile"
        @JvmField val ROOT_DIR_PATH = "rootDirPath"
        @JvmField val START_DIR_PATH = "startDirPath"
        @JvmField val FILE_EXTENSION = "fileExtension"
        @JvmField val SHOW_HIDDEN_FILES = "showHiddenFiles"

        @JvmField val RESULT_PATH = "resultPath"
    }

    private var mSelectFile: Boolean = true
    private var mRootPath: String? = null
    private var mStartPath: String? = null
    private var mFileExtension: String = ""
    private var mShowHiddenFiles = false

    /**
     * Select a file or a folder
     *
     * @param selectFile prompt the user to select a file
     */
    fun setSelectFile(selectFile: Boolean): Chooser {
        mSelectFile = selectFile
        return this
    }

    /**
     * Set the root directory of the picker
     *
     * @param rootPath the user can't go any higher than this
     */
    fun setRootPath(rootPath: String): Chooser {
        mRootPath = rootPath
        return this
    }

    /**
     * Set the start directory of the picker
     *
     * @param startPath where the user starts
     */
    fun setStartPath(startPath: String): Chooser {
        mStartPath = startPath
        return this
    }

    /**
     * Filter files trough extensions
     *
     * @param extension file extension in string format
     * *                  Example : "txt"
     */
    fun setFileExtension(extension: String): Chooser {
        mFileExtension = extension
        return this
    }

    /**
     * Show or hide hidden files
     *
     * @param show show files and folders that begin with '.'
     */
    fun showHiddenFiles(show: Boolean): Chooser {
        mShowHiddenFiles = show
        return this
    }

    /**
     * Start the file chooser activity
     *
     */
    fun start() {
        val intent = Intent(activity, ChooserActivity::class.java)
        if (mRootPath != null)
            intent.putExtra(ROOT_DIR_PATH, mRootPath)
        if (mStartPath != null)
            intent.putExtra(START_DIR_PATH, mStartPath)
        intent.putExtra(FILE_EXTENSION, mFileExtension)
        intent.putExtra(SHOW_HIDDEN_FILES, mShowHiddenFiles)
        intent.putExtra(SELECT_FILE, mSelectFile)

        activity.startActivityForResult(intent, requestCode)
    }
}
