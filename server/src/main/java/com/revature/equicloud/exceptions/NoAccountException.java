package com.revature.equicloud.exceptions;

public class NoAccountException extends Exception{
    public static String noAccount = "Specified Account Not Found";
    public NoAccountException(String message){
        super(message);
    }
}