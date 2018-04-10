package com.alexcfa.precipes.service;

import com.alexcfa.precipes.model.Recipe;

import java.util.List;

public class RecipeResponse {

    private String title;
    private String version;
    private List<Recipe> results;

    private RecipeResponse() {
    }

    public RecipeResponse(String title, String version, List<Recipe> results) {
        this.title = title;
        this.version = version;
        this.results = results;
    }

    public List<Recipe> getRecipes() {
        return results;
    }

}
