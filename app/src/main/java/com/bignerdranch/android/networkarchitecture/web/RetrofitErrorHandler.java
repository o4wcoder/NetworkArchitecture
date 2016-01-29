package com.bignerdranch.android.networkarchitecture.web;


import com.bignerdranch.android.networkarchitecture.exceptions.UnauthorizedException;

import java.net.HttpURLConnection;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Chris Hare on 10/20/2015.
 */
public class RetrofitErrorHandler implements ErrorHandler {


    @Override
    public Throwable handleError(RetrofitError cause) {

        Response response = cause.getResponse();
        //See if we have the 401 error code and return the UnauthorizedException
        if(response != null && response.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            return new UnauthorizedException(cause);
        }

        return cause;
    }
}
