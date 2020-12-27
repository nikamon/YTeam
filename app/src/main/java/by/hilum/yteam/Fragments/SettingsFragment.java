package by.hilum.yteam.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import by.hilum.yteam.API.UserInfoController;
import by.hilum.yteam.Activities.NoGroupsActivity;
import by.hilum.yteam.MainActivity;
import by.hilum.yteam.R;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        String[] settingsParams = new String[]{"Switch Group", "Join New Group", "Leave Group", "Log Out"};

        //List View initialization
        ListView settingsListView = view.findViewById(R.id.settings_listview);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, settingsParams);
        settingsListView.setAdapter(adapter);

        //Dialogs
        settingsListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            switch (i) {
                case 0:
                    //Switch Group
                    if (AdditionalUserInfo.groupArrayList.size() > 1) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "There is one group only!", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 1:
                    //Join Group
                    startActivity(new Intent(getActivity(), NoGroupsActivity.class));
                    getActivity().finish();

                    break;
                case 2:
                    //Leave Group
                    Dialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Do u want to leave channel?")
                            .setMessage("You can join later by code")
                            .setNegativeButton("No", (dialogInterface, i12) -> {
                            })
                            .setPositiveButton("Yes", (dialogInterface, i14) -> {
                                UserInfoController controller = new UserInfoController();
                                controller.LeaveGroup(getActivity());
                            })
                            .create();

                    dialog.show();

                    break;
                case 3:
                    //Logout
                    dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Do you want to logout?")
                            .setMessage("You will be redirected to the login page")
                            .setPositiveButton("Yes", (dialogInterface, i13) -> {
                                AdditionalUserInfo.ClearStorage();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            })
                            .setNegativeButton("No", (dialogInterface, i1) -> {
                            }).create();

                    dialog.show();

                    break;
            }
        });

        return view;
    }
}