package by.hilum.yteam;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

import by.hilum.yteam.API.MessageController;
import by.hilum.yteam.Models.Author;
import by.hilum.yteam.Models.Message;
import by.hilum.yteam.Models.UserInfo;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MessagesActivity extends AppCompatActivity implements Callback {
    //Messages List Adapter
    MessagesListAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        //Setting Up Toolbar Title
        getSupportActionBar().setTitle(AdditionalUserInfo.CURRENT_CHANNEL.LABEL);

        //Me as an author
        Author me = new Author(UserInfo.ID + "", UserInfo.LOGIN, null);

        //Message ListView Initialization
        MessagesList messagesList = findViewById(R.id.messagesList);
        adapter = new MessagesListAdapter<>(UserInfo.ID + "", null);
        messagesList.setAdapter(adapter);

        //Send button and field
        ImageView send = findViewById(R.id.sendMessage);
        EditText messageBox = findViewById(R.id.messageform);

        //Initialization of WebSocket
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");

        Pusher pusher = new Pusher("0fb0285a5a8c5dc48b93", options);
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
        Channel channel = pusher.subscribe("my-channel");
        channel.bind("my-event", event -> {
            //Parse Name and Message, also send channel id to avoid send to other users
            String message = event.getData();
            try {
                JSONObject obj = new JSONObject(message);
                //Если сообщение к этому каналу
                if (Integer.parseInt(obj.getString("channel_id")) == AdditionalUserInfo.CURRENT_CHANNEL.ID) {
                    JSONObject author = obj.getJSONObject("author");

                    Author message_author = new Author(author.getString("id"), author.getString("name"), null);
                    Message new_message = new Message(obj.getString("id"), obj.getString("text"), obj.getString("createdAt"), message_author);

                    runOnUiThread(() -> adapter.addToStart(new_message, true));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        //Getting Current Channel Messages
        MessageController controller = new MessageController();
        controller.GetMessages(this, this);

        //Send Message Action
        send.setOnClickListener(view -> {
            String message = messageBox.getText().toString().trim();

            if (message.length() > 0) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Message newMessage = new Message("0", message, timestamp + "", me);

                Gson gson = new Gson();
                String json = gson.toJson(newMessage);
                controller.SendMessage(MessagesActivity.this, json);

                messageBox.setText("");
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
    }

    @Override
    public void onResponse(Call call, Response response) {
        runOnUiThread(() -> adapter.addToEnd(AdditionalUserInfo.messagesArrayList, true));
    }
}