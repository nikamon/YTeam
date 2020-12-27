package by.hilum.yteam.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import by.hilum.yteam.Adapters.ChatListAdapter;
import by.hilum.yteam.R;
import by.hilum.yteam.Support.Security.AdditionalUserInfo;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
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
        View view = inflater.inflate(R.layout.activity_chat_list, container, false);
        ListView channelsList = view.findViewById(R.id.chat_listview);

        // Displaying channels
        ChatListAdapter adapter = new ChatListAdapter(getActivity(), R.layout.groups_list, AdditionalUserInfo.channelsArrayList);
        channelsList.setAdapter(adapter);

        return view;
    }
}