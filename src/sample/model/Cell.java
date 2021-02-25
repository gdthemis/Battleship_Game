package sample.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    private boolean isHitted;
    private int contains; // 0 : empty, else ship codename

    public Cell() {
        super(31,31);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        isHitted = false;
        contains = 0;
    }

    public Cell(Cell input) {
        super(31,31);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        isHitted = input.isHitted();
        contains = input.contains;
    }

    public Cell(int battleship) {
        super(31,31);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        contains = battleship;
        isHitted = false;
    }

    public boolean isHitted() {
        return isHitted;
    }

    public int contains(){
        return contains;
    }

    public int hit (int x, int y) {
        isHitted = true;
        return contains;
    }

    public void update(boolean yes) {
        if (yes)
            setFill(Color.GREEN);
        else
            setFill(Color.RED);
    }

    public void setContains(int contains) {
        this.contains = contains;
        if (contains > 0)
            setFill(Color.PURPLE);
    }
}
