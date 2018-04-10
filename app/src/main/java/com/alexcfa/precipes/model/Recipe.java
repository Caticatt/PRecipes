package com.alexcfa.precipes.model;

public class Recipe {

    private String title;
    private String href;
    private String ingredients;
    private String thumbnail;

    public Recipe() {
    }

    public Recipe(String title, String href, String ingredients, String thumbnail) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
