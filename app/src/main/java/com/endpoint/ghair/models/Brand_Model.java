package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class Brand_Model implements Serializable {
    private List<Data> data;
    private int current_page;

    public List<Data> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class Data implements Serializable {
        private int id;
        private String ar_title;
        private String en_title;
        private String ar_desc;
        private String en_desc;
        private String image;
        private int level;
        private String parent_id;
        private int custom_order;
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

        public String getAr_desc() {
            return ar_desc;
        }

        public String getEn_desc() {
            return en_desc;
        }

        public int getLevel() {
            return level;
        }

        public String getParent_id() {
            return parent_id;
        }

        public int getCustom_order() {
            return custom_order;
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
    }
}