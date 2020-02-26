package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class Cities_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public static class Data implements Serializable
    {
      private int id_city;
              private String ar_city_title;
              private String en_city_title;
              private String country_id;
              private String city_title;

        public Data(String city_title) {
            this.city_title = city_title;
        }

        public int getId_city() {
            return id_city;
        }

        public String getAr_city_title() {
            return ar_city_title;
        }

        public String getEn_city_title() {
            return en_city_title;
        }

        public String getCountry_id() {
            return country_id;
        }

        public String getCity_title() {
            return city_title;
        }
    }
}

