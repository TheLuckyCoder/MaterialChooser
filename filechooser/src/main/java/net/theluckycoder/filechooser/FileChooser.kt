package net.theluckycoder.filechooser

import android.app.Activity
import android.content.Intent


class FileChooser(private val activity: Activity, private val requestCode: Int) {

    companion object {
        @JvmField val ROOT_DIR_PATH = "rootDirPath"
        @JvmField val START_DIR_PATH = "startDirPath"
        @JvmField val FILE_EXTENSION = "fileExtension"
        @JvmField val SHOW_HIDDEN_FILES = "showHiddenFiles"

        @JvmField val RESULT_FILE_PATH = "resultFilePath"
    }

    private var mRootPath: String? = null
    private var mStartPath: String? = null
    private var mFileExtension: String? = null
    private var mShowHiddenFiles = false

    /**
     * Set the root directory of the picker
     *
     * @param rootPath the user can't go any higher than this
     */
    fun setRootPath(rootPath: String): FileChooser {
        mRootPath = rootPath
        return this
    }

    /**
     * Set the start directory of the picker
     *
     * @param startPath where the user starts
     */
    fun setStartPath(startPath: String): FileChooser {
        mStartPath = startPath
        return this
    }

    /**
     * Filter files trough extensions
     *
     * @param extension file extension in string format
     * *                  Example : "txt"
     */
    fun setFileExtension(extension: String): FileChooser {
        mFileExtension = extension
        return this
    }

    /**
     * Show or hide hidden files
     *
     * @param show show files and folders that begin with '.'
     */
    fun showHiddenFiles(show: Boolean): FileChooser {
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
        if (mFileExtension != null)
            intent.putExtra(FILE_EXTENSION, mFileExtension)
        intent.putExtra(SHOW_HIDDEN_FILES, mShowHiddenFiles)

        activity.startActivityForResult(intent, requestCode)
    }
}
