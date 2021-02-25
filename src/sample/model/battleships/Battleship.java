package sample.model.battleships;

import sample.controller.exceptions.*;
import sample.model.Cell;
import sample.model.Grid;
import sample.model.Ship;

import java.util.ArrayList;

public class Battleship extends Ship {

    public int points_per_heat = 250;
    public int bonus = 500;
    public int space = 4;
    public int points = 0;

    public Battleship(boolean not_touched, boolean hit, boolean sinked) {
        super(not_touched, hit, sinked);
    }

    public Battleship() {
        super(true, false, false);
    }

    public void set (Grid grid, int x, int y, int orientation) throws Exception {
        try {
//            System.out.println(x);
//            System.out.println(orientation);
            _adjacent_check(grid, x, y);

            if (x > 9 || x < 0 || y > 9 || y < 0)
                throw new OversizeException();

            if (orientation == 1) {
                for (int i = 0; i < space; i++) {
                    if (y + i > 9)
                        throw new OversizeException();

                    if (grid.get_cell(x, y + i).contains() != 0)
                        throw new OverlapTilesException();

                    grid.get_cell(x, y + i).setContains(2);
                }
            }
            else {
                for (int i = 0; i < space; i++) {
                    if (x + i > 9)
                        throw new OversizeException();
                    if (grid.get_cell(x + i, y).contains() != 0)
                        throw new OverlapTilesException();

                    grid.get_cell(i + x, y).setContains(2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean _adjacent_check (Grid grid, int x, int y) throws Exception {
        int temp = grid.get_cell(Math.min(x+1,9), y).contains();
        if (!(temp == 2 || temp == 0))
            throw new AdjacentTilesException();

        temp = grid.get_cell(Math.max(x-1,0), y).contains();

        if (!(temp == 2 || temp == 0))
            throw new AdjacentTilesException();

        temp = grid.get_cell(x,Math.max(y-1, 0)).contains();

        if (!(temp == 2 || temp == 0))
            throw new AdjacentTilesException();

        temp = grid.get_cell(x, Math.min(y+1,9)).contains();

        if (!(temp == 2 || temp == 0))
            throw new AdjacentTilesException();

        return true;
    }

    public int set_hit(boolean _hit) {
        int result = 0;
        this._hit = true;
        this._not_touched = false;
        result = points_per_heat;
        points += points_per_heat;
        if (points_per_heat * space == points) {
            result += bonus;
            _sinked = true;
        }

        return result;
    }

}