package com.savanto.utils.wget;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.savanto.utils.R;


public final class WgetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.wget_activity);

        final DownloadManager dm = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        final EditText urlInput = (EditText) this.findViewById(R.id.wget_url);
        final Button startButton = (Button) this.findViewById(R.id.wget_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = urlInput.getText().toString();
                if (url.isEmpty()) {
                    Toast.makeText(
                            WgetActivity.this,
                            R.string.wget_invalid_url,
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                final Uri uri = Uri.parse(url);
                final DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(WgetActivity.this.getString(R.string.wget_app))
                        .setDescription(url)
                        .setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                uri.getLastPathSegment()
                        )
                        .setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        );
                dm.enqueue(request);
            }
        });
    }
}
