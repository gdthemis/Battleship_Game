package sample.model.battleships;

import sample.model.Grid;
import sample.model.Ship;
import sample.controller.exceptions.*;

public class Carrier extends Ship {

    public int points_per_heat = 350;
    public int bonus = 1000;
    public int space = 5;
    public int points = 0;

    public Carrier(boolean not_touched, boolean hit, boolean sinked) {
        super(not_touched, hit, sinked);
    }

    public Carrier() {
        super(true, false, false);
    }


    public void set (Grid grid, int x, int y, int orientation) throws Exception {
        try {
            _adjacent_check(grid, x, y);

            if (x > 9 || x < 0 || y > 9 || y < 0)
                throw new OversizeException();

            if (orientation == 1) {
                for (int i = 0; i < 5; i++) {
                    if (y + i > 9)
                        throw new OversizeException();

                    if (grid.get_cell(x, y + i).contains() != 0)
                        throw new OverlapTilesException();

                    grid.get_cell(x, y + i).setContains(1);
                }
            }
            else {
                for (int i = 0; i < 5; i++) {
                    if (x + i > 9)
                        throw new OversizeException();

                    if (grid.get_cell(x + i, y).contains() != 0)
                        throw new OverlapTilesException();

                    grid.get_cell(i + x, y).setContains(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private boolean _adjacent_check (Grid grid, int x, int y) throws Exception {
        int temp = grid.get_cell(Math.min(x+1,9), y).contains();
        if (!(temp == 1 || temp == 0))
            throw new AdjacentTilesException();

        temp = grid.get_cell(Math.max(x-1,0), y).contains();

        if (!(temp == 1 || temp == 0))
            throw new AdjacentTilesException();

        temp = grid.get_cell(x,Math.max(y-1, 0)).contains();

        if (!(temp == 1 || temp == 0))
            throw new AdjacentTilesException();

        temp = grid.get_cell(x, Math.min(y+1,9)).contains();

        if (!(temp == 1 || temp == 0))
            throw new AdjacentTilesException();

        return true;
    }
}
