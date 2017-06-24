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

import net.theluckycoder.filechooser.FileChooser;

public class MainActivity extends AppCompatActivity {

    private final int mRequestCode = 100;
    private final int PERMISSION_REQUEST_CODE = 200;
    private TextView filePathTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePathTxt = findViewById(R.id.filePath);

        // Check for Storage Permission of Android 6 and above
        if (checkPermission()) requestPermission();
    }


    public void startFileChooser(View view) {
        new FileChooser(this, mRequestCode)
                .setRootPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                .setStartPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/")
                .showHiddenFiles(true)
                .setFileExtension("txt")
                .start();

        /* Second Method:
        Intent intent = new Intent(this, ChooserActivity.class);
        intent.putExtra(Args.ROOT_DIR_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
        intent.putExtra(Args.SHOW_HIDDEN, true);
        startActivityForResult(intent, mRequestCode);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == mRequestCode && resultCode == RESULT_OK)
            filePathTxt.setText(data.getStringExtra(Args.RESULT_FILE_PATH));
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
