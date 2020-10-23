package com.five5.chatroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.five5.chatroom.Data.SubChannel;
import com.five5.chatroom.Data.chnnl;
import com.five5.chatroom.Data.user;
import com.five5.chatroom.Output_Chatroom;
import com.five5.chatroom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.channelViewHolder> {
    ArrayList<chnnl> channels;
    int layout=R.layout.channel_layout;
    Context c;
    DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
    String email;
    SearchView searchView;

    public ChannelAdapter(ArrayList<chnnl> channels, Context c, String s) {
        this.channels = channels;
        this.c=c;
        this.email=s;

    }

    public ChannelAdapter() {
    }


    @NonNull
    @Override
    public channelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_layout,parent,false);

        return new channelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final channelViewHolder holder, int position) {
        final chnnl curr= channels.get(position);
        holder.name.setText(curr.getName());
        holder.lstMssg.setText(curr.getLstMssg());
        if(layout==R.layout.channel_layout)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, Output_Chatroom.class);
                i.putExtra("Name",curr.getName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteSubChannel(curr.getName());

                    Toast.makeText(c,"Deleted",Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void deleteSubChannel(final String name) {
        Query query= mRef.child("Users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("deletelkeyuser",snapshot.toString());
                for (DataSnapshot s:snapshot.getChildren()){
                    user use= s.getValue(user.class);
                    if(use.getChnls().size()>1)
                    deleteChannel(s.getKey(),name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteChannel(final String key, final String name) {

        Query q=mRef.child("Users").child(key).child("chnls").orderByChild("name").equalTo(name);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d:snapshot.getChildren()){
                    mRef.child("Users").child(key).child("chnls").child(d.getKey()).setValue(null);
                    FirebaseMessaging.getInstance().getToken();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return channels.size();
    }

    public class channelViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView lstMssg;
        ImageButton delete;


        public channelViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.textView2);
            lstMssg=(TextView)itemView.findViewById(R.id.textView3);
            delete=(ImageButton)itemView.findViewById(R.id.imageView4);

        }
    }
}
