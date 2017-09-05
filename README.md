# Material Chooser

A lightweight Material-Designed File and Folder Chooser Library for Android written in Kotlin.

## SDK
This library is built using Build Tools **26.0.1** and Kotlin version **1.1.4-3**, requires at least SDK Version **14** and targets SDK Version **26**.
It also uses the AppCompat Library version **26.0.2** to maintain backwards compatibility.

You will need Android Studio 3.0 or newer to build this project.

## How to add to your project

Copy the Chooser module to your main project folder

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

#### Open File Picker:
```java
new Chooser(this, 10)
                .showHiddenFiles(true)
                .setFileExtension("txt")
                .start();
```
or you can use Intents
```java
Intent intent = new Intent(this, ChooserActivity.class);
intent.putExtra(Chooser.SHOW_HIDDEN_FILES, true);
intent.putExtra(Chooser.FILE_EXTENSION, "txt");
startActivityForResult(intent, 10);
```

#### Open Folder Picker:
It's the same as for the file picker but you need to add "setChooserType(false)"
```java
new Chooser(this, 10)
                .setChooserType(false)
                .start();
```

#### Receive the data:
Override the onActivityResult method
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 10 && resultCode == RESULT_OK) {
        String filePath = data.getStringExtra(Chooser.RESULT_PATH);
        // Do something with the file
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
