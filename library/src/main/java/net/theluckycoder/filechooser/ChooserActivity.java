package net.theluckycoder.filechooser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChooserActivity extends AppCompatActivity {

    private ListView listView;
    private FileArrayAdapter adapter;
    private String rootDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private File currentDir;
    private boolean showHiddenFiles = false;
    private String fileExtension;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Option option = adapter.getItem(position);
                if (option != null) {
                    if(option.isFolder()){
                        currentDir = new File(option.getPath());
                        load(currentDir);
                    } else
                        onFileClick(option);
                }
            }
        });

        Intent intent = getIntent();

        rootDirPath = getExtraString(intent, Args.ROOT_DIR_PATH, rootDirPath);
        currentDir = new File(getExtraString(intent, Args.START_DIR_PATH, rootDirPath));
        showHiddenFiles = intent.getBooleanExtra(Args.SHOW_HIDDEN, false);
        fileExtension = getExtraString(intent, Args.FILE_EXTENSION, "");
        load(currentDir);
    }

    @Override
    public void onBackPressed() {
        if (!currentDir.getAbsolutePath().equals(rootDirPath)) {
            currentDir = currentDir.getParentFile();
            load(currentDir);
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentPath", currentDir.getAbsolutePath());
        outState.putBoolean("showHiddenFiles", showHiddenFiles);
        outState.putString("fileExtension", fileExtension);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String currentPath = savedInstanceState.getString("currentPath");
        showHiddenFiles = savedInstanceState.getBoolean("showHiddenFiles", false);
        String extension = savedInstanceState.getString("fileExtension");
        if (currentPath != null)
            currentDir = new File(currentPath);
        if (extension != null)
            fileExtension = extension;
        load(currentDir);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getExtraString(Intent intent, String name, String defaultValue) {
        String extra = intent.getStringExtra(name);
        if (extra != null)
            return extra;
        else
            return defaultValue;
    }

    private void load(File file) {
        File[] dirs = file.listFiles();
        setTitle(file.getPath().replace(rootDirPath, getString(R.string.file_chooser_device)));
        List<Option> dir = new ArrayList<>();
        List<Option> fls = new ArrayList<>();

        try {
            for(File ff: dirs) {
                String fName = ff.getName();
                if (ff.canRead()) {
                    if (showHiddenFiles) {
                        if (ff.isDirectory())
                            dir.add(new Option(fName, ff.getAbsolutePath(), true));
                        else if (!fileExtension.equals("") && ff.getName().substring(fName.lastIndexOf(".") + 1, fName.length()).equals(fileExtension))
                            fls.add(new Option(fName, ff.getAbsolutePath(), false));
                        else if (fileExtension.equals(""))
                            fls.add(new Option(fName, ff.getAbsolutePath(), false));
                    } else {
                        if (!ff.getName().substring(0, 1).equals(".")) {
                            if (ff.isDirectory())
                                dir.add(new Option(fName, ff.getAbsolutePath(), true));
                            else if (!fileExtension.equals("") && ff.getName().substring(fName.lastIndexOf(".") + 1, fName.length()).equals(fileExtension))
                                fls.add(new Option(fName, ff.getAbsolutePath(), false));
                            else if (fileExtension.equals(""))
                                fls.add(new Option(fName, ff.getAbsolutePath(), false));
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()))
            dir.add(0, new Option("Parent Directory", file.getParent(), true));
        adapter = new FileArrayAdapter(this, R.layout.file_view, dir);
        listView.setAdapter(adapter);
    }

    private void onFileClick(Option option) {
        Intent data = new Intent();
        data.putExtra(Args.RESULT_FILE_PATH, option.getPath());
        setResult(RESULT_OK, data);
        finish();
    }
}
