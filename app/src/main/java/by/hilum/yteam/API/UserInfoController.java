package by.hilum.yteam.API;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import by.hilum.yteam.Interface.AuthCallBack;
import by.hilum.yteam.MainActivity;
import by.hilum.yteam.Models.Channel;
import by.hilum.yteam.Models.Group;
import by.hilum.yteam.Models.UserInfo;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoController implements Callback {
    //Current API Call
    private static int CURRENT_CALL = 0;
    //Current CallBack
    private static AuthCallBack authCallBack = null;
    //Activity Context
    private static Activity context;

    /**
     * Get User Groups
     *
     * @param context  Activity
     * @param callback AuthCallBack
     */
    public void GetGroups(Activity context, AuthCallBack callback) {
        CURRENT_CALL = 0;

        if (ConnectionHandler.GetConnectionState(context)) {
            authCallBack = callback;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "get_groups")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);
        }
    }

    /**
     * Join To Group
     *
     * @param context  Activity
     * @param callBack AuthCallBack
     * @param code     String
     */
    public void JoinGroup(Activity context, AuthCallBack callBack, String code) {
        CURRENT_CALL = 0;

        if (ConnectionHandler.GetConnectionState(context)) {
            authCallBack = callBack;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "join_group")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .addFormDataPart("code", code)
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);
        }
    }

    /**
     * Create New Group
     *
     * @param context      Activity
     * @param callBack     AuthCallBack
     * @param name         String
     * @param channel_name String
     * @param category     String
     */
    public void CreateGroup(Activity context, AuthCallBack callBack, String name, String channel_name, String category) {
        CURRENT_CALL = 0;

        if (ConnectionHandler.GetConnectionState(context)) {
            authCallBack = callBack;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "create_group")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .addFormDataPart("group_name", name)
                    .addFormDataPart("channel_name", channel_name)
                    .addFormDataPart("category", category)
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);
        }
    }

    /**
     * Leave Current Group
     *
     * @param context Activity
     */
    public void LeaveGroup(Activity context) {
        CURRENT_CALL = 2;
        UserInfoController.context = context;

        if (ConnectionHandler.GetConnectionState(context)) {

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "leave_group")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .addFormDataPart("group_id", AdditionalUserInfo.CURRENT_GROUP_ID + "")
                    .build();

            SimpleRequestController.SendDataWithoutReply(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);

            //Removing From Current Array
            for (int i = 0; i < AdditionalUserInfo.groupArrayList.size(); i++) {
                if (AdditionalUserInfo.groupArrayList.get(i).ID == AdditionalUserInfo.CURRENT_GROUP_ID) {
                    AdditionalUserInfo.groupArrayList.remove(i);
                    AdditionalUserInfo.CURRENT_GROUP_ID = 0;

                    if (AdditionalUserInfo.groupArrayList.size() == 0)
                        AdditionalUserInfo.groupArrayList = null;

                    break;
                }
            }
        }
    }

    /**
     * Create New Channel in current Group
     *
     * @param context      Activity
     * @param channel_name String
     */
    public void CreateChannel(Activity context, String channel_name) {
        CURRENT_CALL = 2;
        UserInfoController.context = context;

        if (ConnectionHandler.GetConnectionState(context)) {

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "create_channel")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .addFormDataPart("group_id", AdditionalUserInfo.CURRENT_GROUP_ID + "")
                    .addFormDataPart("channel_name", channel_name)
                    .build();

            SimpleRequestController.SendDataWithoutReply(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);
        }
    }

    /**
     * Remove Current Group
     *
     * @param context Activity
     */
    public void RemoveGroup(Activity context) {
        CURRENT_CALL = 2;
        UserInfoController.context = context;

        if (ConnectionHandler.GetConnectionState(context)) {

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "remove_group")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .addFormDataPart("group_id", AdditionalUserInfo.CURRENT_GROUP_ID + "")
                    .build();

            SimpleRequestController.SendDataWithoutReply(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);

            for (int i = 0; i < AdditionalUserInfo.groupArrayList.size(); i++) {
                if (AdditionalUserInfo.groupArrayList.get(i).ID == AdditionalUserInfo.CURRENT_GROUP_ID) {
                    AdditionalUserInfo.groupArrayList.remove(i);
                    AdditionalUserInfo.CURRENT_GROUP_ID = 0;

                    if (AdditionalUserInfo.groupArrayList.size() == 0)
                        AdditionalUserInfo.groupArrayList = null;

                    break;
                }
            }
        }
    }

    /**
     * Get All Channels
     *
     * @param context  Activity
     * @param callBack AuthCallBack
     */
    public void GetChannels(Activity context, AuthCallBack callBack) {
        CURRENT_CALL = 1;

        if (ConnectionHandler.GetConnectionState(context)) {
            authCallBack = callBack;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "get_channels")
                    .addFormDataPart("login", UserInfo.LOGIN)
                    .addFormDataPart("password", UserInfo.PASSWORD)
                    .addFormDataPart("group_id", "" + AdditionalUserInfo.CURRENT_GROUP_ID)
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.USER_INFO_URL, UserInfoController.this);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e("Error: ", e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();

        switch (CURRENT_CALL) {
            case 0:
                AdditionalUserInfo.groupArrayList = new ArrayList<Group>();

                //Parse JSON String
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        String name = jsonobject.getString("name");
                        String id = jsonobject.getString("id");
                        String owner = jsonobject.getString("owner");
                        String code = jsonobject.getString("code");

                        Group group = new Group(name, id, owner, code);

                        AdditionalUserInfo.groupArrayList.add(group);
                    }

                    runOnUiThread(() -> authCallBack.logResult(true));
                } catch (JSONException e) {
                    runOnUiThread(() -> {
                        e.printStackTrace();
                        authCallBack.logResult(false);
                    });
                }
                CURRENT_CALL = 1;

                break;
            case 1:
                AdditionalUserInfo.channelsArrayList = new ArrayList<Channel>();
                //Parse JSON String

                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        String label = jsonobject.getString("label");
                        String id = jsonobject.getString("id");
                        String owner = jsonobject.getString("owner_id");
                        String min_mod_id = jsonobject.getString("min_mod_id");

                        Channel channel = new Channel(id, label, owner, min_mod_id);

                        AdditionalUserInfo.channelsArrayList.add(channel);
                    }

                    runOnUiThread(() -> authCallBack.logResult(true));

                } catch (JSONException e) {
                    runOnUiThread(() -> {
                        e.printStackTrace();
                        authCallBack.logResult(false);
                    });
                }

                CURRENT_CALL = 0;

                break;
            case 2:
                context.startActivity(new Intent(context, MainActivity.class));
                CURRENT_CALL = 0;

                break;
        }
    }

    /**
     * Run Task on UI Thread
     *
     * @param task Runnable
     */
    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }

}
