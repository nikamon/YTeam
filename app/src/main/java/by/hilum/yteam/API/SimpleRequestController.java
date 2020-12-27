package by.hilum.yteam.API;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SimpleRequestController {
    //Static URL's of Server Actions
    public static final String AUTH_URL = "http://u203395axc.ha005.t.justns.ru/auth.php";
    public static final String USER_INFO_URL = "http://u203395axc.ha005.t.justns.ru/userinfo.php";
    public static final String IM_URL = "http://u203395axc.ha005.t.justns.ru/im.php";

    /**
     * Main HTTP Client
     */
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Sending Data to Server Without Require Reply
     * @param data RequestBody
     * @param url String
     * @param callback Callback
     */
    public static void SendDataWithoutReply(RequestBody data, String url, Callback callback) {
        RequestBody requestBody = data;

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);

    }

    /**
     * Sending Data to Server With Require Reply
     * @param data RequestBody
     * @param url String
     * @param callback Callback
     */
    public static void SendDataWithResult(RequestBody data, String url, Callback callback) {
        RequestBody requestBody = data;

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
