package com.aman.thinkin.pojos;

/**
 * Created by 1305274 on 09-04-2016.
 */
public class TeacherPojo {
    String name;
    String cabinNo;
    int mobileNo;
    int teacherPhoto;

    public void setTeacherName(String name){
        this.name=name;
    }
    public String getTeacherName(){
        return name;
    }

    public void setCabinNo(String cabinNo){
        this.cabinNo=cabinNo;
    }
    public String getCabinNo(){
        return cabinNo;
    }

    public void setMobileNo(int mobileNo){
        this.mobileNo=mobileNo;
    }
    public int getMobileNo(){
        return mobileNo;
    }

    public void setTeacherPhoto(int teacherPhoto){
        this.teacherPhoto=teacherPhoto;
    }

    public int getTeacherPhoto(){
        return teacherPhoto;
    }
}
