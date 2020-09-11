package com.plaltair.texteditor;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pierluca Lippi on 03/03/18.
 */

public class TextFilesCustomAdapter extends BaseAdapter {

    private Context context;
    private List<TextFilesRowItem> rowItems;

    private List<Integer> selectedPositions = new ArrayList<Integer>();
    private List<String> selectedFileNames = new ArrayList<String>();

    private boolean isSelectMode;

    public TextFilesCustomAdapter(Context context, List<TextFilesRowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public void deleteSlectedFiles() {
        for (int i = 0; i < selectedFileNames.size(); i++) {
            File file = new File(context.getFilesDir() + "/SavedTexts/" + selectedFileNames.get(i));
            file.delete();
        }

        Activity activity = (Activity)context;
        activity.finish();
        context.startActivity(activity.getIntent());
    }

    private class ViewHolder {
        TextView fileName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_text_files, null);

            holder.fileName = convertView.findViewById(R.id.fileName);

            holder.fileName.getLayoutParams().height = PercentageSize.percentageVertical(10);

            ScaledFont.fixTextSize(holder.fileName, 20);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        final TextFilesRowItem row_pos = rowItems.get(position);

        String fileName = row_pos.getFileName();

        holder.fileName.setText(fileName);

        holder.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectMode) {
                    boolean found = false;
                    for (int i = 0; i < selectedPositions.size(); i++) {
                        if (position == selectedPositions.get(i)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        selectedPositions.remove(Integer.valueOf(position));
                        selectedFileNames.remove(row_pos.getFileName());
                        notifyDataSetChanged();
                        if (selectedPositions.size() == 0) {
                            isSelectMode = false;
                            Activity activity = (Activity) context;
                            activity.invalidateOptionsMenu();
                        }
                    }
                    else {
                        isSelectMode = true;
                        selectedPositions.add(position);
                        selectedFileNames.add(row_pos.getFileName());
                        notifyDataSetChanged();
                        Activity activity = (Activity) context;
                        activity.invalidateOptionsMenu();
                    }
                }
                else {
                    Intent intent = new Intent(context, Read.class);
                    intent.putExtra("fileName", row_pos.getFileName());
                    context.startActivity(intent);
                }
            }
        });

        holder.fileName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean found = false;
                for (int i = 0; i < selectedPositions.size(); i++) {
                    if (position == selectedPositions.get(i)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    selectedPositions.remove(Integer.valueOf(position));
                    selectedFileNames.remove(row_pos.getFileName());
                    notifyDataSetChanged();
                    if (selectedPositions.size() == 0) {
                        isSelectMode = false;
                        Activity activity = (Activity) context;
                        activity.invalidateOptionsMenu();
                    }
                }
                else {
                    isSelectMode = true;
                    selectedPositions.add(position);
                    selectedFileNames.add(row_pos.getFileName());
                    notifyDataSetChanged();
                    Activity activity = (Activity) context;
                    activity.invalidateOptionsMenu();
                }

                return true;
            }
        });

        holder.fileName.setBackgroundColor(Color.parseColor("#FAFAFA"));

        for (int i = 0; i < selectedPositions.size(); i++) {
            if (position == selectedPositions.get(i)) {
                holder.fileName.setBackgroundColor(Color.parseColor("#C0C0C0"));
            }
        }

        return convertView;
    }
}
