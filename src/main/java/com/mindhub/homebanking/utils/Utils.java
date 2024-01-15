package com.mindhub.homebanking.utils;


public class Utils {


    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String generateCardNumber(){
        String number1 = String.valueOf(getRandomNumber(4000, 9999));
        String number2 = String.valueOf(getRandomNumber(1000, 9999));
        String number3 = String.valueOf(getRandomNumber(1000, 9999));
        String number4 = String.valueOf(getRandomNumber(1000, 9999));
        return number1 + "-" + number2 + "-" + number3 + "-" + number4;
    }

    public static String generateCvv(){
        return String.valueOf(getRandomNumber(100, 999));
    }

    public static String generateAccountNumber(){
        String number = "VIN-" + getRandomNumber(100000, 99999999);
        return number;
    }
}
