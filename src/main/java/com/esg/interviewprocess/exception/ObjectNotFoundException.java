package com.esg.interviewprocess.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends Exception
{
    public ObjectNotFoundException(String objectType)
    {
        super("Object type: " + objectType + "Not found");
    }
}
