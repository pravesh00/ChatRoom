package com.five5.chatroom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.five5.chatroom.Data.mssg;
import com.five5.chatroom.R;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.messageViewHolder> {
    ArrayList<mssg> messages;
    String sId="0";

    public messageAdapter(ArrayList<mssg> messages,String s) {
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
        if(messages.get(position).getSender().equals(sId)) {
        return 0;
        }else{
            return 1;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
        mssg curMssg=messages.get(position);
        holder.txtMess.setText(curMssg.getText());
        holder.txtTimeStamp.setText(curMssg.getTime());
        holder.user.setText(curMssg.getSender());

    }
    public void updateList (ArrayList<mssg> items) {


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
