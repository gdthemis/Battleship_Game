package sample.controller.exceptions;

public class OversizeException extends Exception {
    public OversizeException(){
        super("Ships should not go out of bounds...");
    }
    public OversizeException(String message){
        super(message);
    }
}