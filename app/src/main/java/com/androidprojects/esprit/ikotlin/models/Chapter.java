package com.androidprojects.esprit.ikotlin.models;

/**
 * Created by Amal on 25/11/2017.
 */

public class Chapter {


    private int orderInCouse;
    private String title;
    private String description;
    private int nbBadges;
    private int timeTocomplete;
    private int quizzId;


    public Chapter(String title, String description, int orderInCouse) {
        this.title=title;
        this.description=description;
        this.orderInCouse=orderInCouse;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNbBadges() {
        return nbBadges;
    }

    public int getTimeTocomplete() {
        return timeTocomplete;
    }

    public int getQuizzId() {
        return quizzId;
    }

    public int getOrderInCouse() {
        return orderInCouse;
    }
}
