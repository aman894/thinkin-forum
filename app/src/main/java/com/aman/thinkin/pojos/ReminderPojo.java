package com.aman.thinkin.pojos;

/**
 * Created by 1305274 on 09-04-2016.
 */
public class ReminderPojo {
    String category;
    int categoryImage;
    public void setCategory(String category){
        this.category=category;
    };

    public String getCategory(){
        return category;
    }

    public void setCategoryImage(int categoryImage){
        this.categoryImage=categoryImage;
    }

    public int getCategoryImage(){
        return categoryImage;
    }
}
