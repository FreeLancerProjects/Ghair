package com.endpoint.ghair.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {
   private String logo;
           private String titleOfApp;
           private String about_us;
           private String terms;

    public String getLogo() {
        return logo;
    }

    public String getTitleOfApp() {
        return titleOfApp;
    }

    public String getAbout_us() {
        return about_us;
    }

    public String getTerms() {
        return terms;
    }
}
