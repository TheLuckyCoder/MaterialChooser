# File Chooser

A Material-Designed File Chooser Library for Android.

## How to add to your project

Copy the library module to your main project folder

Add this line to your settings.gradle file:
```gradle
include ':library'
```
Then make sure your build.gradle file contains this:
```gradle
dependencies {
    compile project(':library')
}
```

## SDK
This library is built using Build Tools **26.0.0**, requires at least SDK Version **14** and targets Version **26**.
It also uses the AppCompat Libray **26.0.0-beta2**.

## How to use

Open File Picker
```java
new FileChooser(this, 1)
                .showHiddenFiles(true)
                .setFileExtension("txt")
                .start();
```
or
```java
Intent intent = new Intent(this, ChooserActivity.class);
intent.putExtra(Args.SHOW_HIDDEN, true);
intent.putExtra(Args.FILE_EXTENSION, "txt);
startActivityForResult(intent, 1);
```

Override onActivityResult:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 1 && resultCode == RESULT_OK) {
        String filePath = data.getStringExtra(Args.RESULT_FILE_PATH);
        // Do something with the file
    }
}
```

## Documentation

Specify the activity and the request code
```java
new FileChooser(Activity activity, int requestCode);
```

Set the root directory of the picker.
Default: SDCard
```java
FileChooser setRootPath(String rootPath);
```

Set the start directory of the picker.
Default: Root Path
```java
FileChooser setStartPath(String startPath);
```
Filter files by extension.
Ignore all files taht don't have this extension.
```java
FileChooser setStartPath(String startPath);
```

Show or hide hidden files
Default: false
```java
FileChooser showHiddenFiles(boolean show);
```

Start the file chooser activity.
```java
void start();
```

## Customize the look

If you want to change any color of the File Chooser, just redefine one of these colors in your own project:
```xml
<!-- Default Colors -->
<color name="colorPrimary">#29b6f6</color> <!-- Main App Color -->
<color name="colorPrimaryDark">#0086c3</color> <!-- Dark Main App Color -->
<color name="colorFiles">#d500f9</color> <!-- Folders and Files Icon Color -->
```

## License

Apache License 2.0
