package by.hilum.yteam.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.LayoutMode;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.bottomsheets.BottomSheet;

import by.hilum.yteam.API.UserInfoController;
import by.hilum.yteam.Interface.AuthCallBack;
import by.hilum.yteam.R;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;

public class NoGroupsActivity extends AppCompatActivity implements AuthCallBack {
    //Current Action ID
    private int CURRENT_ACTION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_groups);

        //Buttons Initialization
        Button createGroup = findViewById(R.id.noGroup_create);
        Button joinGroup = findViewById(R.id.noGroup_join);

        //Click Listeners
        createGroup.setOnClickListener(view -> {
            //Create Group Dialog
            View dialog_view = getLayoutInflater().inflate(R.layout.nogroups_dialog_create, null);
            Button cancel_dialog = dialog_view.findViewById(R.id.cancel_create);
            Button create = dialog_view.findViewById(R.id.create_group);

            //Create Dialog Main Fields
            EditText GroupName = dialog_view.findViewById(R.id.groupName);
            EditText ChannelName = dialog_view.findViewById(R.id.groupMainChannelName);

            MaterialDialog dialog = new MaterialDialog(NoGroupsActivity.this, new BottomSheet(LayoutMode.WRAP_CONTENT));
            dialog.setContentView(dialog_view);

            cancel_dialog.setOnClickListener(view1 -> {
                dialog.dismiss();
                dialog.cancel();
            });
            create.setOnClickListener(view12 -> {
                //Getting Values
                String g_name = GroupName.getText().toString().trim();
                String c_name = ChannelName.getText().toString().trim();

                //If not empty
                if (g_name.length() > 0 && c_name.length() > 0) {
                    CURRENT_ACTION = 2;
                    //Calling Controller to Create Group
                    UserInfoController controller = new UserInfoController();
                    controller.CreateGroup(NoGroupsActivity.this, NoGroupsActivity.this, g_name, c_name, "");
                } else {
                    Toast.makeText(NoGroupsActivity.this, "Enter Valid Names", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        });

        joinGroup.setOnClickListener(view -> {
            //Join Group Dialog
            View dialog_view = getLayoutInflater().inflate(R.layout.nogroups_dialog_join, null);

            //Join Group Buttons
            Button cancel_dialog = dialog_view.findViewById(R.id.cancel_join);
            Button join = dialog_view.findViewById(R.id.join_group);

            //Code Field
            EditText codeET = dialog_view.findViewById(R.id.joinGroupCode);

            MaterialDialog dialog = new MaterialDialog(NoGroupsActivity.this, new BottomSheet(LayoutMode.WRAP_CONTENT));
            dialog.setContentView(dialog_view);

            join.setOnClickListener(view13 -> {
                //Joining
                String code = codeET.getText().toString().trim();
                if (code.length() > 0) {
                    CURRENT_ACTION = 1;
                    //Calling Controller to Join
                    UserInfoController controller = new UserInfoController();
                    controller.JoinGroup(NoGroupsActivity.this, NoGroupsActivity.this, code);
                } else {
                    Toast.makeText(NoGroupsActivity.this, "Please, Enter Valid Group Code", Toast.LENGTH_SHORT).show();
                }
            });
            cancel_dialog.setOnClickListener(view1 -> {
                dialog.dismiss();
                dialog.cancel();
            });

            dialog.show();
        });
    }

    @Override
    public void logResult(boolean result) {
        switch (CURRENT_ACTION) {
            case 1:
                if (result && AdditionalUserInfo.groupArrayList != null && AdditionalUserInfo.channelsArrayList != null && AdditionalUserInfo.groupArrayList.size() > 0 && AdditionalUserInfo.channelsArrayList.size() > 0) {
                    AdditionalUserInfo.CURRENT_GROUP_ID = AdditionalUserInfo.groupArrayList.get(0).ID;
                    AdditionalUserInfo.CURRENT_GROUP = AdditionalUserInfo.groupArrayList.get(0);

                    CURRENT_ACTION = 0;
                    startActivity(new Intent(this, Home.class));

                    finish();
                } else if (result) {
                    CURRENT_ACTION = 2;
                    logResult(true);
                } else {
                    Toast.makeText(NoGroupsActivity.this, "Cannot join group", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (result && AdditionalUserInfo.groupArrayList.size() > 0) {
                    AdditionalUserInfo.CURRENT_GROUP_ID = AdditionalUserInfo.groupArrayList.get(0).ID;
                    AdditionalUserInfo.CURRENT_GROUP = AdditionalUserInfo.groupArrayList.get(0);

                    CURRENT_ACTION = 1;

                    UserInfoController controller = new UserInfoController();
                    controller.GetChannels(NoGroupsActivity.this, NoGroupsActivity.this);
                } else {
                    Toast.makeText(NoGroupsActivity.this, "Cannot join group", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                break;
        }
    }
}