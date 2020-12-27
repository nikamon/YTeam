package by.hilum.yteam.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import by.hilum.yteam.API.UserInfoController;
import by.hilum.yteam.Fragments.ChatsFragment;
import by.hilum.yteam.Fragments.SettingsFragment;
import by.hilum.yteam.Models.UserInfo;
import by.hilum.yteam.R;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;

public class Home extends AppCompatActivity {
    //Current Opened Fragment
    int current_fragment = R.id.fragment_container_view;
    //Current BottomBar
    private ExpandableBottomBar bar;
    //Current WebSocket Channel
    private Channel channel;
    //Current Pusher Instance
    private Pusher pusher;
    //Notification ID
    private static final int NOTIFY_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Init views
        bar = findViewById(R.id.expandable_bottom_bar);
        bar.setOnItemSelectedListener((view, item) -> {
            switch (item.getItemId()) {

                case R.id.home:
                    //If current fragment is not a home fragment
                    if (current_fragment != R.layout.activity_chat_list) {
                        current_fragment = R.layout.activity_chat_list;
                        //Do transaction
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                                .replace(R.id.fragment_container_view, new ChatsFragment())
                                .commit();
                    }

                    break;
                case R.id.setting:
                    //If current fragment is not a settings fragment
                    if (current_fragment != R.layout.fragment_settings) {
                        current_fragment = R.layout.fragment_settings;
                        //Do transaction
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                                .replace(R.id.fragment_container_view, new SettingsFragment())
                                .commit();
                    }

                    break;
                case R.id.params:
                    //If current user is an admin of current group
                    if (UserInfo.ID == AdditionalUserInfo.CURRENT_GROUP.OWNER) {
                        String[] contextMenu = {"Create Channel", "Invite User", "Remove Group", "Close"};

                        Dialog dialog = new AlertDialog.Builder(Home.this)
                                .setItems(contextMenu, (dialogInterface, i) -> {
                                    switch (i) {
                                        case 0:
                                            //Show dialog with text field to create new channel
                                            final EditText input = new EditText(Home.this);
                                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT);
                                            input.setLayoutParams(lp);

                                            Dialog subDialog = new AlertDialog.Builder(Home.this).setTitle("New Channel")
                                                    .setView(input).setPositiveButton("Add", (dialogInterface14, i14) -> {
                                                        if (input.getText().toString().trim().length() > 0) {
                                                            UserInfoController controller = new UserInfoController();
                                                            controller.CreateChannel(Home.this, input.getText().toString().trim());
                                                        } else {
                                                            Toast.makeText(Home.this, "Write name of the channel", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", (dialogInterface13, i13) -> {
                                                    }).create();

                                            subDialog.show();

                                            break;
                                        case 1:
                                            //Show dialog with invite code
                                            subDialog = new AlertDialog.Builder(Home.this).setTitle("Invitation")
                                                    .setMessage("Use this code to join: " + AdditionalUserInfo.CURRENT_GROUP.CODE)
                                                    .setPositiveButton("OK", (dialogInterface1, i1) -> {

                                                    }).create();

                                            subDialog.show();

                                            break;
                                        case 2:
                                            //Show dialog to remove group
                                            subDialog = new AlertDialog.Builder(Home.this).setTitle("Remove Group")
                                                    .setMessage("Do you want to remove group '" + AdditionalUserInfo.CURRENT_GROUP.NAME + "'?\n" +
                                                            " You cannot reverse this action!")
                                                    .setPositiveButton("Yes", (dialogInterface15, i15) -> {
                                                        UserInfoController controller = new UserInfoController();
                                                        controller.RemoveGroup(Home.this);
                                                    })
                                                    .setNegativeButton("No", (dialogInterface12, i12) -> {
                                                    }).create();

                                            subDialog.show();

                                            break;
                                        default:
                                            //Simple Close Handler
                                            break;
                                    }
                                }).create();

                        dialog.show();
                    }
                    break;
            }

            return null;
        });

        //Pusher Initialization
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        //Ow, it's my key! Don't take it!
        pusher = new Pusher("0fb0285a5a8c5dc48b93", options);
        createNotificationChannel();
    }

    @Override
    protected void onResume() {
        //Initialization of WebSocket
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        channel = pusher.subscribe("my-channel");
        channel.bind("my-event", event -> {
            //Parse Name and Message, also send channel id to avoid send to other users
            String message = event.getData();

            try {
                JSONObject obj = new JSONObject(message);
                int channel_id = Integer.parseInt(obj.getString("channel_id"));

                for (int i = 0; i < AdditionalUserInfo.channelsArrayList.size(); i++) {
                    if (AdditionalUserInfo.channelsArrayList.get(i).ID == channel_id) {

                        String text = obj.getString("text");
                        String channel_name = AdditionalUserInfo.channelsArrayList.get(i).LABEL;

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this, "MESSAGE_CHANNEL")
                                .setSmallIcon(R.drawable.logo)
                                .setContentTitle(channel_name)
                                .setContentText(text)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager =
                                NotificationManagerCompat.from(Home.this);
                        notificationManager.notify(NOTIFY_ID, builder.build());

                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        super.onResume();
    }

    @Override
    protected void onPause() {
        //Unsubscribe the message listener
        pusher.unsubscribe("my-channel");

        super.onPause();
    }

    /**
     * Creation of Notification Channel
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "YTeam Notification";
            String description = "Chat Notifications";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MESSAGE_CHANNEL", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}