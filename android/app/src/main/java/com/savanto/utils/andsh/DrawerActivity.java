package com.savanto.utils.andsh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.savanto.utils.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public final class DrawerActivity extends Activity {
    /* package */ static void start(Context context) {
        context.startActivity(new Intent(context, DrawerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.andsh_drawer_activity);
        final GridView gridView = (GridView) this.findViewById(R.id.andsh_drawer);
        final PackageManager pm = this.getPackageManager();

        new AsyncTask<Void, Void, List<ResolveInfo>>() {
            @Override
            protected List<ResolveInfo> doInBackground(Void... params) {
                final Intent filter =
                        new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
                final List<ResolveInfo> infos = pm.queryIntentActivities(filter, 0);
                Collections.sort(infos, new Comparator<ResolveInfo>() {
                    @Override
                    public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                        return lhs.loadLabel(pm).toString().compareToIgnoreCase(
                                rhs.loadLabel(pm).toString()
                        );
                    }
                });

                return infos;
            }

            @Override
            protected void onPostExecute(final List<ResolveInfo> infos) {
                final LayoutInflater inflater = DrawerActivity.this.getLayoutInflater();
                gridView.setAdapter(new ArrayAdapter<ResolveInfo>(
                        DrawerActivity.this,
                        R.layout.andsh_drawer_item,
                        infos
                ) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = inflater.inflate(R.layout.andsh_drawer_item, null);
                        }
                        final TextView item = (TextView) convertView;
                        final ResolveInfo info = this.getItem(position);
                        if (info != null) {
                            item.setText(info.loadLabel(pm));
                            final Drawable icon = info.loadIcon(pm);
                            icon.setBounds(0, 0, 144, 144);
                            item.setCompoundDrawables(null, icon, null, null);
                        }

                        return convertView;
                    }
                });

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DrawerActivity.this.startActivity(pm.getLaunchIntentForPackage(
                                infos.get(position).activityInfo.packageName
                        ));
                        DrawerActivity.this.finish();
                    }
                });
            }
        }.execute();
    }
}
