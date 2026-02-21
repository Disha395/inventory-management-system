package com.example.ims_backend.exceptions;

public class NameValueRequiredException extends RuntimeException{
    public NameValueRequiredException(String msg){
        super(msg);
    }
}
