package com.example.firebaseapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.ChatActivity;
import com.example.firebaseapp.R;
import com.example.firebaseapp.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class  AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;

    FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context=context;
        this.chatList=chatList;
        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layouts: row_chat_left.xml for receiver, row_Chat_right.xml for sender
        if(i==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, viewGroup, false);
            return new MyHolder(view);
        }else {
            View view=LayoutInflater.from(context).inflate(R.layout.row_chat_left, viewGroup, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder,final int i) {
        //get data
        String message=chatList.get(i).getMessage();
        String timeStamp=chatList.get(i).getTimestamp();

        //convert time stamp to dd/mm/yyyy hh:mm am/pm
        //Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        //cal.setTimeInMillis(Long.parseLong(timeStamp));
        //String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
        //Calendar cal=Calendar.getInstance();
        //cal.setTimeInMillis(Long.parseLong(timeStamp));
        //SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        //String dateTime= simpleDateFormat.format(cal).toString();

        //set data
        myHolder.messageTv.setText(message);
        //myHolder.timeTv.setText(dateTime);

        try {
            Picasso.get().load(imageUrl).into(myHolder.profileIv);
        }catch (Exception e){ }

        //click to show delete dialog
        myHolder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete message confirm dialog
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Sil");
                builder.setMessage("Mesajı silmek istediğinize emin misiniz?");
                //delete button
                builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(i);
                    }
                });
                //cancel delete button
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss dialog
                        dialog.dismiss();
                    }
                });
                //create and show dialog
                builder.create().show();
            }
        });

        //set seen/delivered status of message
        if(i==chatList.size()-1){
            if(chatList.get(i).isSeen()){
                myHolder.isSeenTv.setText("Seen");
            }else{
                myHolder.isSeenTv.setText("Delivered");
            }
        }else{ myHolder.isSeenTv.setVisibility(View.GONE); }
    }

    private void deleteMessage(int position) {
        final String myUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        /* Logic:
         * Get timestamp of clicked message
         * Compare the timestamp of the clicked message with all messages in Chats
         * Where both values matches delete that message
         * This will allow sender to delete his and receiver's message
         */
        String msgTimeStamp=chatList.get(position).getTimestamp();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    /* if you want to allow sender to delete only his message then
                    compare sender value with current user's uid
                    if they match means its the message of sender that is trying to delete
                     */

                    if(ds.child("sender").getValue().equals(myUID)){
                        /* We can do one of two things here
                         * 1) Remove the message from chats
                         * 2) Set the value of message "This message was deleted..."
                         *  So do whatever you want
                         */

                        // 1)Remove the message from Chats
                        ds.getRef().removeValue();

                        // 2) Set the value of message "This message was deleted.."
                        //HashMap<String,Object> hashMap=new HashMap<>();
                        //hashMap.put("message","Bu mesaj silindi...");
                        //ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context,"mesaj silindi...",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Sadece kendi mesajını silebilirsin...",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() { return chatList.size(); }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return  MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        //views
        ImageView profileIv;
        TextView messageTv, timeTv, isSeenTv;
        LinearLayout messageLayout; //for click listener to show delete

        public MyHolder(@NonNull View itemView){
            super(itemView);

            //init views
            profileIv=itemView.findViewById(R.id.profileIv);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            isSeenTv=itemView.findViewById(R.id.isSeenTv);
            messageLayout=itemView.findViewById(R.id.messageLayout);

        }
    }
}




