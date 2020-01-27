package net.theluckycoder.materialchooser

import android.app.Activity
import android.content.Intent
import android.os.Environment

/**
 * File/Folder Chooser Builder
 */
class Chooser @JvmOverloads constructor(
    private val activity: Activity,
    private val requestCode: Int,
    private var rootPath: String = Environment.getExternalStorageDirectory().absolutePath,
    private var startPath: String? = null,
    private var fileExtensions: List<String>? = null,
    private var showHiddenFiles: Boolean = false,
    private var useNightTheme: Boolean = false,
    @ChooserType private var chooserType: Int = FILE_CHOOSER
) {

    @Deprecated("Use the new constructor")
    constructor(
        activity: Activity,
        requestCode: Int,
        rootPath: String,
        startPath: String?,
        fileExtension: String?,
        showHiddenFiles: Boolean,
        useNightTheme: Boolean,
        @ChooserType chooserType: Int
    ) : this(
        activity,
        requestCode,
        rootPath,
        startPath,
        if (fileExtension != null) listOf(fileExtension) else null,
        showHiddenFiles,
        useNightTheme,
        chooserType
    )

    companion object Constants {
        internal const val ARG_CHOOSER_PARAMS = "chooser_params"

        const val RESULT_PATH = "result_path"

        const val FILE_CHOOSER = 0
        const val FOLDER_CHOOSER = 1
    }

    /**
     * Select a file or a folder
     *
     * @param chooserType select between file chooser or directory chooser
     * *    Default: FILE_CHOOSER
     */
    fun setChooserType(@ChooserType chooserType: Int): Chooser {
        this.chooserType = chooserType
        return this
    }

    /**
     * Set the root directory of the picker
     *
     * @param rootPath the user can't go any higher than this
     * *    Default: External Storage
     */
    fun setRootPath(rootPath: String): Chooser {
        this.rootPath = rootPath
        return this
    }

    /**
     * Set the start directory of the picker
     *
     * @param startPath where the user starts
     * *    Default: Root Path
     */
    fun setStartPath(startPath: String?): Chooser {
        this.startPath = startPath
        return this
    }

    /**
     * Filter files trough extensions
     *
     * @param extension file extension in string format
     * *    Example: "txt"
     */
    @Deprecated("Multiple File Extensions are now supported",
        replaceWith = ReplaceWith("setFileExtensions(arrayOf(extension))"))
    fun setFileExtension(extension: String?): Chooser {
        val fileExtension = extension?.removePrefix(".")
        fileExtensions = if (fileExtension != null) listOf(fileExtension) else null
        return this
    }

    /**
     * Only files with these extensions will be displayed
     *
     * * These will be simply ignored if the chooserType is equal to FOLDER_CHOOSER
     *
     * @param extensions a list containing the file extensions
     * *    Example: ["txt", ".zip"]
     * *    Default: null
     */
    fun setFileExtensions(extensions: List<String>?): Chooser {
        fileExtensions = extensions
            ?.filter { it.isNotBlank() }
            ?.map { it.removePrefix(".") }

        return this
    }

    /**
     *
     * @see setFileExtensions
     */
    fun setFileExtensions(vararg extensions: String): Chooser {
        fileExtensions = extensions
            .filter { it.isNotBlank() }
            .map { it.removePrefix(".") }

        return this
    }

    /**
     * Show or hide hidden files
     *
     * @param show show files and folders that begin with '.'
     * *    Default: false
     */
    fun setShowHiddenFiles(show: Boolean): Chooser {
        showHiddenFiles = show
        return this
    }

    /**
     * Set the Theme to Night mode
     *
     * @param useNightTheme enable night theme
     * *    Default: false
     */
    fun setNightTheme(useNightTheme: Boolean): Chooser {
        this.useNightTheme = useNightTheme
        return this
    }

    /**
     *  Start the chooser activity
     */
    fun start() {
        val extension = fileExtensions
        val params = ChooserParams(
            rootPath,
            startPath,
            extension,
            showHiddenFiles,
            useNightTheme,
            chooserType == FILE_CHOOSER
        )

        val intent = Intent(activity, ChooserActivity::class.java)
        intent.putExtra(ARG_CHOOSER_PARAMS, params)

        activity.startActivityForResult(intent, requestCode)
    }
}
