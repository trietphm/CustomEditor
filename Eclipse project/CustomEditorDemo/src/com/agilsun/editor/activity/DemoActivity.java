
package com.agilsun.editor.activity;

import hu.scythe.droidwriter.R;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.agilsun.editor.customview.CustomEditText;
import com.agilsun.editor.customview.CustomEditText.EventBack;

public class DemoActivity extends Activity implements OnAmbilWarnaListener {
    private LinearLayout lnl;
    private CustomEditText customEditor;
    private AmbilWarnaDialog colorPickerDialog;
    private ImageView imgChangeColor;
    
    private int selectionStart;
    private int selectionEnd;
    
    private EventBack eventBack = new EventBack() {

        @Override
        public void close() {
            lnl.setVisibility(View.GONE);
        }

        @Override
        public void show() {
            lnl.setVisibility(View.VISIBLE);
        }
    };
    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (customEditor.isFocused()) {
                lnl.setVisibility(View.VISIBLE);
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        colorPickerDialog = new AmbilWarnaDialog(this, Color.BLACK, this);
        ToggleButton boldToggle = (ToggleButton) findViewById(R.id.btnBold);
        ToggleButton italicsToggle = (ToggleButton) findViewById(R.id.btnItalics);
        ToggleButton underlinedToggle = (ToggleButton) findViewById(R.id.btnUnderline);
        imgChangeColor = (ImageView) findViewById(R.id.btnChangeTextColor);
        lnl = (LinearLayout) findViewById(R.id.lnlAction);
        lnl.setVisibility(View.VISIBLE);

        customEditor = (CustomEditText) findViewById(R.id.CustomEditor);
        customEditor.setSingleLine(false);
        customEditor.setMinLines(10);
        customEditor.setBoldToggleButton(boldToggle);
        customEditor.setItalicsToggleButton(italicsToggle);
        customEditor.setUnderlineToggleButton(underlinedToggle);
        customEditor.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lnl.setVisibility(View.VISIBLE);
                }
                else
                {
                    lnl.setVisibility(View.GONE);
                }
            }
        });
        customEditor.setEventBack(eventBack);
        customEditor.setOnClickListener(clickListener);
        imgChangeColor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectionStart = customEditor.getSelectionStart();
                selectionEnd = customEditor.getSelectionEnd();
                colorPickerDialog.show();
            }
        });

    }

    @Override
    public void onCancel(AmbilWarnaDialog dialog) {
        
    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        customEditor.setColor(color, selectionStart, selectionEnd);
        imgChangeColor.setBackgroundColor(color);
    }
}
