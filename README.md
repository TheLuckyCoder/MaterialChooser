# Material Chooser
[ ![Download](https://api.bintray.com/packages/theluckycoder/materialchooser/material-chooser/images/download.svg) ](https://bintray.com/theluckycoder/materialchooser/material-chooser/_latestVersion)

A lightweight Material-Designed File and Folder Chooser Library for Android written in Kotlin.

## SDK
This library requires at least SDK Version **14** and 
targets SDK Version **27**.

You will need Android Studio 3.0 or newer to build this project.

## Including it in your Project

Add the following to your app's build.gradle file:
```gradle
dependencies {
    compile 'net.theluckycoder.materialchooser:materialchooser:1.1.2'
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

You can find all the usable methods documented [here](https://github.com/TheLuckyCoder/MaterialChooser/blob/kotlin/filechooser/src/main/java/net/theluckycoder/filechooser/Chooser.kt)

## Customize

If you want to change any color of the File Chooser, just redefine one of these colors in your own project:
```xml
<!-- Default Colors -->
<color name="colorPrimary">#29b6f6</color> <!-- Main Chooser Color -->
<color name="colorPrimaryDark">#0086c3</color> <!-- Dark Main Chooser Color -->
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

```
Copyright 2017 TheLuckyCoder

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
