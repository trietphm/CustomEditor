CustomEditor
============

A custom editor for Android. You can set Bold, Italic, Underline and Color.

The color picker library is AmbilWarna and you can find it here: https://code.google.com/p/android-color-picker/

    
## Setup
### 1. Import to Eclipse
    
    File->Import->Existing Projects into Workspace
    
### 2. Reference this lib to your project
    
    Right Click on your project -> Properties -> Android -> Add -> <<Select AmbilWarna & CustomEditor>> 

## How to use?
#### 1. Add CustomEditor view element in your layout.xml
```XML
<com.agilsun.editor.customview.CustomEditText
            android:id="@+id/CustomEditor"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="top"
            android:inputType="textFilter|textMultiLine|textNoSuggestions"
            android:minLines="10" >
</com.agilsun.editor.customview.CustomEditText>
```
#### 2. Get the Object and use it as an EditText (CustomEditor extends from EditText)
```Java
CustomEditText customEditor = (CustomEditText) findViewById(R.id.CustomEditor);
```
#### 3. There is a demo in CustomEditorDemo project, see it for more detail.

## Have fun.
