package fr.uga.l3miage.spring.tp3.exceptions.rest;


public class StepNotFoundRestException extends RuntimeException{


    public StepNotFoundRestException(String message) {
        super(message);

    }
}