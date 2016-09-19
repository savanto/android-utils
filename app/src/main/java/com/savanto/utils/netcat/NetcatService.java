package com.savanto.utils.netcat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.savanto.utils.R;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class NetcatService extends IntentService {
    private static final String SERVICE_NAME = "NetcatService";

    private static final String EXTRA_HOSTNAME = "com.savanto.netcat.Hostname";
    private static final String EXTRA_PORT = "com.savanto.netcat.Port";
    private static final String EXTRA_FILENAME = "com.savanto.netcat.Filename";

    public NetcatService() {
        super(SERVICE_NAME);
    }

    /* package */ static void download(Context context, String hostname, int port, String filename) {
        context.startService(
                new Intent(context, NetcatService.class)
                        .putExtra(EXTRA_HOSTNAME, hostname)
                        .putExtra(EXTRA_PORT, port)
                        .putExtra(EXTRA_FILENAME, filename)
        );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Socket socket;
        try {
            socket = new Socket(
                    intent.getStringExtra(EXTRA_HOSTNAME),
                    intent.getIntExtra(EXTRA_PORT, 0)
            );
        } catch (IOException e) {
            this.showError(e.getMessage());
            e.printStackTrace();
            return;
        }

        if (! socket.isConnected()) {
            this.showError(R.string.netcat_connection_error);
            this.cleanUp(socket);
            return;
        }

        final OutputStream out;
        try {
            final File downloads = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
            );
            //noinspection ResultOfMethodCallIgnored
            downloads.mkdirs();

            out = new FileOutputStream(new File(downloads, intent.getStringExtra(EXTRA_FILENAME)));
        } catch (IOException e) {
            this.showError(e.getMessage());
            e.printStackTrace();
            this.cleanUp(socket);
            return;
        }

        final InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            this.showError(e.getMessage());
            e.printStackTrace();
            this.cleanUp(socket, out);
            return;
        }

        try {
            final byte[] buffer = new byte[8 * 1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        } catch (IOException e) {
            this.showError(e.getMessage());
            e.printStackTrace();
        } finally {
            this.cleanUp(socket, out, in);
        }
    }

    private void showError(final int error) {
        new Handler(this.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NetcatService.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showError(final String error) {
        new Handler(this.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NetcatService.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cleanUp(Closeable... closeables) {
        if (closeables != null && closeables.length != 0) {
            for (final Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) { /* NOP */ }
                }
            }
        }
    }
}
