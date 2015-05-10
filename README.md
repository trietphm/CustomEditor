CustomEditor
============

[![Release](https://img.shields.io/github/release/windyzboy/CustomEditor.svg?label=JitPack%20Maven)](https://jitpack.io/#windyzboy/CustomEditor/v1.0.1)

A custom editor for Android. You can set Bold, Italic, Underline and Color.

The color picker library is AmbilWarna and you can find it here: https://code.google.com/p/android-color-picker/
or here https://github.com/yukuku/ambilwarna

This library is extended from EditText, so you can get the output as HTML with `toHtml()` or set it from HTML code by using `Html.fromHtml()` or `getTextHTML()`. But the HTML format will be like this 
```HTML
    <b><i><u><font color="#FF0000">Hello world</font></u><i><b>
```
Because Android doesn't support CSS style for TextView, see <a href="http://javatechig.com/android/display-html-in-android-textview"> this article</a> for more.

## Screen shot
<img src='http://s12.postimg.org/w499omo65/Screenshot_2015_05_09_23_07_56.png'>
<img src='http://s12.postimg.org/mz0wurkrh/Screenshot_2015_05_09_23_10_54.png'>

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
