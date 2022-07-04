package com.quizz.model;

public class Category {

    String categoryName;
    String level;
    String image;

    public Category(String categoryName, String level, String image) {
        this.categoryName = categoryName;
        this.level = level;
        this.image = image;
    }

    public Category(){

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
