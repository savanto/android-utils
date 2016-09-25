package com.savanto.utils.passwordinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;


public final class PasswordKeyboardView extends KeyboardView {
    private final Paint paint = new Paint();
    private boolean isCapsLocked;

    public PasswordKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint.setTextAlign(Paint.Align.RIGHT);
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(30);
    }

    /* package */ void toggleShift(int primaryCode) {
        if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            if (this.isCapsLocked) {
                this.isCapsLocked = false;
                this.setShifted(false);
            } else if (this.isShifted()) {
                this.isCapsLocked = true;
            } else {
                this.setShifted(true);
            }
        } else if (this.isShifted() && ! this.isCapsLocked) {
            this.setShifted(false);
        }
    }

    @Override
    public boolean setShifted(boolean shifted) {
        if (shifted) {
            for (final Keyboard.Key key : this.getKeyboard().getKeys()) {
                if (key.codes[0] >= 'a' && key.codes[0] <= 'z') {
                    key.codes[0] = Character.toUpperCase(key.codes[0]);
                    key.label = key.label.toString().toUpperCase();
                }
            }
        } else {
            for (final Keyboard.Key key : this.getKeyboard().getKeys()) {
                if (key.codes[0] >= 'A' && key.codes[0] <= 'Z') {
                    key.codes[0] = Character.toLowerCase(key.codes[0]);
                    key.label = key.label.toString().toLowerCase();
                }
            }

            this.isCapsLocked = false;
        }

        return super.setShifted(shifted);
    }

    @Override
    protected boolean onLongPress(Keyboard.Key popupKey) {
        if (popupKey.codes.length > 1) {
            this.getOnKeyboardActionListener().onKey(popupKey.codes[1], null);
            return true;
        } else {
            return super.onLongPress(popupKey);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (final Keyboard.Key key : this.getKeyboard().getKeys()) {
            if (key.popupCharacters != null && key.popupCharacters.length() != 0) {
                canvas.drawText(
                        key.popupCharacters,
                        0,
                        1,
                        key.x + (key.width / 4),
                        key.y + (key.height / 4),
                        this.paint
                );
            } else if (key.codes[0] == Keyboard.KEYCODE_SHIFT && this.isShifted()) {
                this.paint.setStyle(
                        this.isCapsLocked ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE
                );
                canvas.drawCircle(
                        key.x + (key.width / 4 * 3),
                        key.y + (key.height / 4),
                        10,
                        this.paint
                );
            }
        }
    }
}
