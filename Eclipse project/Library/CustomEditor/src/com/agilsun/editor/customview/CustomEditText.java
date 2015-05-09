
package com.agilsun.editor.customview;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class CustomEditText extends EditText {

    // Log tag
    public static final String TAG = "DroidWriter";

    // Style constants
    private static final int STYLE_BOLD = 0;
    private static final int STYLE_ITALIC = 1;
    private static final int STYLE_UNDERLINED = 2;

    // Optional styling button references
    private ToggleButton boldToggle;
    private ToggleButton italicsToggle;
    private ToggleButton underlineToggle;

    // Html image getter that handles the loading of inline images
    private Html.ImageGetter imageGetter;

    private boolean isDeleteCharaters = false;

    // Color
    private int currentColor = -1;
    private EventBack eventBack;
    
   

    // interface
    public interface EventBack {
        public void close();

        public void show();
    }

    public EventBack getEventBack() {
        return eventBack;
    }

    public void setEventBack(EventBack eventBack) {
        this.eventBack = eventBack;
    }

    // XXX Triet H.M. Pham
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            eventBack.close();
        }
        else
        {
            eventBack.show();
        }
        return super.dispatchKeyEvent(event);
    }

    public CustomEditText(Context context) {
        super(context);
        initialize();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        // Add a default imageGetter
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                return null;
            }
        };

        // Add TextWatcher that reacts to text changes and applies the selected
        // styles
        this.addTextChangedListener(new DWTextWatcher());
    }

    /**
     * When the user selects a section of the text, this method is used to
     * toggle the defined style on it. If the selected text already has the
     * style applied, we remove it, otherwise we apply it.
     * 
     * @param style The styles that should be toggled on the selected text.
     */
    private void toggleStyle(int style) {
        int selectionStart = this.getSelectionStart();
        int selectionEnd = this.getSelectionEnd();

        // Reverse if the case is what's noted above
        if (selectionStart > selectionEnd) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }

        if (selectionEnd > selectionStart) {
            switch (style) {
            case STYLE_BOLD:
                boldButtonClick(selectionStart, selectionEnd);
                break;
            case STYLE_ITALIC:
                italicButtonClick(selectionStart, selectionEnd);
                break;
            case STYLE_UNDERLINED:
                underlineButtonClick(selectionStart, selectionEnd);
                break;
            }
        }
    }

    private void underlineButtonClick(int selectionStart, int selectionEnd) {
        boolean exists = false;
        Spannable str = this.getText();
        AgsUnderlineSpan[] underSpan = str.getSpans(selectionStart, selectionEnd, AgsUnderlineSpan.class);

        // If the selected text-part already has UNDERLINE style on it, then we need to disable it
        int underlineStart = -1;
        int underlineEnd = -1;
        for (AgsUnderlineSpan styleSpan : underSpan) {
            if (str.getSpanStart(styleSpan) < selectionStart) {
                underlineStart = str.getSpanStart(styleSpan);
            }
            if (str.getSpanEnd(styleSpan) > selectionEnd) {
                underlineEnd = str.getSpanEnd(styleSpan);
            }
            str.removeSpan(styleSpan);
            exists = true;
        }
        if (underlineStart > -1) {
            str.setSpan(new AgsUnderlineSpan(), underlineStart, selectionStart,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (underlineEnd > -1) {
            str.setSpan(new AgsUnderlineSpan(), selectionEnd, underlineEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Else we set UNDERLINE style on it
        if (!exists) {
            str.setSpan(new AgsUnderlineSpan(), selectionStart, selectionEnd,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            underlineToggle.setChecked(false);
        }

        this.setSelection(selectionStart, selectionEnd);
    }

    private void italicButtonClick(int selectionStart, int selectionEnd) {
        handleStyleSpannable(selectionStart, selectionEnd, android.graphics.Typeface.ITALIC);
    }

    private void boldButtonClick(int selectionStart, int selectionEnd) {
        handleStyleSpannable(selectionStart, selectionEnd, android.graphics.Typeface.BOLD);
    }
    
    private void handleStyleSpannable(int selectionStart, int selectionEnd, int type) {
        boolean exists = false;
        Spannable str = this.getText();
        StyleSpan[] styleSpans = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);

        // If the selected text-part already has BOLD style on it,
        // then
        // we need to disable it
        int styleStart = -1;
        int styleEnd = -1;
        for (StyleSpan styleSpan : styleSpans) {
            if (styleSpan.getStyle() == type) {
                if (str.getSpanStart(styleSpan) < selectionStart) {
                    styleStart = str.getSpanStart(styleSpan);
                }
                if (str.getSpanEnd(styleSpan) > selectionEnd) {
                    styleEnd = str.getSpanEnd(styleSpan);
                }
                str.removeSpan(styleSpan);
                exists = true;
            }
        }
        if (styleStart > -1) {
            str.setSpan(new StyleSpan(type), styleStart, selectionStart,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (styleEnd > -1) {
            str.setSpan(new StyleSpan(type), selectionEnd, styleEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Else we set BOLD style on it
        if (!exists) {
            str.setSpan(new StyleSpan(type), selectionStart, selectionEnd,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            switch (type) {
            case android.graphics.Typeface.ITALIC:
                italicsToggle.setChecked(false);
                break;
            case android.graphics.Typeface.BOLD:
                boldToggle.setChecked(false);
                break;
            }
        }
        this.setSelection(selectionStart, selectionEnd);
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {

        boolean boldExists = false;
        boolean italicsExists = false;
        boolean underlinedExists = false;

        // If the user only placed the cursor around
        if (selStart > 0 && selStart == selEnd) {
            CharacterStyle[] styleSpans = this.getText().getSpans(selStart - 1, selStart, CharacterStyle.class);

            for (int i = 0; i < styleSpans.length; i++) {
                if (styleSpans[i] instanceof StyleSpan) {
                    if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
                        boldExists = true;
                    } else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
                        italicsExists = true;
                    } else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
                        italicsExists = true;
                        boldExists = true;
                    }
                } else if (styleSpans[i] instanceof AgsUnderlineSpan) {
                    underlinedExists = true;
                }
            }
        }

        // Else if the user selected multiple characters
        else if (!TextUtils.isEmpty(CustomEditText.this.getText())) {
            CharacterStyle[] styleSpans = this.getText().getSpans(selStart, selEnd,
                    CharacterStyle.class);

            for (int i = 0; i < styleSpans.length; i++) {
                if (styleSpans[i] instanceof StyleSpan) {
                    if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD) {
                        if (this.getText().getSpanStart(styleSpans[i]) <= selStart
                                && this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
                            boldExists = true;
                        }
                    } else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.ITALIC) {
                        if (this.getText().getSpanStart(styleSpans[i]) <= selStart
                                && this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
                            italicsExists = true;
                        }
                    } else if (((StyleSpan) styleSpans[i]).getStyle() == android.graphics.Typeface.BOLD_ITALIC) {
                        if (this.getText().getSpanStart(styleSpans[i]) <= selStart
                                && this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
                            italicsExists = true;
                            boldExists = true;
                        }
                    }
                } else if (styleSpans[i] instanceof AgsUnderlineSpan) {
                    if (this.getText().getSpanStart(styleSpans[i]) <= selStart
                            && this.getText().getSpanEnd(styleSpans[i]) >= selEnd) {
                        underlinedExists = true;
                    }
                }
            }
        }

        // Display the format settings
        if (boldToggle != null) {
            if (boldExists)
                boldToggle.setChecked(true);
            else
                boldToggle.setChecked(false);
        }

        if (italicsToggle != null) {
            if (italicsExists)
                italicsToggle.setChecked(true);
            else
                italicsToggle.setChecked(false);
        }

        if (underlineToggle != null) {
            if (underlinedExists)
                underlineToggle.setChecked(true);
            else
                underlineToggle.setChecked(false);
        }
    }
    // Get and set Spanned, styled text
    public Spanned getSpannedText() {
        return this.getText();
    }

    public void setSpannedText(Spanned text) {
        this.setText(text);
    }

    // Get and set simple text as simple strings
    public String getStringText() {
        return this.getText().toString();
    }

    public void setStringText(String text) {
        this.setText(text);
    }

    // Get and set styled HTML text
    public String getTextHTML() {
        return Html.toHtml(this.getText());
    }

    public void setTextHTML(String text) {
        this.setText(Html.fromHtml(text, imageGetter, null));
    }

    // Set the default image getter that handles the loading of inline images
    public void setImageGetter(Html.ImageGetter imageGetter) {
        this.imageGetter = imageGetter;
    }

    
    
    // Style toggle button setters
    public void setBoldToggleButton(ToggleButton button) {
        boldToggle = button;

        boldToggle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toggleStyle(STYLE_BOLD);
            }
        });
    }

    public void setItalicsToggleButton(ToggleButton button) {
        italicsToggle = button;

        italicsToggle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toggleStyle(STYLE_ITALIC);
            }
        });
    }

    public void setUnderlineToggleButton(ToggleButton button) {
        underlineToggle = button;

        underlineToggle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                toggleStyle(STYLE_UNDERLINED);
            }
        });
    }

    public void setColor(int color, int selectionStart, int selectionEnd) {
        currentColor = color;

        // Reverse if the case is what's noted above
        if (selectionStart > selectionEnd) {
            int temp = selectionEnd;
            selectionEnd = selectionStart;
            selectionStart = temp;
        }

        // The selectionEnd is only greater then the selectionStart position
        // when the user selected a section of the text. Otherwise, the 2
        // variables
        // should be equal (the cursor position).
        if (selectionEnd > selectionStart) {
            Spannable spannable = this.getText();
            ForegroundColorSpan[] appliedStyles = spannable.getSpans(selectionStart, selectionEnd,
                    ForegroundColorSpan.class);
            if (appliedStyles != null && appliedStyles.length > 0) {
                int colorStart = -1;
                int colorEnd = -1;
                int beforeColor = 0;
                int afterColor = 0;
                for (ForegroundColorSpan foregroundColorSpan : appliedStyles) {
                    if (spannable.getSpanStart(foregroundColorSpan) < selectionStart) {
                        colorStart = spannable.getSpanStart(foregroundColorSpan);
                        beforeColor = foregroundColorSpan.getForegroundColor();
                    }
                    if (spannable.getSpanEnd(foregroundColorSpan) > selectionEnd) {
                        colorEnd = spannable.getSpanEnd(foregroundColorSpan);
                        afterColor = foregroundColorSpan.getForegroundColor();
                    }
                    spannable.removeSpan(foregroundColorSpan);
                }

                //
                if (colorStart > -1) {
                    spannable.setSpan(new ForegroundColorSpan(beforeColor), colorStart,
                            selectionStart,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (colorEnd > -1) {
                    spannable.setSpan(new ForegroundColorSpan(afterColor), selectionEnd, colorEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                spannable.setSpan(new ForegroundColorSpan(color), selectionStart, selectionEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannable.setSpan(new ForegroundColorSpan(color), selectionStart, selectionEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            this.setSelection(selectionStart, selectionEnd);
        }
    }

    private class DWTextWatcher implements TextWatcher {
        private int beforeChangeTextLength = 0;
        private int appendTextLength = 0;
        /**
         * After text Change
         */
        @Override
        public void afterTextChanged(Editable editable) {
            // Add style as the user types if a toggle button is enabled
            int position = Selection.getSelectionStart(CustomEditText.this.getText());
            appendTextLength = Math.abs(position - beforeChangeTextLength);
            
            //XXX: Fixed bold error when text size not change  
            if (appendTextLength == 0 || isDeleteCharaters) {
                return;
            }
            
            if (position < 0) {
                position = 0;
            }

            if (position > 0) {
                CharacterStyle[] appliedStyles = editable.getSpans(position - 1, position, CharacterStyle.class);

                StyleSpan currentBoldSpan = null;
                StyleSpan currentItalicSpan = null;
                AgsUnderlineSpan currentAgsUnderlineSpan = null;
                ForegroundColorSpan currentForegroundColorSpan = null;

                // Look for possible styles already applied to the entered text
                for (int i = 0; i < appliedStyles.length; i++) {
                    if (appliedStyles[i] instanceof StyleSpan) {
                        if (((StyleSpan) appliedStyles[i]).getStyle() == android.graphics.Typeface.BOLD) {
                            // Bold style found
                            currentBoldSpan = (StyleSpan) appliedStyles[i];
                        } else if (((StyleSpan) appliedStyles[i]).getStyle() == android.graphics.Typeface.ITALIC) {
                            // Italic style found
                            currentItalicSpan = (StyleSpan) appliedStyles[i];
                        }
                    } else if (appliedStyles[i] instanceof AgsUnderlineSpan) {
                        // Underlined style found
                        currentAgsUnderlineSpan = (AgsUnderlineSpan) appliedStyles[i];
                    } else if (appliedStyles[i] instanceof ForegroundColorSpan) {
                        if (currentForegroundColorSpan == null) {
                            currentForegroundColorSpan = (ForegroundColorSpan) appliedStyles[i];
                        }
                    }
                }

                handleInsertBoldCharacter(editable, position, currentBoldSpan);
                handleInsertItalicCharacter(editable, position, currentItalicSpan);
                handleInsertUnderlineCharacter(editable, position, currentAgsUnderlineSpan);
                handleInsertColorCharacter(editable, position, currentForegroundColorSpan);
            }
            
        }
        
        private void handleInsertColorCharacter(Editable editable, int position, ForegroundColorSpan currentForegroundColorSpan) {
            // Handle color
            if (currentForegroundColorSpan != null) {
                if (currentForegroundColorSpan.getForegroundColor() != currentColor) {
                    int colorStart = editable.getSpanStart(currentForegroundColorSpan);
                    int colorEnd = editable.getSpanEnd(currentForegroundColorSpan);

                    if (position == colorEnd) {
                        ForegroundColorSpan nextSpan = getNextForegroundColorSpan(editable, position);
                        if (nextSpan != null) {
                            if (currentColor == nextSpan.getForegroundColor()) {
                                int colorEndNextSpan = editable.getSpanEnd(nextSpan);
                                editable.removeSpan(currentForegroundColorSpan);
                                editable.removeSpan(nextSpan);
                                // set before span
                                editable.setSpan(new ForegroundColorSpan(currentForegroundColorSpan.getForegroundColor()), colorStart,
                                        colorEnd - appendTextLength,
                                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                // set after span
                                editable.setSpan(new ForegroundColorSpan(nextSpan.getForegroundColor()),
                                        position - appendTextLength,
                                        colorEndNextSpan,
                                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                                return;
                            }
                        }

                    }
                    editable.removeSpan(currentForegroundColorSpan);
                    if (position - appendTextLength < colorEnd && colorStart != colorEnd) {
                        // Cursor in the text's middle with different color
                        int oldColor = currentForegroundColorSpan.getForegroundColor();

                        if (colorStart < position - appendTextLength) {
                            // Before inserting text
                            editable.setSpan(new ForegroundColorSpan(oldColor), colorStart,
                                    position - appendTextLength,
                                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }

                        // At inserting
                        editable.setSpan(new ForegroundColorSpan(currentColor), position - appendTextLength,
                                position,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                        if (position < colorEnd) {
                            // After inserting
                            editable.setSpan(new ForegroundColorSpan(oldColor), position, colorEnd,
                                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                    } else
                    {
                        // Cursor in the end
                        editable.setSpan(new ForegroundColorSpan(currentColor), position - appendTextLength,
                                colorEnd,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }
                }
            }
            else if (currentColor != -1) {
                ForegroundColorSpan nextSpan = getNextForegroundColorSpan(editable, position);
                if (nextSpan != null)
                {
                    int colorEndNextSpan = editable.getSpanEnd(nextSpan);
                    if (currentColor == nextSpan.getForegroundColor())
                    {
                        editable.removeSpan(nextSpan);
                        // set before span
                        editable.setSpan(new ForegroundColorSpan(nextSpan.getForegroundColor()),
                                position - appendTextLength,
                                colorEndNextSpan,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

                        return;
                    }
                }
                editable.setSpan(new ForegroundColorSpan(currentColor),
                        position - appendTextLength, position,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        
        private ForegroundColorSpan getNextForegroundColorSpan(Editable editable, int position) {
            ForegroundColorSpan nextSpans[] = editable.getSpans(position, position + 1, ForegroundColorSpan.class);
            if (nextSpans.length > 0) {
                return nextSpans[0];
            }
            return null;
        }

        private void handleInsertUnderlineCharacter(Editable editable, int position,
                AgsUnderlineSpan currentAgsUnderlineSpan) {
            // Handle the underlined style toggle button if it's present
            if (underlineToggle != null && underlineToggle.isChecked()
                    && currentAgsUnderlineSpan == null) {
                editable.setSpan(new AgsUnderlineSpan(), position - appendTextLength, position,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (underlineToggle != null && !underlineToggle.isChecked()
                    && currentAgsUnderlineSpan != null) {
                int underLineStart = editable.getSpanStart(currentAgsUnderlineSpan);
                int underLineEnd = editable.getSpanEnd(currentAgsUnderlineSpan);

                editable.removeSpan(currentAgsUnderlineSpan);
                if (underLineStart <= (position - appendTextLength)) {
                    editable.setSpan(new AgsUnderlineSpan(), underLineStart, position - appendTextLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // We need to split the span
                if (underLineEnd > position) {
                    editable.setSpan(new AgsUnderlineSpan(), position, underLineEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        private void handleInsertItalicCharacter(Editable editable, int position,
                StyleSpan currentItalicSpan) {
            // Handle the italics style toggle button if it's present
            if (italicsToggle != null && italicsToggle.isChecked() && currentItalicSpan == null) {
                editable.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), position - appendTextLength,
                        position,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (italicsToggle != null && !italicsToggle.isChecked()
                    && currentItalicSpan != null) {
                int italicStart = editable.getSpanStart(currentItalicSpan);
                int italicEnd = editable.getSpanEnd(currentItalicSpan);

                editable.removeSpan(currentItalicSpan);
                if (italicStart <= (position - appendTextLength)) {
                    editable.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC),
                            italicStart, position - appendTextLength,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // Split the span
                if (italicEnd > position) {
                    editable.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), position,
                            italicEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        private void handleInsertBoldCharacter(Editable editable, int position,
                StyleSpan currentBoldSpan) {
            // Handle the bold style toggle button if it's present
            if (boldToggle != null) {
                if (boldToggle.isChecked() && currentBoldSpan == null) {
                    // The user switched the bold style button on and the
                    // character doesn't have any bold
                    // style applied, so we start a new bold style span. The
                    // span is inclusive,
                    // so any new characters entered right after this one
                    // will automatically get this style.
                    editable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                            position - appendTextLength, position,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else if (!boldToggle.isChecked() && currentBoldSpan != null) {
                    // The user switched the bold style button off and the
                    // character has bold style applied.
                    // We need to remove the old bold style span, and define
                    // a new one that end 1 position right
                    // before the newly entered character.
                    int boldStart = editable.getSpanStart(currentBoldSpan);
                    int boldEnd = editable.getSpanEnd(currentBoldSpan);

                    editable.removeSpan(currentBoldSpan);
                    if (boldStart <= (position - appendTextLength)) {
                        editable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                                boldStart, position - appendTextLength,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // The old bold style span end after the current cursor
                    // position, so we need to define a
                    // second newly created style span too, which begins
                    // after the newly entered character and
                    // ends at the old span's ending position. So we split
                    // the span.
                    if (boldEnd > position) {
                        editable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                                position, boldEnd,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        /**
         * Before Text Change
         */
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            int position = Selection.getSelectionStart(CustomEditText.this.getText());
            if (position < 0) {
                position = 0;
            }
            
            beforeChangeTextLength = position;
            if ((count - after == 1) || (s.length() == 0) && position > 0) { // Delete character
                Editable editable = CustomEditText.this.getText();
                
                removeForegroundColorSpan(position, editable);
                removeAgsUnderlineSpan(position, editable);
                removeStyleSpan(position, editable, android.graphics.Typeface.ITALIC);
                removeStyleSpan(position, editable, android.graphics.Typeface.BOLD);
            }
        }

        private void removeForegroundColorSpan(int position, Editable editable) {
            ForegroundColorSpan previousColorSpan = (ForegroundColorSpan) getPreviousForegroundColorSpan(editable, position, ForegroundColorSpan.class);
            ForegroundColorSpan[] appliedStyles = editable.getSpans(position - 1, position, ForegroundColorSpan.class);
                
            if (appliedStyles.length > 0 && appliedStyles[0] != null && previousColorSpan != null 
                    && previousColorSpan.getForegroundColor() !=  appliedStyles[0].getForegroundColor()) {
                ForegroundColorSpan colorSpan = (ForegroundColorSpan) appliedStyles[0];
                int colorStart = editable.getSpanStart(colorSpan);
                int colorEnd = editable.getSpanEnd(colorSpan);

                editable.removeSpan(colorSpan);
                if (colorStart < (position - 1)) {
                    editable.setSpan(new AgsUnderlineSpan(), colorStart, position - 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // We need to split the span
                if (colorEnd > position) {
                    editable.setSpan(new AgsUnderlineSpan(), position, colorEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        
        private void removeAgsUnderlineSpan(int position, Editable editable) {
            AgsUnderlineSpan previousColorSpan = (AgsUnderlineSpan) getPreviousForegroundColorSpan(editable, position, AgsUnderlineSpan.class);
            AgsUnderlineSpan[] appliedStyles = editable.getSpans(position - 1, position, AgsUnderlineSpan.class);
            
            if (appliedStyles.length > 0 && previousColorSpan == null) {
                AgsUnderlineSpan colorSpan = (AgsUnderlineSpan) appliedStyles[0];
                int underLineStart = editable.getSpanStart(colorSpan);
                int underLineEnd = editable.getSpanEnd(colorSpan);

                editable.removeSpan(colorSpan);
                if (underLineStart < (position - 1)) {
                    editable.setSpan(new AgsUnderlineSpan(), underLineStart, position - 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // We need to split the span
                if (underLineEnd > position) {
                    editable.setSpan(new AgsUnderlineSpan(), position, underLineEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        
        private void removeStyleSpan(int position, Editable editable, int type) {
            StyleSpan previousColorSpan = (StyleSpan) getPreviousForegroundColorSpan(editable, position, StyleSpan.class);
            StyleSpan[] appliedStyles = editable.getSpans(position - 1, position, StyleSpan.class);
            
            StyleSpan styleSpan = null;
            for (StyleSpan span : appliedStyles) {
                if (span.getStyle() == type) {
                    styleSpan = span;
                }
            }
            
            if (styleSpan != null && previousColorSpan == null) {
                int styleStart = editable.getSpanStart(styleSpan);
                int styleEnd = editable.getSpanEnd(styleSpan);

                editable.removeSpan(styleSpan);
                if (styleStart < (position - 1)) {
                    editable.setSpan(new StyleSpan(type), styleStart, position - 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                // We need to split the span
                if (styleEnd > position) {
                    editable.setSpan(new StyleSpan(type), position, styleEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        
        private Object getPreviousForegroundColorSpan(Editable editable, int position, Class<?> clss) {
            if (position - 2 >= 0) {
                Object[] nextSpans = editable.getSpans(position - 2, position - 1, clss);
                if (nextSpans.length > 0) {
                    return nextSpans[0];
                }
            }
            return null;
        }

        /**
         * On Text Change
         */
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isDeleteCharaters = count == 0 ? true : false;
            // Remove all span when EditText is empty
            if (CustomEditText.this.getText().toString().isEmpty()) {
                CharacterStyle[] appliedStyles = CustomEditText.this.getText().getSpans(0,
                        CustomEditText.this.getText().length(), CharacterStyle.class);
                for (CharacterStyle characterStyle : appliedStyles) {
                    CustomEditText.this.getText().removeSpan(characterStyle);
                }
            }
        }
    }
}
