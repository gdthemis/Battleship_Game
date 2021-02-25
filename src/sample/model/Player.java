package sample.model;

import javafx.scene.paint.Color;
import sample.model.battleships.*;
import sample.controller.exceptions.*;

import java.util.*;

public class Player {
    private Battleship _battleship;
    private Carrier _carrier;
    private Destroyer _destroyer;
    private Cruiser _cruiser;
    private Submarine _submarine;
    public Grid my_grid;
    public Grid shooting_grid;

    public int points;
    public int remaining_hits;
    public int hits = 0;
    public ArrayList<Quartet> past_moves;

    public Player(char[][] place) {
        remaining_hits = 40;
        points = 0;
        my_grid = new Grid();
        shooting_grid = new Grid();
        _battleship = new Battleship();
        set_ships(place);
        past_moves = new ArrayList<>();
    }

    public Player() {
        remaining_hits = 40;
        points = 0;
        my_grid = new Grid();
        shooting_grid = new Grid();
        _battleship = new Battleship();
        _cruiser = new Cruiser();
        _submarine = new Submarine();
        _destroyer = new Destroyer();
        _carrier = new Carrier();
        past_moves = new ArrayList<>();
    }

    private void set_ships(char[][] input) {
        try {
            ArrayList<Integer> seen = new ArrayList<>();
            seen.add(0);seen.add(0);seen.add(0);seen.add(0);seen.add(0);
//            for (int i = 0; i < 10; i++) {
//                System.out.println();
//                for (int j = 0; j < 10; j++) {
//                    System.out.print(input[i][j]);
//                    System.out.print(" ");
//                }
//            }
            for (int i = 0; i < 5; i++)
            {
                if (seen.get(input[i][0] - '0'  - 1) != 0)
                    throw new InvalidCountException();

                seen.set(input[i][0] - '0'  - 1, 1);

                switch (input[i][0]) {
                    case '1' : _carrier.set(my_grid, input[i][2] - '0', input[i][4] - '0', input[i][6] - '0');
                    break;
                    case '2' : _battleship.set(my_grid, input[i][2] - '0', input[i][4] - '0', input[i][6] - '0');
                    break;
                    case '3' : _cruiser.set(my_grid, input[i][2] - '0', input[i][4] - '0', input[i][6] - '0');
                    break;
                    case '4' : _submarine.set(my_grid, input[i][2] - '0', input[i][4] - '0', input[i][6] - '0');
                    break;
                    case '5' : _destroyer.set(my_grid, input[i][2] - '0', input[i][4] - '0', input[i][6] - '0');
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int hit(int x, int y) {
        return my_grid.get_cell(x,y).hit(x,y);
    }

    public void update_after_hit (int x, int y, boolean a)
    {
        shooting_grid.get_cell(x,y).update(a);
    }

    public int update (int x, int y, boolean b, int type) {
        int a = 0;
        switch (type) {
            case 1 : a = _carrier.set_hit(true); break;
            case 2 : a =  _battleship.set_hit(true); break;
            case 3 : a =  _cruiser.set_hit(true); break;
            case 4 : a = _submarine.set_hit(true); break;
            case 5 : a =  _destroyer.set_hit(true); break;
        }
        return a;
    }

    public void update_after_hit_enemy (int x, int y, boolean a)
    {
        my_grid.get_cell(x,y).update(a);
    }

    public void set (char[][] inp) {
        set_ships(inp);
    }

    public Battleship get_battleship() {
        return _battleship;
    }

    public Carrier get_carrier() {
        return _carrier;
    }

    public Cruiser get_cruiser() {
        return _cruiser;
    }

    public Destroyer get_destroyer() {
        return _destroyer;
    }

    public Submarine get_submarine() {
        return _submarine;
    }
}
