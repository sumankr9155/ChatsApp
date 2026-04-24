package com.sumankumar.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactArrayAdaptar extends ArrayAdapter<Current_List_data> {

    public ContactArrayAdaptar(@NonNull Context context, ArrayList<Current_List_data> arrayList) {

        super(context,0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView=convertView;
        if(currentItemView==null){
            currentItemView= LayoutInflater.from(getContext()).inflate(R.layout.contact_list,parent,false);
        }
        Current_List_data current_list_data_position=getItem(position);
        ImageView img=currentItemView.findViewById(R.id.profile_img);
        assert current_list_data_position!=null;

        img.setImageResource(current_list_data_position.getProfileImg());


        TextView title=currentItemView.findViewById(R.id.title);
        title.setText(current_list_data_position.getName());

        TextView subTitle=currentItemView.findViewById(R.id.sub_title);
        subTitle.setText(current_list_data_position.getLast_msg());

        TextView date=currentItemView.findViewById(R.id.last_msg_date);
        date.setText(current_list_data_position.getLast_msg_date());

        return currentItemView;
    }
}
