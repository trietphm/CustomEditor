CustomEditor
============

[![Release](https://img.shields.io/github/release/windyzboy/CustomEditor.svg?label=JitPack%20Maven)](https://jitpack.io/#windyzboy/CustomEditor/v1.0.1)

A custom editor for Android. You can set Bold, Italic, Underline and Color.

The color picker library is AmbilWarna and you can find it here: https://code.google.com/p/android-color-picker/
or here https://github.com/yukuku/ambilwarna

## Screen shot
==============


## Setup
### For Eclipse user
Import to Eclipse
    
    File->Import->Existing Projects into Workspace
    
Reference this lib to your project
    
    Right Click on your project -> Properties -> Android -> Add -> <<Select AmbilWarna & CustomEditor>> 
### For Android Studio user
Step 1. Add the JitPack repository to your build file `build.gradle` with:
```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```
and:

```gradle
dependencies {
    compile 'com.github.jitpack:android-example:1.0.2'
}
```

## How to use?
**Step 1:** Add CustomEditor view element in your layout.xml
```XML
<windyzboy.github.io.customeeditor.CustomEditText
            android:id="@+id/CustomEditor"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="top"
            android:inputType="textFilter|textMultiLine|textNoSuggestions"
            android:minLines="10" >
</windyzboy.github.io.customeeditor.CustomEditText>
```

**Step 2:** Get the Object and use it as an EditText (CustomEditor extends from EditText)
```Java
CustomEditText customEditor = (CustomEditText) findViewById(R.id.CustomEditor);
```

There is a demo in folder Demo (Android Studio) or Eclipse project/CustomEditorDemo (Eclipse), see it for more detail.

## Have fun.
