package fr.uga.l3miage.spring.tp3.exceptions.rest;


public class LastStepNotPassedRestException extends RuntimeException{
    public LastStepNotPassedRestException(String message) {
        super(message);
    }
}