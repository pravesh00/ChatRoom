package com.five5.chatroom.Adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.channelViewHolder> {
    ArrayList<chnnl> channels;
    int layout=R.layout.channel_layout;
    Context c;
    DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
    String email;
    SearchView searchView;

    public ChannelAdapter(ArrayList<chnnl> channels, Context c, String s, SearchView searchView) {
        this.channels = channels;
        this.c=c;
        this.email=s;
        this.searchView=searchView;
    }

    public ChannelAdapter() {
    }


    @NonNull
    @Override
    public channelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType==1){

            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.search_channel,parent,false);  }
        else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_layout,parent,false);
        }
        return new channelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final channelViewHolder holder, int position) {
        final chnnl curr= channels.get(position);
        holder.name.setText(curr.getName());
        holder.lstMssg.setText(curr.getLstMssg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, Output_Chatroom.class);
                i.putExtra("User", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                i.putExtra("Name",curr.getName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout==R.layout.channel_layout){
                    Toast.makeText(c,"Deleted",Toast.LENGTH_SHORT).show();
                    delete();
                }else{
                    Toast.makeText(c,"Subscribed",Toast.LENGTH_SHORT).show();
                    subscribe(curr);


                }
            }
        });

    }

    private void subscribe(final chnnl u) {

        Query q=mRef.child("Users").orderByChild("email").equalTo(email);
        Log.e("ref",mRef.child("Users").orderByChild("email").equalTo(email).getRef().toString());
        String key;
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot s:snapshot.getChildren()){
                        user c=s.getValue(user.class);
                        addSub(s.getKey(),c,u.getName());
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addSub(String key, user c,String n) {
        SubChannel a= new SubChannel(n);
        c.getChnls().add(a);
        mRef.child("Users").child(key).setValue(c);
    }

    private void delete() {
    }

    public void changeLayout(int i){
        if(i==1)
        layout=R.layout.search_channel;
        else
            layout=R.layout.channel_layout;
    }

    @Override
    public int getItemViewType(int position) {

        if(layout==R.layout.search_channel)
            return 1;
        else
            return 0;
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
