package by.hilum.yteam.API;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import by.hilum.yteam.Models.Author;
import by.hilum.yteam.Models.Message;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageController implements Callback {
    //Activity Context
    private Activity context;
    //Callback road
    private Callback callback;
    //Current Action
    private int CURRENT_CALL = 0;

    /**
     * Send Message to Server
     *
     * @param context Activity
     * @param message String
     */
    public void SendMessage(Activity context, String message) {
        this.context = context;

        if (ConnectionHandler.GetConnectionState(context)) {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "send")
                    .addFormDataPart("channel_id", AdditionalUserInfo.CURRENT_CHANNEL.ID + "")
                    .addFormDataPart("json_message", message)
                    .build();

            SimpleRequestController.SendDataWithoutReply(body, SimpleRequestController.IM_URL, MessageController.this);
        }
    }

    /**
     * Get Array of Current Messages
     *
     * @param context  Activity
     * @param callback Callback
     */
    public void GetMessages(Activity context, Callback callback) {
        this.context = context;
        this.callback = callback;

        if (ConnectionHandler.GetConnectionState(context)) {
            CURRENT_CALL = 1;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "get_messages")
                    .addFormDataPart("channel_id", AdditionalUserInfo.CURRENT_CHANNEL.ID + "")
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.IM_URL, MessageController.this);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Toast.makeText(context, "Error while sending", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (CURRENT_CALL == 1) {
            String result = response.body().string();

            //Parsing Messages To Object and Array
            try {
                JSONArray jsonarray = new JSONArray(result);
                AdditionalUserInfo.messagesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String uid = jsonobject.getString("uid");
                    String name = jsonobject.getString("name");

                    String id = jsonobject.getString("id");
                    String text = jsonobject.getString("text");
                    String date = jsonobject.getString("date");

                    Author author = new Author(uid, name, null);
                    Message message = new Message(id, text, date, author);

                    AdditionalUserInfo.messagesArrayList.add(message);
                }

                callback.onResponse(null, null);

            } catch (JSONException ignored) {

            }
        }
    }
}
