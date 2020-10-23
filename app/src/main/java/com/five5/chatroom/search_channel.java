package com.five5.chatroom;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.five5.chatroom.Adapter.ChannelAdapter;
import com.five5.chatroom.Adapter.search_adapter;
import com.five5.chatroom.Data.chnnl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search_channel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search_channel extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView mChannel;
    ArrayList<chnnl> channels= new ArrayList<>();
    DatabaseReference mref;
    search_adapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SearchView searchView;
    String email;

    public search_channel() {
        // Required empty public constructor
    }
    public search_channel(SearchView s,String email){
        this.searchView=s;
        this.email=email;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search_channel.
     */
    // TODO: Rename and change types and number of parameters
    public static search_channel newInstance(String param1, String param2) {
        search_channel fragment = new search_channel();
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
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               channels.add(new chnnl("",""));
               mAdapter.notifyDataSetChanged();
               mref= FirebaseDatabase.getInstance().getReference();
                Query query = mref.child("Channels").orderByChild("name").startAt(newText).endAt(newText+"\uf8ff");
                channels.clear();


                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        channels.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            chnnl c= snapshot1.getValue(chnnl.class);
                            Log.e("channel",c.getName());
                            channels.add(c);
                            mAdapter.notifyDataSetChanged();

                        }
                        Log.e("snap",snapshot.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search_channel, container, false);
        LinearLayoutManager manager = new LinearLayoutManager(v.getContext());
        mAdapter=new search_adapter(channels,v.getContext(),email);
        mChannel=(RecyclerView)v.findViewById(R.id.search);
        mChannel.setAdapter(mAdapter);
        mChannel.setLayoutManager(manager);
        mAdapter.notifyDataSetChanged();

        return v;
    }
}