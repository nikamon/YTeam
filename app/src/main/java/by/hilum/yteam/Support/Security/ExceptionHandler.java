package by.hilum.yteam.Support.Security;

import android.util.Log;

import java.util.HashMap;

public class ExceptionHandler {
    /**
     * HashMap of Known Error Codes
     */
    public static HashMap<Integer, String> CodesMap = new HashMap<Integer, String>() {{
        //Main Error Codes
        put(100, "Internal Server Error");
        //Auth Error Codes
        put(200, "Invalid Login/Password");
        put(201, "Auth Failed");
        //Reg Error Codes
        put(300, "User already exists");
        //Groups Error codes
        put(400, "Group You Requested Not Found");
        put(401, "Invalid Code");
        put(402, "Not Enough Permissions");
        put(403, "No Channels Found");
    }};

    /**
     * Last Error Message String
     */
    public static String LAST_ERROR_MESSAGE = "";

    /**
     * Print error by Code
     *
     * @param code String
     */
    public static void ProcessLastError(String code) {
        LAST_ERROR_MESSAGE = code;
        Log.e("Exception Handler", code);
    }
}
