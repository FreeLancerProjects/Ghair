package com.endpoint.ghair.models;

import java.io.Serializable;
import java.util.List;

public class MyServiceModel implements Serializable {

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
                    private String user_id;
                    private String market_id;
                    private String service_id;
                    private String address;
                    private double latitude;
                    private double longitude;
                    private String date;
                    private String time;
                    private String finished_date;
                    private double price;
                    private String details;
                    private String status;
                    private String rating;
                    private Market market;
                    private Service service;

            public int getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getMarket_id() {
                return market_id;
            }

            public String getService_id() {
                return service_id;
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

            public String getDate() {
                return date;
            }

            public String getTime() {
                return time;
            }

            public String getFinished_date() {
                return finished_date;
            }

            public double getPrice() {
                return price;
            }

            public String getDetails() {
                return details;
            }

            public String getStatus() {
                return status;
            }

            public String getRating() {
                return rating;
            }

            public Market getMarket() {
                return market;
            }

            public Service getService() {
                return service;
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
                  public class Service implements Serializable {
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
                  }
            }
}
