package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class MyAuctionModel implements Serializable {

    private int current_page;
    private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {

        private int id;
                private String  ar_title;
        private String en_title;
        private String start_date;
        private String end_date;
        private String start_price;
        private String selling_price;
        private String user_id;
                private int offer_id;
                private String taker_id;
                private String auction_image[];

                private int status;
                private String timeOutWithoutOffer;
                private String title;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getStart_date() {
            return start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public String getStart_price() {
            return start_price;
        }

        public String getSelling_price() {
            return selling_price;
        }

        public String getUser_id() {
            return user_id;
        }

        public int getOffer_id() {
            return offer_id;
        }

        public String getTaker_id() {
            return taker_id;
        }

        public String[] getAuction_image() {
            return auction_image;
        }

        public int getStatus() {
            return status;
        }

        public String getTimeOutWithoutOffer() {
            return timeOutWithoutOffer;
        }

        public String getTitle() {
            return title;
        }
    }
}
