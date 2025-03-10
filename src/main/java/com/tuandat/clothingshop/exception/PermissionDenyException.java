package com.tuandat.clothingshop.exception;

public class PermissionDenyException extends RuntimeException{
    public PermissionDenyException(String msg){
        super(msg);
    }
}
