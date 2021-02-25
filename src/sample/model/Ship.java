package sample.model;

import javafx.util.Pair;

import java.util.ArrayList;

public abstract class Ship {
    protected boolean _not_touched;
    protected boolean _hit;
    protected boolean _sinked;

    public Ship (boolean not_touched, boolean hit, boolean sinked) {
        _not_touched = not_touched;
        _hit = hit;
        _sinked = sinked;
    }

    public boolean is_hit() {
        return _hit;
    }

    public boolean is_not_touched() {
        return _not_touched;
    }

    public boolean is_sinked() {
        return _sinked;
    }

    public void set_not_touched(boolean _not_touched) {
        this._not_touched = _not_touched;
    }

    public void set_sinked(boolean _sinked) {
        this._sinked = _sinked;
    }

    public abstract int set_hit(boolean _hit);
}
