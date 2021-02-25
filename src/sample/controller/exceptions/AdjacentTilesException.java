package sample.controller.exceptions;

public class AdjacentTilesException extends Exception {
    public AdjacentTilesException(){
        super("Adjacent...");
    }
    public AdjacentTilesException(String message){
        super(message);
    }
}