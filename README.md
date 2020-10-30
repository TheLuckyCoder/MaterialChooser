# Material Chooser
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Download](https://api.bintray.com/packages/theluckycoder/materialchooser/material-chooser/images/download.svg) ](https://bintray.com/theluckycoder/materialchooser/material-chooser/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/vipulasri/Timeline-View/blob/master/LICENSE)

A lightweight Material-Designed File and Folder Chooser Library for Android written in Kotlin.

You will need Android Studio 3.5 or newer to build this project.

## Including it in your Project

**Using Gradle**
```gradle
dependencies {
    implementation 'net.theluckycoder.materialchooser:materialchooser:1.2.3'
}
```

**Using Maven**

```maven
<dependency>
  <groupId>net.theluckycoder.materialchooser</groupId>
  <artifactId>materialchooser</artifactId>
  <version>1.2.3</version>
  <type>pom</type>
</dependency>
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
    fileExtensions = listOf("txt")
).start()
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
    data ?: return

    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
        val path = data.getStringExtra(Chooser.RESULT_PATH)
        // Do what you wish with this path
    }
}
```

## Documentation

You can find all the usable methods documented [here](https://github.com/TheLuckyCoder/MaterialChooser/blob/master/materialchooser/src/main/java/net/theluckycoder/materialchooser/Chooser.kt)

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
<style name="FileChooserTheme" parent="Theme.AppCompat.DayNight.DarkActionBar">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>
```

## License

```
Copyright 2018-2020 TheLuckyCoder

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
