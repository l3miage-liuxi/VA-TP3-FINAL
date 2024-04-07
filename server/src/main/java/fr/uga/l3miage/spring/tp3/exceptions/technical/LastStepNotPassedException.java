package fr.uga.l3miage.spring.tp3.exceptions.technical;

import lombok.Getter;

@Getter
public class LastStepNotPassedException extends Exception{
    public LastStepNotPassedException(String message){
       super(message);
   }

}