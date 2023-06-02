package model.exceptions;

public class InvalidClusterNumberException extends IllegalArgumentException{
    public InvalidClusterNumberException(String message){
        super(message);
    }
}
