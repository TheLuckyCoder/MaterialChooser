package net.theluckycoder.filechooserexample

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Switch
import android.widget.TextView
import net.theluckycoder.materialchooser.Chooser

class MainActivity : AppCompatActivity() {

    private companion object {
        private const val REQUEST_CODE = 10
    }

    private lateinit var filePathTxt: TextView
    private lateinit var showHiddenFiles: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filePathTxt = findViewById(R.id.tv_file_path)
        showHiddenFiles = findViewById(R.id.sw_hidden_files)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val filePath = data.getStringExtra(Chooser.RESULT_PATH)

            filePathTxt.text = filePath
        }
    }

    fun startFileChooser(@Suppress("UNUSED_PARAMETER") view: View) {
        Chooser(this,
            requestCode = REQUEST_CODE,
            showHiddenFiles = showHiddenFiles.isChecked,
            fileExtension = "txt")
            .start()
    }
}
