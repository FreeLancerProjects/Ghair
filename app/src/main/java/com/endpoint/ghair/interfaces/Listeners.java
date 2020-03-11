package com.endpoint.ghair.interfaces;


import com.endpoint.ghair.models.ContactUsModel;
import com.endpoint.ghair.models.SignUpMArketModel;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String phone_code, String phone, String password);
    }
    interface TransFerListener {
        void checkData(String amount);
    }
    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUpCustomer(String name, String phone_code, String phone, String password);

    }
    interface EditprofileListener
    {
        void Editprofile(String name);
        void Editprofile(String englishname,String arabicname);


    }

    interface BackListener
    {
        void back();
    }
    interface SignupMArketListener
    {
        void checkDataSignUpMarket(SignUpMArketModel signUpMArketModel);
    }



    interface ContactListener
    {
        void sendContact(ContactUsModel contactUsModel);
    }

    interface ForgetListner
    {
        void forget();
    }
    interface PasswordListner {
        void checkDatapass(String pass);
    }
    interface ForgetpasswordListner {
        void checkDataForget(String phone_code, String phone);
    }
}
