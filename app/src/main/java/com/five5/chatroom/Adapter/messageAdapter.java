package com.five5.chatroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.five5.chatroom.Data.message;
import com.five5.chatroom.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.messageViewHolder> {
    ArrayList<message> messages;
    String sId="0";

    public messageAdapter(ArrayList<message> messages,String s) {
        this.messages = messages;
        this.sId=s;

    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType==1){

           view=LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message,parent,false);}
        else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message,parent,false);
        }

        return new messageViewHolder(view);


    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSenderId().equals(sId)) {
        return 0;
        }else{
            return 1;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
        message curMssg=messages.get(position);
        holder.txtMess.setText(curMssg.getMssg());
        holder.txtTimeStamp.setText(curMssg.getTimeStamp());
        holder.user.setText("--"+curMssg.getSenderId());

    }
    public void updateList (ArrayList<message> items) {


            messages.clear();
            messages.addAll(items);
            notifyDataSetChanged();
        }


        @Override
    public int getItemCount() {
        return messages.size();
    }

    public class messageViewHolder extends RecyclerView.ViewHolder{
        TextView txtMess,txtTimeStamp,user;

        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMess =itemView.findViewById(R.id.txtReceiveMssg);
            txtTimeStamp=itemView.findViewById(R.id.txtTime);
            user=itemView.findViewById(R.id.txtUsername);



        }
    }
}
