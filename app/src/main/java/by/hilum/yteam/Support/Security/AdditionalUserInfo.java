package by.hilum.yteam.Support.Security;

import com.pusher.client.channel.User;

import java.util.ArrayList;

import by.hilum.yteam.Models.Channel;
import by.hilum.yteam.Models.Group;
import by.hilum.yteam.Models.Message;
import by.hilum.yteam.Models.UserInfo;

public class AdditionalUserInfo {

    //Current Opened Groups And Channels Info
    public static ArrayList<Group> groupArrayList = null;
    public static ArrayList<Channel> channelsArrayList = null;
    public static ArrayList<Message> messagesArrayList = null;

    //Current Opened Group Info
    public static Group CURRENT_GROUP = null;
    public static int CURRENT_GROUP_ID = 0;
    public static int CURRENT_GROUP_USER_MOD = 0;
    public static Channel CURRENT_CHANNEL = null;
    public static String CURRENT_USER_LABEL = "";

    /**
     * Clear All Static User Data
     */
    public static void ClearStorage() {
        //This file
        groupArrayList = null;
        channelsArrayList = null;
        messagesArrayList = null;
        CURRENT_GROUP = null;
        CURRENT_CHANNEL = null;
        CURRENT_GROUP_ID = 0;
        CURRENT_GROUP_USER_MOD = 0;
        CURRENT_USER_LABEL = "";
        //User Info
        UserInfo.ID = 0;
        UserInfo.LOGIN = "";
        UserInfo.PASSWORD = "";

    }
}
