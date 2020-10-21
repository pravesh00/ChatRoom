package com.five5.chatroom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.five5.chatroom.Data.chnnl;
import com.five5.chatroom.R;

import java.util.ArrayList;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.channelViewHolder> {
    ArrayList<chnnl> channels;

    public searchAdapter(ArrayList<chnnl> channels) {
        this.channels = channels;
    }

    public searchAdapter() {
    }

    @NonNull
    @Override
    public channelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_channel,parent,false);
        return new channelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull channelViewHolder holder, int position) {
        chnnl curr= channels.get(position);
        holder.name.setText(curr.getName());
        holder.lstMssg.setText(curr.getLstMssg());

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
