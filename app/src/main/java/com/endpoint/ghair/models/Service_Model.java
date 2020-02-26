package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class Service_Model implements Serializable {
    private List<Data> data;
    private int current_page;

    public List<Data> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public static class Data implements Serializable
    {
        public Data(String title) {
            this.title = title;
        }

        private int id;
            private String ar_title;
            private String en_title;
            private String ar_description;
            private String en_description;
            private String image;

            private String title;
            private String desc;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_description() {
            return ar_description;
        }

        public String getEn_description() {
            return en_description;
        }

        public String getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }
    }}