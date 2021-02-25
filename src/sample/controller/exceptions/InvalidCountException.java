package sample.controller.exceptions;

public class InvalidCountException extends Exception {
    public InvalidCountException(){
        super("Wrong in ship types");
    }
    public InvalidCountException(String message){
        super(message);
    }
}