package com.five5.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.five5.chatroom.Adapter.ChannelAdapter;
import com.five5.chatroom.Data.SubChannel;
import com.five5.chatroom.Data.chnnl;
import com.five5.chatroom.Data.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link channel_new#newInstance} factory method to
 * create an instance of this fragment.
 */
public class channel_new extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mChannel;
    ArrayList<chnnl> channels= new ArrayList<>();
    DatabaseReference mref;
    ChannelAdapter mAdapter;
    String email;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public channel_new() {
        // Required empty public constructor
    }
    public channel_new(String email){
        this.email=email;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment channel_new.
     */
    // TODO: Rename and change types and number of parameters
    public static channel_new newInstance(String param1, String param2) {
        channel_new fragment = new channel_new();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mref = FirebaseDatabase.getInstance().getReference();

        channels.clear();
        Query q=mref.child("Users").orderByChild("email").equalTo(email);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Chnls",snapshot.toString());
                for(DataSnapshot m: snapshot.getChildren()){
                    user u = m.getValue(user.class);
                    Log.e("mail",u.getEmail());
                    ArrayList<SubChannel> x= u.getChnls();
                    for(SubChannel c:x){
                        checkChannelandAdd(c.getName());
                        Log.e("name",c.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_channel_new, container, false);
        LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
        mAdapter=new ChannelAdapter(channels,v.getContext(),"");
        mChannel=(RecyclerView)v.findViewById(R.id.channelRecycle);
        mChannel.setAdapter(mAdapter);
        mChannel.setLayoutManager(manager);
        //channels.add(new chnnl("",""));
        mAdapter.notifyDataSetChanged();
        mAdapter.changeLayout(0);

        return v;
    }
    private void checkChannelandAdd(String n) {
        Query query = mref.child("Channels").orderByChild("name").equalTo(n);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    chnnl c= snapshot1.getValue(chnnl.class);
                    Log.e("channel",c.getName());
                    channels.add(c);
                    mAdapter.notifyDataSetChanged();
                    Log.e("chnnnnls",channels.size()+"");
                }
                Log.e("snap",snapshot.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}