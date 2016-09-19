package com.savanto.utils.netcat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
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

    private boolean cancelled;

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
        final String hostname = intent.getStringExtra(EXTRA_HOSTNAME);
        final int port = intent.getIntExtra(EXTRA_PORT, 0);
        final String filename = intent.getStringExtra(EXTRA_FILENAME);

        final Socket socket;
        try {
            socket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
            this.showError(e.getMessage());
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

            out = new FileOutputStream(new File(downloads, filename));
        } catch (IOException e) {
            e.printStackTrace();
            this.showError(e.getMessage());
            this.cleanUp(socket);
            return;
        }

        final InputStream in;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            this.showError(e.getMessage());
            this.cleanUp(socket, out);
            return;
        }

        final int id = 0;
        final NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder notificationBuilder = new Notification.Builder(this);
        notificationBuilder
                .setContentTitle(this.getString(R.string.netcat_notif, hostname, port, filename))
                .setSmallIcon(R.drawable.ic_download)
                .setWhen(System.currentTimeMillis())
                .setUsesChronometer(true)
                .setProgress(0, 0, true);
        notificationManager.notify(id, notificationBuilder.build());

        final String status = this.getString(R.string.netcat_status);
        int total = 0;
        try {
            final byte[] buffer = new byte[8 * 1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                if (this.cancelled) {
                    this.cleanUp(socket, out, in);
                    notificationBuilder
                            .setContentText(String.format(status, "Cancelled", total))
                            .setProgress(0, 0, false)
                            .setWhen(System.currentTimeMillis())
                            .setUsesChronometer(false);
                    notificationManager.notify(id, notificationBuilder.build());
                    return;
                }
                out.write(buffer, 0, count);
                total += count;
                notificationBuilder.setContentText(String.format(status, "In progress", total));
                notificationManager.notify(id, notificationBuilder.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.showError(e.getMessage());
            notificationBuilder
                    .setContentText(String.format(status, "Failed", total))
                    .setProgress(0, 0, false)
                    .setWhen(System.currentTimeMillis())
                    .setUsesChronometer(false);
            notificationManager.notify(id, notificationBuilder.build());
            return;
        } finally {
            this.cleanUp(socket, out, in);
        }

        notificationBuilder
                .setContentText(String.format(status, "Complete", total))
                .setProgress(0, 0, false)
                .setWhen(System.currentTimeMillis())
                .setUsesChronometer(false);
        notificationManager.notify(id, notificationBuilder.build());
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
