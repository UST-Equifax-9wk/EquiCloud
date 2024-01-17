package com.revature.equicloud.exceptions;

public class InvalidInputException extends Exception{
    public static String invalidInput = "Invalid input";
    public static String duplicateUseOfUniqueAttribute="Unique Constraint Failed";
    public static String accountNotFound="Account not found";
    public InvalidInputException(String message){
        super(message);
    }
}
