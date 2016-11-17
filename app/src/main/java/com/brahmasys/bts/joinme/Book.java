package com.brahmasys.bts.joinme;

/**
 * Created by apple on 29/04/16.
 */
public class Book {

    private String name;
    private String imageUrl;
    private String authorName;
    private String time;
    private String icon_image;
    boolean selected = false;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public   String getTime()
    {
        return time;
    }
    public  void setTime(String time)
    {
        this.time = time;
    }

    public  String getIcon_image()
    {
        return  icon_image;
    }
    public  void setIcon_image(String icon_image)
    {
        this.icon_image = icon_image;
    }


    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}