package com.savanto.utils.andsh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public final class AndshActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.findViewById(android.R.id.content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DrawerActivity.start(AndshActivity.this);
                return true;
            }
        });
    }
}
