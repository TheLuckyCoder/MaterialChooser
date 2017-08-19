# Material File Chooser

A Material-Designed File Chooser Library for Android written in Kotlin.

## SDK
This library is built using Build Tools **26.0.1** and Kotlin version **1.1.4**, requires at least SDK Version **14** and targets SDK Version **26**.
It also uses the AppCompat Library version **26.0.1** to maintain backwards compatibility.

You will need Android Studio 3.0 or newer to build this project.

## How to add to your project

Copy the filechooser module to your main project folder

Add this line to your settings.gradle file:
```gradle
include ':filechooser'
```
Then make sure your build.gradle file contains this:
```gradle
dependencies {
    implementation project(':filechooser')
}
```

## How to use

Open File Picker
```java
new FileChooser(this, 10)
                .showHiddenFiles(true)
                .setFileExtension("txt")
                .start();
```
or you can you Intents
```java
Intent intent = new Intent(this, ChooserActivity.class);
intent.putExtra(Chooser.showHiddenFiles, true);
intent.putExtra(Chooser.fileExtension, "txt");
startActivityForResult(intent, 1);
```

Override onActivityResult:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 10 && resultCode == RESULT_OK) {
        String filePath = data.getStringExtra(Chooser.resultFilePath);
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
Default: The value of the root path
```java
FileChooser setStartPath(String startPath);
```
Filter files by extension.
Ignore all files that don't have this extension.
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

This project is licensed under the Apache License 2.0