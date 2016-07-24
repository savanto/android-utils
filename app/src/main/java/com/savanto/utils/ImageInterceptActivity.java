package com.savanto.utils;

import android.app.Activity;

/**
 * Intercept {@code android.intent.action.VIEW} Intents from Gmail to workaround bug:
 * if there's no Activity that can respond to view image Intents, Gmail cannot show it, but if an
 * Activity can respond, Gmail can actually show the image no problem.
 *
 * This can be expanded later to handle viewing images by other means.
 */
public final class ImageInterceptActivity extends Activity {
}
