package com.example.sokna.models;

public class Language {
    private static final Language ourInstance = new Language();
    private String language;

    private Language() {
        language = "en";
    }

    public static Language getInstance() {
        return ourInstance;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
