# Material Chooser

A lightweight Material-Designed File and Folder Chooser Library for Android written in Kotlin.

## SDK
This library is built Kotlin version **1.1.6**, requires at least SDK Version **14** and targets SDK Version **27**.

You will need Android Studio 3.0 or newer to build this project.

## How to add to your project

Copy the library module to your main project folder

Add this line to your settings.gradle file:
```gradle
include ':materialchooser'
```
Then make sure your build.gradle file contains this:
```gradle
dependencies {
    implementation project(':materialchooser')
}
```

## How to use

#### Open File Picker:
```kotlin
Chooser(this, 10)
            .setShowHiddenFiles(true)
            .setFileExtension("txt")
            .start()
```

Or you can just use the default parameters
```kotlin
Chooser(this, 10,
            startPath = Environment.getExternalStorageDirectory().absolutePath + "/Android/",
            showHiddenFiles = true,
            fileExtension = "txt")
            .start()
```

#### Open Folder Picker:
It's the same as for the file picker but you need to add ```setChooserType(Chooser.FOLDER_CHOOSER)```
```kotlin
Chooser(this, 10)
                .setChooserType(Chooser.FOLDER_CHOOSER)
                .start()
```

#### Receive the data:
Override the onActivityResult method
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val filePath = data.getStringExtra(Chooser.RESULT_PATH)
            filePathTxt.text = filePath
        }
    }
```

## Documentation

You can find all the usable methods documented over [here](https://github.com/TheLuckyCoder/MaterialChooser/blob/kotlin/filechooser/src/main/java/net/theluckycoder/filechooser/Chooser.kt

## Customize

If you want to change any color of the File Chooser, just redefine one of these colors in your own project:
```xml
<!-- Default Colors -->
<color name="colorPrimary">#29b6f6</color> <!-- Main App Color -->
<color name="colorPrimaryDark">#0086c3</color> <!-- Dark Main App Color -->
<color name="colorAccent">#d500f9</color> <!-- Folders and Files Icon Color -->
```

or you can directly override the theme
```xml
<!-- Default Theme -->
<style name="FileChooserTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>
```

## License

This project is licensed under the Apache License 2.0
