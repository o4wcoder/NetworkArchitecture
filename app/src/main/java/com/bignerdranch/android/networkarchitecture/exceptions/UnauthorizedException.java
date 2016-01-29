package com.bignerdranch.android.networkarchitecture.exceptions;

/**
 * Created by Chris Hare on 10/20/2015.
 *
 * Exception to handle when the token is invalid
 */
public class UnauthorizedException extends RuntimeException {

    /**
     *
     * @param cause Whatever exception thrown by Retrofit while trying
     *              to make a request
     */
    public UnauthorizedException(RuntimeException cause) {
        super(cause);
    }
}
