package net.theluckycoder.filechooser

import android.app.Activity
import android.content.Intent
import android.support.annotation.RequiresPermission


class Chooser(private val activity: Activity, private val requestCode: Int) {

    companion object {
        @JvmField val CHOOSER_TYPE = "chooserType"
        @JvmField val ROOT_DIR_PATH = "rootDirPath"
        @JvmField val START_DIR_PATH = "startDirPath"
        @JvmField val FILE_EXTENSION = "fileExtension"
        @JvmField val SHOW_HIDDEN_FILES = "showHiddenFiles"

        @JvmField val RESULT_PATH = "resultPath"
    }

    enum class ChooserType {
        FILE_CHOOSER, FOLDER_CHOOSER
    }

    private var mChooserType: ChooserType = ChooserType.FILE_CHOOSER
    private var mRootPath: String? = null
    private var mStartPath: String? = null
    private var mFileExtension: String = ""
    private var mShowHiddenFiles = false

    /**
     * Select a file or a folder
     *
     * @param chooserType select between file chooser or directory chooser
     * *                     Default: FILE_CHOOSER
     */
    fun setChooserType(chooserType: ChooserType): Chooser {
        mChooserType = chooserType
        return this
    }

    /**
     * Set the root directory of the picker
     *
     * @param rootPath the user can't go any higher than this
     * *                    Default: External Storage
     */
    fun setRootPath(rootPath: String): Chooser {
        mRootPath = rootPath
        return this
    }

    /**
     * Set the start directory of the picker
     *
     * @param startPath where the user starts
     * *                  Default: Root Path
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
    @RequiresPermission(value = "android.permission.READ_EXTERNAL_STORAGE")
    fun start() {
        val intent = Intent(activity, ChooserActivity::class.java)
        if (mRootPath != null)
            intent.putExtra(ROOT_DIR_PATH, mRootPath)
        if (mStartPath != null)
            intent.putExtra(START_DIR_PATH, mStartPath)
        intent.putExtra(FILE_EXTENSION, mFileExtension)
        intent.putExtra(SHOW_HIDDEN_FILES, mShowHiddenFiles)
        intent.putExtra(CHOOSER_TYPE, mChooserType)

        activity.startActivityForResult(intent, requestCode)
    }
}
