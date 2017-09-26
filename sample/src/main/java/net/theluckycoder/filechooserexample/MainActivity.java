package net.theluckycoder.filechooserexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.theluckycoder.materialchooser.Chooser;


public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 10;
    private final int PERMISSION_REQUEST_CODE = 100;
    private TextView filePathTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePathTxt = findViewById(R.id.filePath);

        // Check for Storage Permission in Android 6 and above
        if (checkPermission())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    public void startFileChooser(View view) {
        new Chooser(this, REQUEST_CODE)
                .setRootPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                .setStartPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/")
                .showHiddenFiles(true)
                .setFileExtension("txt")
                .start();

        // Second Method
        /*
        Intent intent = new Intent(this, ChooserActivity.class);
        intent.putExtra(Chooser.CHOOSER_TYPE, Chooser.FOLDER_CHOOSER);
        intent.putExtra(Chooser.ROOT_DIR_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
        intent.putExtra(Chooser.START_DIR_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/");
        intent.putExtra(Chooser.SHOW_HIDDEN_FILES, true);
        startActivityForResult(intent, REQUEST_CODE);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(Chooser.RESULT_PATH);
            filePathTxt.setText(filePath);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length < 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("Storage Permission", "Not Granted");
                    finish();
                }
                break;
        }
    }
}
