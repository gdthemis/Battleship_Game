package sample.controller.exceptions;

public class OverlapTilesException extends Exception {
    public OverlapTilesException(){
        super("Boats Should Not Overlap");
    }
    public OverlapTilesException(String message){
        super(message);
    }
}