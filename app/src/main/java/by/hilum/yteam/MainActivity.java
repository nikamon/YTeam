package by.hilum.yteam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.pusher.client.channel.User;

import java.io.File;

import by.hilum.yteam.API.AuthController;
import by.hilum.yteam.API.ConnectionHandler;
import by.hilum.yteam.API.UserInfoController;
import by.hilum.yteam.Activities.Home;
import by.hilum.yteam.Activities.NoGroupsActivity;
import by.hilum.yteam.Interface.AuthCallBack;
import by.hilum.yteam.Models.UserInfo;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;
import by.hilum.yteam.Support.Security.LocalStorageController;
import by.hilum.yteam.Support.Security.MD5;

public class MainActivity extends AppCompatActivity implements AuthCallBack {
    //Auth Buttons Animations
    ViewPropertyAnimator loginButtonAnimation = null;
    ViewPropertyAnimator regButtonAnimation = null;
    //Current API Call ID's
    public static int CURRENT_API_CALL = 0;
    public static int CURRENT_API_CALL_AUTH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If we just reload dialog page
        if ((UserInfo.PASSWORD.length() > 1 && UserInfo.LOGIN.length() > 1) || LocalStorageController.isPrefsAlive(this)) {
            //Re-auth
            if (UserInfo.LOGIN.length() > 1 && UserInfo.PASSWORD.length() > 1) {
                //If current activity has login and password
                AuthController controller = new AuthController();
                controller.Auth(UserInfo.LOGIN, UserInfo.PASSWORD, MainActivity.this, MainActivity.this);
                //Then Write Data to prefs
                LocalStorageController.SaveData(this, UserInfo.LOGIN, UserInfo.PASSWORD);
            }else{
                //If file exists and activity don't know about user
                String[] authData = LocalStorageController.GetData(this);
                if(authData != null){
                    //If there is auth data in file
                    UserInfo.LOGIN = authData[0];
                    UserInfo.PASSWORD = authData[1];

                    AuthController controller = new AuthController();
                    controller.Auth(UserInfo.LOGIN, UserInfo.PASSWORD, MainActivity.this, MainActivity.this);
                }else{
                    Toast.makeText(MainActivity.this, "Password was not store, u mast re-login", Toast.LENGTH_SHORT).show();
                    LocalStorageController.ClearStorage(this);
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            }
        } else {
            final ConstraintLayout[] currentlayout = {null};

            //Layouts
            ConstraintLayout logo = findViewById(R.id.login_logo_constaint);
            ConstraintLayout buttons = findViewById(R.id.login_buttons_constraint);
            ConstraintLayout login_layout = findViewById(R.id.login_log_in_constraint);
            ConstraintLayout reg_layout = findViewById(R.id.login_sigh_up_constraint);

            //Buttons
            Button login_button = findViewById(R.id.login_call_log_in);
            Button reg_button = findViewById(R.id.login_call_sing_up);
            Button do_reg = findViewById(R.id.login_reg_button);
            Button do_log = findViewById(R.id.login_button);
            ImageButton close_curr_tab = findViewById(R.id.closeCurrentLayout);

            //Progress Bars
            ProgressBar login_progress = findViewById(R.id.progressBar2);
            ProgressBar reg_progress = findViewById(R.id.registration_progress);

            //Init main screen
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            int height = size.y;

            //Starting Move Initialization
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                ValueAnimator anim = ValueAnimator.ofInt(buttons.getMeasuredHeight(), height / 2);
                anim.addUpdateListener(valueAnimator -> {
                    int val = (Integer) valueAnimator.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = buttons.getLayoutParams();
                    ViewGroup.LayoutParams logoParams = logo.getLayoutParams();

                    logoParams.height = height - val;
                    layoutParams.height = val;

                    buttons.setLayoutParams(layoutParams);
                    logo.setLayoutParams(logoParams);
                });
                anim.setDuration(300);
                anim.start();
            }, 2000);

            //Buttons form clicking handler
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentlayout[0] = login_layout;
                    ValueAnimator anim = ValueAnimator.ofInt(buttons.getMeasuredHeight(), 0);
                    anim.addUpdateListener(valueAnimator -> {
                        int val = (Integer) valueAnimator.getAnimatedValue();

                        ViewGroup.LayoutParams layoutParams = buttons.getLayoutParams();
                        ViewGroup.LayoutParams loginParams = currentlayout[0].getLayoutParams();

                        loginParams.height = height - val;
                        layoutParams.height = val;

                        buttons.setLayoutParams(layoutParams);
                        currentlayout[0].setLayoutParams(loginParams);
                    });
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            close_curr_tab.setAlpha((float) 1.0);
                        }
                    });
                    anim.setDuration(300);
                    anim.start();
                }
            });

            reg_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentlayout[0] = reg_layout;
                    ValueAnimator anim = ValueAnimator.ofInt(buttons.getMeasuredHeight(), 0);
                    anim.addUpdateListener(valueAnimator -> {
                        int val = (Integer) valueAnimator.getAnimatedValue();

                        ViewGroup.LayoutParams layoutParams = buttons.getLayoutParams();
                        ViewGroup.LayoutParams loginParams = currentlayout[0].getLayoutParams();

                        loginParams.height = height - val;
                        layoutParams.height = val;

                        buttons.setLayoutParams(layoutParams);
                        currentlayout[0].setLayoutParams(loginParams);
                    });
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            close_curr_tab.setAlpha((float) 1.0);
                        }
                    });
                    anim.setDuration(300);
                    anim.start();
                }
            });

            close_curr_tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ValueAnimator anim = ValueAnimator.ofInt(0, height / 2);
                    anim.addUpdateListener(valueAnimator -> {
                        int val = (Integer) valueAnimator.getAnimatedValue();

                        ViewGroup.LayoutParams layoutParams = buttons.getLayoutParams();
                        ViewGroup.LayoutParams currLayoutParamrs = currentlayout[0].getLayoutParams();

                        layoutParams.height = val;
                        currLayoutParamrs.height = height / 2 - val;

                        currentlayout[0].setLayoutParams(currLayoutParamrs);
                        buttons.setLayoutParams(layoutParams);
                    });

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);

                            close_curr_tab.setAlpha((float) 0);
                        }
                    });
                    anim.setDuration(300);
                    anim.start();
                }
            });

            //Action buttons implementation
            do_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Registration Fields
                    TextInputEditText login_et = findViewById(R.id.login_reg_field_et);
                    TextInputEditText password_et = findViewById(R.id.password_reg_field_et);
                    TextInputEditText email_et = findViewById(R.id.email_reg_field_et);

                    //Registration Values
                    String loginVal = login_et.getText().toString();
                    String passwordVal = password_et.getText().toString();
                    String emailVal = email_et.getText().toString();

                    //If is not empty
                    if (loginVal.isEmpty() || passwordVal.isEmpty() || emailVal.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        regButtonAnimation = do_reg.animate().alpha(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                reg_progress.animate().alpha(1).setDuration(800).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                }).start();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                do_reg.setVisibility(View.GONE);
                            }
                        });

                        regButtonAnimation.start();

                        //Processing
                        passwordVal = MD5.ProcessString(passwordVal);

                        UserInfo.LOGIN = loginVal;
                        UserInfo.PASSWORD = passwordVal;

                        CURRENT_API_CALL_AUTH = 2;

                        AuthController controller = new AuthController();
                        controller.Reg(MainActivity.this, loginVal, passwordVal, emailVal, MainActivity.this);
                    }
                }
            });

            do_log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextInputEditText login_et = findViewById(R.id.login_field_et);
                    TextInputEditText password_et = findViewById(R.id.password_field_et);

                    String loginVal = login_et.getText().toString();
                    String passwordVal = password_et.getText().toString();

                    if (loginVal.isEmpty() || passwordVal.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        loginButtonAnimation = do_log.animate().alpha(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                login_progress.animate().alpha(1).setDuration(800).setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                }).start();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                do_log.setVisibility(View.GONE);
                            }
                        });

                        loginButtonAnimation.start();

                        //Processing
                        passwordVal = MD5.ProcessString(passwordVal);
                        CURRENT_API_CALL_AUTH = 1;

                        UserInfo.LOGIN = loginVal;
                        UserInfo.PASSWORD = passwordVal;

                        AuthController controller = new AuthController();
                        controller.Auth(loginVal, passwordVal, MainActivity.this, MainActivity.this);
                    }
                }
            });

            //Check internet state... While not Курсовой, we don't need to store local storage
            if (!ConnectionHandler.GetConnectionState(this)) {

                AlertDialog _dialog = null;
                AlertDialog final_dialog = _dialog;

                _dialog = new AlertDialog.Builder(this)
                        .setTitle("No connection")
                        .setMessage("This application still need an internet connection for work.\nTurn it on, and try again!")
                        .setCancelable(false)
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (ConnectionHandler.GetConnectionState(MainActivity.this)) {
                                    if (final_dialog != null)
                                        final_dialog.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "We still don't have connection", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).create();

                _dialog.show();
            }
        }
    }

    @Override
    public void logResult(boolean result) {
        if (result) {
            switch (CURRENT_API_CALL) {
                case 0:
                    //Запрашиваем инфу про группы
                    LocalStorageController.SaveData(this, UserInfo.LOGIN, UserInfo.PASSWORD);

                    UserInfoController controller = new UserInfoController();
                    controller.GetGroups(this, this);
                    CURRENT_API_CALL = 1;

                    break;
                case 1:
                    //Ok, we have groups/no groups
                    //If groups count = 0 - Go to NoGroupsActivity
                    //If groups count = 1 - Go to Chats
                    //If groups count > 1 - Open Groups Selection Dialog

                    switch (AdditionalUserInfo.groupArrayList.size()) {
                        case 0:
                            CURRENT_API_CALL = 0;
                            startActivity(new Intent(MainActivity.this, NoGroupsActivity.class));
                            finish();

                            break;
                        case 1:
                            CURRENT_API_CALL = 2;
                            AdditionalUserInfo.CURRENT_GROUP_ID = AdditionalUserInfo.groupArrayList.get(0).ID;
                            AdditionalUserInfo.CURRENT_GROUP = AdditionalUserInfo.groupArrayList.get(0);
                            controller = new UserInfoController();
                            controller.GetChannels(MainActivity.this, this);

                            break;
                        default:
                            String[] names = new String[AdditionalUserInfo.groupArrayList.size()];

                            for (int i = 0; i < AdditionalUserInfo.groupArrayList.size(); i++) {
                                names[i] = AdditionalUserInfo.groupArrayList.get(i).NAME;
                            }

                            Dialog dialog = new AlertDialog.Builder(this)
                                    .setTitle("Choose Group to Join")
                                    .setCancelable(false)
                                    .setItems(names, (dialogInterface, i) -> {
                                        AdditionalUserInfo.CURRENT_GROUP = AdditionalUserInfo.groupArrayList.get(i);
                                        AdditionalUserInfo.CURRENT_GROUP_ID = AdditionalUserInfo.groupArrayList.get(i).ID;

                                        UserInfoController controller1 = new UserInfoController();
                                        controller1.GetChannels(MainActivity.this, MainActivity.this);

                                        CURRENT_API_CALL = 2;
                                    }).create();
                            dialog.show();

                            break;
                    }

                    break;
                case 2:
                    CURRENT_API_CALL = 0;
                    startActivity(new Intent(MainActivity.this, Home.class));
                    finish();

                    break;
            }
        } else {
            switch (CURRENT_API_CALL_AUTH) {
                case 1:
                    UserInfo.PASSWORD = "";
                    UserInfo.LOGIN = "";

                    Button do_log = findViewById(R.id.login_button);
                    ProgressBar login_progress = findViewById(R.id.progressBar2);

                    do_log.animate().alpha(1).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);

                            login_progress.animate().alpha(0).setDuration(300).start();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            do_log.setVisibility(View.VISIBLE);

                            super.onAnimationEnd(animation);
                        }
                    }).start();

                    Toast.makeText(this, "Invalid Login/Password", Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    UserInfo.PASSWORD = "";
                    UserInfo.LOGIN = "";

                    Button do_reg = findViewById(R.id.login_reg_button);
                    ProgressBar reg_progress = findViewById(R.id.registration_progress);

                    do_reg.animate().alpha(1).setDuration(300).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);

                            reg_progress.animate().alpha(0).setDuration(300).start();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            do_reg.setVisibility(View.VISIBLE);

                            super.onAnimationEnd(animation);
                        }
                    }).start();

                    Toast.makeText(this, "You can't register with the same email!", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }
}