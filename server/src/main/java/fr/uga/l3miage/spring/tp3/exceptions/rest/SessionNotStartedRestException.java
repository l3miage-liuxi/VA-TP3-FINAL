package fr.uga.l3miage.spring.tp3.exceptions.rest;

public class SessionNotStartedRestException extends RuntimeException{
    public SessionNotStartedRestException(String message) {
        super(message);
    }
}
