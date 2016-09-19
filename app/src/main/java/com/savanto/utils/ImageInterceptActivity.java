package com.savanto.utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;


/**
 * Intercept {@code android.intent.action.VIEW} Intents from Gmail to workaround bug:
 * if there's no Activity that can respond to view image Intents, Gmail cannot show it, but if an
 * Activity can respond, Gmail can actually show the image no problem.
 *
 * This can be expanded later to handle viewing images by other means.
 */
public final class ImageInterceptActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.image_intercept_activity);

        ((ImageView) this.findViewById(R.id.image_intercept_view))
                .setImageURI(this.getIntent().getData());
    }
}
