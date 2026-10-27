package com.fullstack.authservice.exception;

public class MalCredencialException extends RuntimeException {
    public MalCredencialException(String mensaje) {
        super(mensaje);
    }
}