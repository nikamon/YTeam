package by.hilum.yteam.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import by.hilum.yteam.MessagesActivity;
import by.hilum.yteam.Models.Channel;
import by.hilum.yteam.R;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;
import by.hilum.yteam.Support.Security.NameToColor;

public class ChatListAdapter extends ArrayAdapter {
    /**
     * Activity Context
     */
    private Activity context;
    /**
     * Dialogs Array
     */
    private ArrayList<Channel> channels;

    /**
     * Simple Dialogs Adapter
     *
     * @param context  Activity
     * @param resource int
     * @param channels ArrayList<Channel>
     */
    public ChatListAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Channel> channels) {
        super(context, resource, channels);

        this.context = context;
        this.channels = channels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Checking for NULL - Optimization
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.groups_list, null);
            //Layout Views
            TextView photo = convertView.findViewById(R.id.group_photo);
            TextView label = convertView.findViewById(R.id.group_name);

            //Initialization of Views
            photo.setBackgroundColor(Color.parseColor(NameToColor.NameToColor(channels.get(position).LABEL)));
            photo.setText(channels.get(position).LABEL.charAt(0) + "");
            label.setText(channels.get(position).LABEL);

            //Click Listener
            convertView.setOnClickListener(view -> {
                AdditionalUserInfo.CURRENT_CHANNEL = channels.get(position);
                context.startActivity(new Intent(context, MessagesActivity.class));
            });
        }

        return convertView;
    }
}
