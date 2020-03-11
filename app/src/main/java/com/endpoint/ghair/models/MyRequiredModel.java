package com.endpoint.ghair.models;

import org.stringtemplate.v4.ST;

import java.io.Serializable;
import java.util.List;

public class MyRequiredModel implements Serializable {

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
        private double price;
        private String ar_title;
        private String en_title;
        private String ar_desc;
        private String en_desc;
        private String model_title;
        private String user_id;
        private String market_id;
        private int brand_id;
        private int offer_id;
        private int cat_id;
        private String title;
        private int amount;
        private String required_type;

        private Market market;
        private int status;
        private Brand brand;
        private String created_at;

        public String getCreated_at() {
            return created_at;
        }

        public String getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }

        public double getPrice() {
            return price;
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

        public String getModel_title() {
            return model_title;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getMarket_id() {
            return market_id;
        }

        public int getBrand_id() {
            return brand_id;
        }

        public int getOffer_id() {
            return offer_id;
        }

        public int getCat_id() {
            return cat_id;
        }

        public int getAmount() {
            return amount;
        }

        public String getRequired_type() {
            return required_type;
        }

        public Market getMarket() {
            return market;
        }

        public int getStatus() {
            return status;
        }

        public Brand getBrand() {
            return brand;
        }

        public class Market implements Serializable {
            private int id;
            private String user_type;
            private String name;
            private String ar_market_title;
            private String en_market_title;
            private String email;
            private String phone_code;
            private String phone;
            private String national_number;
            private String address;
            private double latitude;
            private double longitude;
            private String logo;
            private String email_verified_at;
            private int country_id;
            private int city_id;
            private int block;
            private String is_accepted;
            private int is_login;
            private long logout_time;
            private int is_confirmed;

            private String token;
            private String market_title;

            public int getId() {
                return id;
            }

            public String getUser_type() {
                return user_type;
            }

            public String getName() {
                return name;
            }

            public String getAr_market_title() {
                return ar_market_title;
            }

            public String getEn_market_title() {
                return en_market_title;
            }

            public String getEmail() {
                return email;
            }

            public String getPhone_code() {
                return phone_code;
            }

            public String getPhone() {
                return phone;
            }

            public String getNational_number() {
                return national_number;
            }

            public String getAddress() {
                return address;
            }

            public double getLatitude() {
                return latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public String getLogo() {
                return logo;
            }

            public String getEmail_verified_at() {
                return email_verified_at;
            }

            public int getCountry_id() {
                return country_id;
            }

            public int getCity_id() {
                return city_id;
            }

            public int getBlock() {
                return block;
            }

            public String getIs_accepted() {
                return is_accepted;
            }

            public int getIs_login() {
                return is_login;
            }

            public long getLogout_time() {
                return logout_time;
            }

            public int getIs_confirmed() {
                return is_confirmed;
            }

            public String getToken() {
                return token;
            }

            public String getMarket_title() {
                return market_title;
            }
        }

        public class Brand implements Serializable {
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

            public String getImage() {
                return image;
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

            public String getTitle() {
                return title;
            }

            public String getDesc() {
                return desc;
            }
        }
    }
}
