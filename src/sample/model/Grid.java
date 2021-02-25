package sample.model;

import java.util.ArrayList;

public class Grid {
    private ArrayList<ArrayList<Cell>> grid;

    public Grid() {
        grid = new ArrayList<ArrayList<Cell>>();

        for (int i = 0; i < 10; i++) {
            grid.add(new ArrayList<Cell>());
            for (int j = 0; j < 10; j++) {
                grid.get(i).add(new Cell());
            }
        }
    }

    public ArrayList<ArrayList<Cell>> get_grid() {
        return grid;
    }

    public Cell get_cell(int x_axis, int y_axis) {
        return grid.get(x_axis).get(y_axis);
    }
}
