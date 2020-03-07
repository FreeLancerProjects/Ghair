package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class Add_Order_Model implements Serializable {


   private List<Products> details;

    public List<Products> getDetails() {
        return details;
    }

    public void setDetails(List<Products> details) {
        this.details = details;
    }

    public static class Products implements Serializable {
      private int market_id;
              private int product_id;
              private int amount;
              private double price;
        private String ar_title;
        private String en_title;
private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAr_title() {
            return ar_title;
        }

        public void setAr_title(String ar_title) {
            this.ar_title = ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public void setEn_title(String en_title) {
            this.en_title = en_title;
        }

        public int getMarket_id() {
            return market_id;
        }

        public void setMarket_id(int market_id) {
            this.market_id = market_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }



}
