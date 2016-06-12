package com.aman.thinkin.planner.planner.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.thinkin.R;
import com.aman.thinkin.planner.planner_components.Planner_Reminder;
import com.aman.thinkin.pojos.TeacherPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1305274 on 20-05-2016.
 */
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>{
    List<TeacherPojo> mItems;
    int[] adminImage={R.drawable.admin1, R.drawable.admin2, R.drawable.admin3, R.drawable.admin4, R.drawable.admin5, R.drawable.admin6, R.drawable.admin7};
    String[] adminName={"Amandeep Malhotra","Ariz Eqbal","Gourav Beura","Harsh Soni","Prachi Tiwari","Shaleen Mehrotra","Tatwika Kashyap"};
    String[] adminContact={"+918339042006","+918339041825","+918763637189","+917749995105","+919776315374","+919776306718","+919040442807"};
    String[] cabinNo={"C5/1","C3/1","C5/2","C4/1","C4/2","C3/2","C5/3"};
    Activity context;
    public MeetingAdapter(Activity activity){
        context=activity;
        mItems=new ArrayList<>();
        TeacherPojo[] teacherPojos=new TeacherPojo[adminImage.length];
        for(int i=0;i<teacherPojos.length;i++){
            teacherPojos[i]=new TeacherPojo();
            teacherPojos[i].setTeacherPhoto(adminImage[i]);
            teacherPojos[i].setTeacherName(adminName[i]);
            teacherPojos[i].setCabinNo(cabinNo[i]);
            teacherPojos[i].setMobileNo(adminContact[i]);
            mItems.add(teacherPojos[i]);
        }
    }
    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_single_item,parent,false);
        MeetingViewHolder meetingViewHolder=new MeetingViewHolder(v);
        return meetingViewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        TeacherPojo teacherPojo=mItems.get(position);
        holder.img_admin.setImageResource(teacherPojo.getTeacherPhoto());
        holder.txt_adminName.setText(teacherPojo.getTeacherName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView img_admin;
        public TextView txt_adminName;
        public MeetingViewHolder(View itemView)
        {
            super(itemView);
            img_admin= (ImageView) itemView.findViewById(R.id.admin_image);
            txt_adminName= (TextView) itemView.findViewById(R.id.admin_name);
            itemView.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getContext(), Planner_Reminder.class);
            
            switch (getAdapterPosition()){
                case 0:
                    intent.putExtra("AdminName",adminName[0]);
                    intent.putExtra("AdminCabin",cabinNo[0]);
                    intent.putExtra("AdminMobile",adminContact[0]);
                    break;
                case 1:
                    intent.putExtra("AdminName",adminName[1]);
                    intent.putExtra("AdminCabin",cabinNo[1]);
                    intent.putExtra("AdminMobile",adminContact[1]);
                    break;
                case 2:
                    intent.putExtra("AdminName",adminName[2]);
                    intent.putExtra("AdminCabin",cabinNo[2]);
                    intent.putExtra("AdminMobile",adminContact[2]);
                    break;
                case 3:
                    intent.putExtra("AdminName",adminName[3]);
                    intent.putExtra("AdminCabin",cabinNo[3]);
                    intent.putExtra("AdminMobile",adminContact[3]);
                    break;
                case 4:
                    intent.putExtra("AdminName",adminName[4]);
                    intent.putExtra("AdminCabin",cabinNo[4]);
                    intent.putExtra("AdminMobile",adminContact[4]);
                    break;
                case 5:
                    intent.putExtra("AdminName",adminName[5]);
                    intent.putExtra("AdminCabin",cabinNo[5]);
                    intent.putExtra("AdminMobile",adminContact[5]);
                    break;
                case 6:
                     
                    intent.putExtra("AdminName",adminName[6]);
                    intent.putExtra("AdminCabin",cabinNo[6]);
                    intent.putExtra("AdminMobile",adminContact[6]);
                    break;
                default:
                    Toast.makeText(v.getContext(), "Work Under Progress..!", Toast.LENGTH_SHORT).show();
                    break;
            }
            v.getContext().startActivity(intent);
        }
    }
}
