package net.theluckycoder.filechooserexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import net.theluckycoder.materialchooser.Chooser

class MainActivity : AppCompatActivity() {

    private companion object {
        private const val REQUEST_CODE = 10
    }

    private lateinit var tvResultPath: TextView
    private lateinit var swShowHiddenFiles: Switch
    private lateinit var swUseNightTheme: Switch
    private lateinit var etFileExtension: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResultPath = findViewById(R.id.tv_result_path)
        swShowHiddenFiles = findViewById(R.id.sw_hidden_files)
        swUseNightTheme = findViewById(R.id.sw_night_theme)
        etFileExtension = findViewById(R.id.et_file_extension)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val path = data.getStringExtra(Chooser.RESULT_PATH)

            tvResultPath.text = path
        }
    }

    fun startFileChooser(@Suppress("UNUSED_PARAMETER") view: View) {
        Chooser(this,
            requestCode = REQUEST_CODE,
            showHiddenFiles = swShowHiddenFiles.isChecked,
            fileExtension = etFileExtension.text?.toString().orEmpty(),
            useNightTheme = swUseNightTheme.isChecked)
            .start()
    }

    fun startFolderChooser(@Suppress("UNUSED_PARAMETER") view: View) {
        Chooser(this, REQUEST_CODE)
            .setShowHiddenFiles(swShowHiddenFiles.isChecked)
            .setChooserType(Chooser.FOLDER_CHOOSER)
            .setNightTheme(swUseNightTheme.isChecked)
            .start()
    }
}
