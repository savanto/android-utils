package com.savanto.utils.netcat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.savanto.utils.R;


public final class NetcatActivity extends Activity {
    private static final String PREF_LAST = "com.savanto.utils.netcat.LastHostPort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.netcat_activity);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final EditText hostInput = (EditText) this.findViewById(R.id.netcat_host);
        final EditText portInput = (EditText) this.findViewById(R.id.netcat_port);
        final EditText fileInput = (EditText) this.findViewById(R.id.netcat_file);
        final Button startButton = (Button) this.findViewById(R.id.netcat_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hostname = hostInput.getText().toString();
                if (hostname.isEmpty()) {
                    NetcatActivity.this.showError(R.string.netcat_invalid_host);
                    return;
                }

                final int port;
                try {
                    port = Integer.parseInt(portInput.getText().toString());
                } catch (NumberFormatException e) {
                    NetcatActivity.this.showError(R.string.netcat_invalid_port);
                    return;
                }
                if (port < 1) {
                    NetcatActivity.this.showError(R.string.netcat_invalid_port);
                    return;
                }

                final String filename = fileInput.getText().toString().replace('/', '_');
                if (filename.isEmpty()) {
                    NetcatActivity.this.showError(R.string.netcat_invalid_file);
                    return;
                }

                prefs.edit().putString(
                        PREF_LAST,
                        NetcatActivity.this.getString(R.string.netcat_host_port, hostname, port)
                ).apply();

                NetcatService.download(NetcatActivity.this, hostname, port, filename);
            }
        });

        final String[] last = prefs.getString(PREF_LAST, ":").split(":");
        if (last.length != 0) {
            hostInput.setText(last[0]);
        }
        if (last.length > 1) {
            portInput.setText(last[1]);
        }
    }

    private void showError(int error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
