package com.example.kolokvijum1;

public class Recipe {
    private String name;
    private int prepTimeMinutes;
    private boolean favorite;

    public Recipe(String name, int prepTimeMinutes, boolean favorite) {
        this.name = name;
        this.prepTimeMinutes = prepTimeMinutes;
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
