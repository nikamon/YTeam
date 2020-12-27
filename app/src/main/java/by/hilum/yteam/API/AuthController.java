package by.hilum.yteam.API;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import by.hilum.yteam.Interface.AuthCallBack;
import by.hilum.yteam.Models.UserInfo;
import by.hilum.yteam.Support.Security.ExceptionHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthController implements Callback {
    //CallBack
    public static AuthCallBack loginCallBack = null;
    //Current Action ID
    public static int CURRENT_ACTION = 0;

    /**
     * Registration
     *
     * @param context  Activity
     * @param login    String
     * @param password String
     * @param email    String
     * @param callBack AuthCallBack
     */
    public void Reg(Activity context, String login, String password, String email, AuthCallBack callBack) {
        //Always checking for connection
        if (ConnectionHandler.GetConnectionState(context)) {
            CURRENT_ACTION = 1;
            loginCallBack = callBack;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "reg")
                    .addFormDataPart("login", login)
                    .addFormDataPart("password", password)
                    .addFormDataPart("email", email)
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.AUTH_URL, AuthController.this);
        }
    }

    /**
     * Log In Action
     *
     * @param login    String
     * @param password String
     * @param context  Activity
     * @param callback AuthCallBack
     */
    public void Auth(String login, String password, Activity context, AuthCallBack callback) {
        if (ConnectionHandler.GetConnectionState(context)) {
            CURRENT_ACTION = 1;
            loginCallBack = callback;

            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "login")
                    .addFormDataPart("login", login)
                    .addFormDataPart("password", password)
                    .build();

            SimpleRequestController.SendDataWithResult(body, SimpleRequestController.AUTH_URL, AuthController.this);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(() -> {
            loginCallBack.logResult(false);
            ExceptionHandler.ProcessLastError("error");
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();

        switch (CURRENT_ACTION) {
            case 1:
                //Результат авторизации
                runOnUiThread(() -> {
                    if (!result.contains("error")) {
                        UserInfo.ID = Integer.parseInt(result);
                        loginCallBack.logResult(true);
                    } else {
                        loginCallBack.logResult(false);
                        ExceptionHandler.ProcessLastError("200");
                    }
                });

                break;
        }
    }

    /**
     * Use UI Thread
     *
     * @param task Runnable
     */
    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
}
