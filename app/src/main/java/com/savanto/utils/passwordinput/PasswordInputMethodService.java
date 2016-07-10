package com.savanto.utils.passwordinput;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.savanto.utils.R;


public class PasswordInputMethodService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    private PasswordKeyboardView keyboardView;

    @Override
    public View onCreateInputView() {
        this.keyboardView = (PasswordKeyboardView) this.getLayoutInflater().inflate(
                R.layout.password_input_keyboard,
                null
        );
        this.keyboardView.setOnKeyboardActionListener(this);
        this.keyboardView.setKeyboard(new Keyboard(this, R.xml.password_input_keyboard));

        return this.keyboardView;
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        this.keyboardView.setShifted(false);
        super.onFinishInputView(finishingInput);
    }

    @Override
    public void onPress(int primaryCode) {
    }

    public void onLongPress(int secondaryCode) {
        this.onKey(secondaryCode, null);
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
        case Keyboard.KEYCODE_DELETE:
            final InputConnection inputConnection = this.getCurrentInputConnection();
            inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            break;
        case Keyboard.KEYCODE_SHIFT:
            this.keyboardView.toggleShift(primaryCode);
            break;
        case Keyboard.KEYCODE_CANCEL:
            this.requestHideSelf(0);
        default:
            this.getCurrentInputConnection().commitText(Character.toString((char) primaryCode), 1);
            this.keyboardView.toggleShift(primaryCode);
        }
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
        this.requestHideSelf(0);
    }

    @Override
    public void swipeUp() {
    }
}
