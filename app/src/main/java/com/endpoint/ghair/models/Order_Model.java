package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class Order_Model implements Serializable {

        private int current_page;
        private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
        {
            private int id;
                private int user_id;
                private int offer_id;
                private double longitude;
                private double latitude;
                private String address;
                private String other_phone;
                private int status;
                private String date;
                private String time;
                private String des;
                private double total_cost;
                private float rating;
                private String currency;
                private String order_payment_status;
                private String paypal_payment_id;
                private int  PayerID;
                private String paypal_token;
               private List<Order_details> order_details;

            public int getId() {
                return id;
            }

            public int getUser_id() {
                return user_id;
            }

            public int getOffer_id() {
                return offer_id;
            }

            public double getLongitude() {
                return longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public String getAddress() {
                return address;
            }

            public String getOther_phone() {
                return other_phone;
            }

            public int getStatus() {
                return status;
            }

            public String getDate() {
                return date;
            }

            public String getTime() {
                return time;
            }

            public String getDes() {
                return des;
            }

            public double getTotal_cost() {
                return total_cost;
            }

            public float getRating() {
                return rating;
            }

            public String getCurrency() {
                return currency;
            }

            public String getOrder_payment_status() {
                return order_payment_status;
            }

            public String getPaypal_payment_id() {
                return paypal_payment_id;
            }

            public int getPayerID() {
                return PayerID;
            }

            public String getPaypal_token() {
                return paypal_token;
            }

            public List<Order_details> getOrder_details() {
                return order_details;
            }

            public class Order_details implements Serializable
            {
                private int id;
                    private int order_id;
                    private int product_id;
                    private int market_id;
                    private int amount;
                    private double price;
                    private int offer_id;
                    private product product;
                    private Market market;

                public int getId() {
                    return id;
                }

                public int getOrder_id() {
                    return order_id;
                }

                public int getProduct_id() {
                    return product_id;
                }

                public int getMarket_id() {
                    return market_id;
                }

                public int getAmount() {
                    return amount;
                }

                public double getPrice() {
                    return price;
                }

                public int getOffer_id() {
                    return offer_id;
                }

                public Order_details.product getProduct() {
                    return product;
                }

                public Market getMarket() {
                    return market;
                }

                public class product implements Serializable {
                        private int id;
                        private String slug;
                        private double price;
                        private String ar_title;
                        private String en_title;
                        private String ar_desc;
                        private String en_desc;
                        private String model_title;
                        private int market_id;
                        private int cat_id;
                        private int brand_id;
                        private int have_offer;
                        private int offer_value;
                        private String offer_end_date;
                        private String offer_start_date;
                        private String offer_image;
                        private String offer_active;
                        private float rating;
                        private String main_image;
                        private int amount;

                        private String title;
                        private String desc;

                    public int getId() {
                        return id;
                    }

                    public String getSlug() {
                        return slug;
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

                    public int getMarket_id() {
                        return market_id;
                    }

                    public int getCat_id() {
                        return cat_id;
                    }

                    public int getBrand_id() {
                        return brand_id;
                    }

                    public int getHave_offer() {
                        return have_offer;
                    }

                    public int getOffer_value() {
                        return offer_value;
                    }

                    public String getOffer_end_date() {
                        return offer_end_date;
                    }

                    public String getOffer_start_date() {
                        return offer_start_date;
                    }

                    public String getOffer_image() {
                        return offer_image;
                    }

                    public String getOffer_active() {
                        return offer_active;
                    }

                    public float getRating() {
                        return rating;
                    }

                    public String getMain_image() {
                        return main_image;
                    }

                    public int getAmount() {
                        return amount;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public String getDesc() {
                        return desc;
                    }
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
            }
}}
