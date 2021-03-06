package com.gms.util.exception.domain;

import javassist.NotFoundException;

/**
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 * @version 0.1
 */
public class NotFoundEntityException extends NotFoundException {

    public NotFoundEntityException(String msg) {
        super(msg);
    }

    public NotFoundEntityException(String msg, Exception e) {
        super(msg, e);
    }
}
