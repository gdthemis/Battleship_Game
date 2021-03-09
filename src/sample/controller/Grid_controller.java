package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Grid;
import sample.model.Player;
import sample.model.Quartet;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;


public class Grid_controller implements Initializable{

    @FXML
    public MenuItem load_id;

    @FXML
    public GridPane player_table;

    @FXML
    public GridPane enemy_table;

    @FXML
    public Button submit_move;

    @FXML
    public TextField row_id;

    @FXML
    public TextField column_id;

    @FXML
    public Text player_points;

    @FXML
    public Text player_attempts;

    @FXML
    public Text player_ratio;

    @FXML
    public Text comp_points;

    @FXML
    public Text comp_attempts;

    @FXML
    public Text comp_ratio;

    @FXML
    public MenuItem exit_id;

    @FXML
    public MenuItem start_id;


    private boolean _was_miss = true;
    private int next_x = -10;
    private int next_y = -10;

    private char[][] _player;
    private char[][] _enemy;
    public Player person, pc;
    private boolean has_started = false;
    private boolean _first_move = false;
    private int random;

    private boolean _isLoaded = false;

    private ArrayList<ArrayList<Integer>> neighbs;
    private int _last_type;
    private int it = 0;

    private boolean _game_ended = false;


    @Override
/**
 * Initialize function.
 */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        person = new Player();
        pc = new Player();


//        player_table.add(new Rectangle(10,10), 0,0);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                player_table.add((person.my_grid.get_cell(i, j)), j, i);
                enemy_table.add((person.shooting_grid.get_cell(i, j)), j, i);
            }
        }
    }

    @FXML
/**
 * This function is called when the button submit is pressed, and controlls what will happen next.
 */
    public void submit_move_controller(javafx.event.ActionEvent e){
        if (!has_started)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You should Start and Load the Game first");
            alert.showAndWait();
            return;
        }

        if (_game_ended)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Restart game");
            alert.setContentText("Game ended restart game ");
            alert.showAndWait();
            return;
        }

            update(Integer.parseInt(row_id.getText().toString()), Integer.parseInt(column_id.getText().toString()));
            update_pc();


            if (pc.remaining_hits == 0)
            {
                if (pc.points < person.points) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Your moves ended, player wins");
                    alert.setContentText("Player Won");
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Your moves ended, pc wins, as it has more points");
                    alert.setContentText("Pc Won");
                    alert.showAndWait();
                }
                _game_ended = true;
                return;
            }

        if (person.remaining_hits == 0)
        {
            if (pc.points < person.points) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Your moves ended, player wins");
                alert.setContentText("Player Won, as you have more points");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Your moves ended, pc wins");
                alert.setContentText("Pc Won, as it has more points");
                alert.showAndWait();
            }
            return;
        }

        if (person.points == 5200)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You won");
            alert.setContentText("You sinked all ships, so you are the winner!");
            alert.showAndWait();
            _game_ended = true;
            return;
        }

        if (pc.points == 5200)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You lost");
            alert.setContentText("Pc sinked all your ships, so you lose!");
            alert.showAndWait();
            _game_ended = true;
            return;
        }
    }

    @FXML
    /**
     * This function is called when start button is pressed. It is supposed to start or restart the game.
     */
    public void start_controller(javafx.event.ActionEvent e){
        if (_isLoaded) {
            if (has_started) {
                person = new Player();
                pc = new Player();
                it = 0;
                neighbs.clear();
                _was_miss = true;
                next_x = -10;
                next_y = -10;
                _game_ended = false;
                player_points.setText(Integer.toString(person.points));
                player_ratio.setText(Double.toString((1.0*person.hits/(40 - person.remaining_hits))*100) + " %");
                comp_ratio.setText(Double.toString((1.0*pc.hits/(40 - pc.remaining_hits))*100) + " %");
                comp_attempts.setText(Integer.toString(40 - pc.remaining_hits));

                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        player_table.add((person.my_grid.get_cell(i, j)), j, i);
                        enemy_table.add((person.shooting_grid.get_cell(i, j)), j, i);
                    }
                }
                _isLoaded = false;
                has_started = false;
            }

            if (_isLoaded) {

                random = ThreadLocalRandom.current().nextInt(0, 3);
                _first_move = true;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("First Move");
                if (random == 0)
                    alert.setContentText("You are playing first");
                else
                    alert.setContentText("Computer will play first");
                alert.showAndWait();

                if (random != 0)
                    update_pc();

                has_started = true;
//        _isLoaded = false;
            }
        }

        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You should load the game scenario first");
            alert.setContentText("Press Load and then press Start");
            alert.showAndWait();

        }
    }

    /**
     * This Void updates the player grid based on the coordinates they entered
     * @param x_axis : int, x axis coordinate
     * @param y_axis : int, y axis coordinate
     */
    private void update(int x_axis, int y_axis) {
        int rest = person.remaining_hits - 1;
        person.remaining_hits -= 1;
        player_attempts.setText(Integer.toString(40 - person.remaining_hits));

        int res = pc.hit(x_axis, y_axis);

        if (res > 0) {
            person.update_after_hit(x_axis, y_axis,true);
            int temp = pc.update(x_axis, y_axis, true, res);
            person.points += temp;
//            System.out.println(person.points);
            player_points.setText(Integer.toString(person.points));
            person.hits += 1;
        }
        else {
            person.update_after_hit(x_axis, y_axis, false);
        }
        person.past_moves.add(new Quartet(x_axis, y_axis, res == 0 ? false : true, res));
        player_ratio.setText(Double.toString((1.0*person.hits/(40 - person.remaining_hits))*100) + " %");

    }

    /**
     * This Void updates the enemy
     */
    private void update_pc() {

        if(!_was_miss)
        {
            if (it == neighbs.size())
            {
                sink_the_ship(neighbs.get(it-1).get(0),neighbs.get(it-1).get(1),_last_type);
                return;
            }

            sink_the_ship(neighbs.get(it).get(0),neighbs.get(it).get(1),_last_type);

            return;
        }

        int rest = pc.remaining_hits - 1;
        pc.remaining_hits -= 1;
        comp_attempts.setText(Integer.toString(40 - pc.remaining_hits));
        int y, x;
        do {
            y = ThreadLocalRandom.current().nextInt(0, 9 + 1);
            x = ThreadLocalRandom.current().nextInt(0, 9 + 1);
        }
        while(person.my_grid.get_cell(x,y).isHitted());

        int res = person.hit(x, y);

        if (res > 0) {
            person.update_after_hit_enemy(x, y,true);
            int temp = person.update(x, y, true, res);
            pc.points += temp;
//            System.out.println(pc.points);
            comp_points.setText(Integer.toString(pc.points));
            pc.hits += 1;
            neighbs = new ArrayList<>();
            neighbs = _get_neighbs(x, y);
            _was_miss = false;
            _last_type= res;
            it = 0;
        }
        else {
            person.update_after_hit_enemy(x, y, false);
        }

//        float ratio
        pc.past_moves.add(new Quartet(x, y, res == 0 ? false : true, res));
        comp_ratio.setText(Double.toString((1.0*pc.hits/(40 - pc.remaining_hits))*100) + " %");
    }

    /**
     * This Void updates the enemy and it's purpose is to sink a ship that descovered with random moves.
     * @param x : int, the x parameter that will hit. It may change inside the void.
     * @param y : int, the y parameter that will hit. It may change inside the void.
     * @param type : int, it referes to the type of ship that was descovered.
     */
    private void sink_the_ship(int x, int y, int type) {

        pc.remaining_hits -= 1;


        if(next_x != -10 && next_y != -10)
        {
            x = next_x;
            y = next_y;
            if (x < 0 || x > 9 || y < 0 || y > 9 || person.my_grid.get_cell(x, y).isHitted())
            {
                next_y = -10;
                next_x = -10;
            }

        }

        if (next_x == -10 && next_y == -10){
            do {
                x = neighbs.get(it).get(0);
                y = neighbs.get(it).get(1);
                it++;
            } while (person.my_grid.get_cell(x, y).isHitted() && it < neighbs.size());
        }

        comp_attempts.setText(Integer.toString(40 - pc.remaining_hits));

        int res = person.hit(x, y);

        if (res > 0) {
            person.update_after_hit_enemy(x, y,true);
            int temp = person.update(x, y, true, res);
            pc.points += temp;
//            System.out.println(pc.points);
            comp_points.setText(Integer.toString(pc.points));
            pc.hits += 1;
//            neighbs = new ArrayList<>();
//            neighbs = _get_neighbs(x, y);
//            _was_miss = false;
//            _last_type= res;
//            it = 0;
            switch (neighbs.get(it-1).get(2)) {
                case 1 : next_x = x - 1; next_y = y; break;
                case 2 : next_x = x + 1; next_y = y; break;
                case 3 : next_x = x; next_y = y - 1; break;
                case 4 : next_x = x; next_y = y + 1; break;
            }

            boolean fin = false;

            switch (res) {
                case 1 : fin = person.get_carrier().is_sinked(); break;
                case 2 : fin = person.get_battleship().is_sinked(); break;
                case 3 : fin = person.get_cruiser().is_sinked(); break;
                case 4 : fin = person.get_submarine().is_sinked(); break;
                case 5 : fin = person.get_destroyer().is_sinked(); break;
            }

            if (fin)
            {
                _was_miss = true;
                neighbs.clear();
                _last_type= -1;
                it = 0;
                next_y = -10;
                next_x = -10;
            }
        }
        else {
            person.update_after_hit_enemy(x, y, false);
            next_x = -10;
            next_y = -10;
        }

        pc.past_moves.add(new Quartet(x, y, res == 0 ? false : true, res));
        comp_ratio.setText(Double.toString((1.0*pc.hits/(40 - pc.remaining_hits))*100) + " %");

    }

    /**
     *This function returns the valid neighbors of a cell.
     * @param x : int, x coordinate of cell.
     * @param y : int, y coordinate of cell.
     * @return : an array with all the valid neighbours.
     */
    private ArrayList<ArrayList<Integer>> _get_neighbs (int x, int y)
    {
        ArrayList<ArrayList<Integer>> neighbs = new ArrayList<>();
        if (x-1 >= 0)
            neighbs.add(new ArrayList<Integer>(Arrays.asList(x-1, y, 1)));

        if (x+1 < 10)
            neighbs.add(new ArrayList<Integer>(Arrays.asList(x+1, y, 2)));

        if (y-1 >= 0)
            neighbs.add(new ArrayList<Integer>(Arrays.asList(x, y-1, 3)));

        if(y+1 < 10)
            neighbs.add(new ArrayList<Integer>(Arrays.asList(x, y+1, 4)));
        return neighbs;
    }


    @FXML
    /**
     * This Void is called when the load button is pressed. It loads a player scenario.
     */
    public void load_action(javafx.event.ActionEvent e) throws Exception {

        TextInputDialog item = new TextInputDialog();

        item.setTitle("select files");

        item.setContentText("Enter the n. of test case \nFor example, if you want to run test case 1, type 1");

        Optional<String> file_getter = item.showAndWait();

        String text = file_getter.get();

        _enemy = reader(text, "_enemy");
        _player = reader(text, "_player");

        person.set(_player);
        pc.set(_enemy);

        _isLoaded = true;
    }

    /**
     * This Function reades the text file that has the input
     * @param text : it determines in which scenario we are.
     * @param decider : it decides which text we are reading (enemy or player)
     * @return : an array with the file.
     * @throws Exception
     */
    private char[][] reader (String text, String decider) throws Exception {
        String path = "/Users/georgiosthemelis/IdeaProjects/proj3/project10/src/sample/scenario_" + text;

        final File input = new File(path + decider);

        final char[][] world = new char[11][11];

        if(!input.exists())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Such file");
            alert.setContentText("Enter a file that exists.");
            alert.showAndWait();
            world[0][0] = '-';

            return world;
        }

        final BufferedReader br = new BufferedReader(new FileReader(input));
        String temp;
        int i = 0;

        while ((temp = br.readLine()) != null) {
            world[i] = temp.toCharArray();
            i++;
        }

        return world;
    }

    @FXML
    public void exit_controller(ActionEvent e) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure you want to quit?");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK"))
        {
            javafx.application.Platform.exit();
        }
    }

    /**
     * This function makes a pop-up with enemy ships condition.
     * @param actionEvent
     * @throws IOException
     */
    public void enemy_ships_controller(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        String s;
        s = "is hit : " + (pc.get_carrier().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_carrier().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (pc.get_carrier().is_sinked() ? "yes\n" : "no\n");
        Text nameField = new Text("Carrier :\n" + s);
        comp.getChildren().add(nameField);

        s = "is hit : " + (pc.get_battleship().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_battleship().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (pc.get_battleship().is_sinked() ? "yes\n" : "no\n");
        Text name1 = new Text("Battleship :\n" + s);
        comp.getChildren().add(name1);

        s = "is hit : " + (pc.get_cruiser().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_cruiser().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (pc.get_cruiser().is_sinked() ? "yes\n" : "no\n");
        Text name4 = new Text("Cruiser :\n" + s);
        comp.getChildren().add(name4);

        s = "is hit : " + (pc.get_submarine().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_submarine().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (pc.get_submarine().is_sinked() ? "yes\n" : "no\n");
        Text name3 = new Text("Submarine :\n" + s);
        comp.getChildren().add(name3);

        s = "is hit : " + (pc.get_destroyer().is_hit() ? "yes\n" : "no \n") + "iis not touched :" +
                (!pc.get_destroyer().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (pc.get_destroyer().is_sinked() ? "yes\n" : "no\n");
        Text name2 = new Text("Destroyer :\n" + s);
        comp.getChildren().add(name2);

        Scene stageScene = new Scene(comp, 300, 400);
        newStage.setTitle("Enemy's Ships");
        newStage.setScene(stageScene);
        newStage.show();

    }

    /**
     * This void makes a pop-up with player ships condition.
     * @param actionEvent
     * @throws IOException
     */
    public void ps_controller(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        String s;
        s = "is hit : " + (person.get_carrier().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_carrier().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (person.get_carrier().is_sinked() ? "yes\n" : "no\n");
        Text nameField = new Text("Carrier :\n" + s);
        comp.getChildren().add(nameField);

        s = "is hit : " + (person.get_battleship().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_battleship().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (person.get_battleship().is_sinked() ? "yes\n" : "no\n");
        Text name1 = new Text("Battleship :\n" + s);
        comp.getChildren().add(name1);

        s = "is hit : " + (person.get_cruiser().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_cruiser().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (person.get_cruiser().is_sinked() ? "yes\n" : "no\n");
        Text name4 = new Text("Cruiser :\n" + s);
        comp.getChildren().add(name4);

        s = "is hit : " + (person.get_submarine().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_submarine().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (person.get_submarine().is_sinked() ? "yes\n" : "no\n");
        Text name3 = new Text("Submarine :\n" + s);
        comp.getChildren().add(name3);

        s = "is hit : " + (person.get_destroyer().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_destroyer().is_hit() ? "yes\n" : "no\n") + " is sinked :" +
                (person.get_destroyer().is_sinked() ? "yes\n" : "no\n");
        Text name2 = new Text("Destroyer :\n" + s);
        comp.getChildren().add(name2);

        Scene stageScene = new Scene(comp, 300, 400);
        newStage.setScene(stageScene);
        newStage.setTitle("Player's Ships");
        newStage.show();

    }

    /**
     * This void makes a pop-up with Player's past moves.
     * @param actionEvent
     * @throws IOException
     */
    public void ppm_controller(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.getChildren().add(new Text("Last 5 Moves :"));

        for (int i = 0; i < Math.min(5,person.past_moves.size()); i++)
        {
            String s = "x : " + Integer.toString(person.past_moves.get(person.past_moves.size() - 1 - i).x)
                    + ", y : " + Integer.toString(person.past_moves.get(person.past_moves.size() - 1 - i).y)
                    + ", " + (person.past_moves.get(person.past_moves.size() - 1 - i).miss_or_hit ? "hit" : "miss")
                    + ", " + Integer.toString(person.past_moves.get(person.past_moves.size() - 1 - i).type);
            Text nameField = new Text(s);
            comp.getChildren().add(nameField);
        }

        String s = "Last line is the codename for each ship : \n 1 : carrier, 2 : Battleship, 3 : Cruiser, 4 : Submarine, 5 : Destroyer";
        Text nameField = new Text(s);
        comp.getChildren().add(nameField);

        Scene stageScene = new Scene(comp, 400, 300);
        newStage.setScene(stageScene);
        newStage.setTitle("Past Player Moves");
        newStage.show();

    }

    /**
     * This void makes a pop-up with enemy's last moves.
     * @param actionEvent
     * @throws IOException
     */
    public void epm_controller(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        comp.getChildren().add(new Text("Last 5 Moves :"));

        for (int i = 0; i < Math.min(5,pc.past_moves.size()); i++)
        {
            String s = "x : " + Integer.toString(pc.past_moves.get(pc.past_moves.size() - 1 - i).x)
                    + ", y : " + Integer.toString(pc.past_moves.get(pc.past_moves.size() - 1 - i).y)
                    + ", " + (pc.past_moves.get(pc.past_moves.size() - 1 - i).miss_or_hit ? "hit" : "miss")
                    + ", " + Integer.toString(pc.past_moves.get(pc.past_moves.size() - 1 - i).type);
            Text nameField = new Text(s);
            comp.getChildren().add(nameField);
        }

        String s = "Last line is the codename for each ship : \n 1 : carrier, 2 : Battleship, 3 : Cruiser, 4 : Submarine, 5 : Destroyer";
        Text nameField = new Text(s);
        comp.getChildren().add(nameField);

        Scene stageScene = new Scene(comp, 400, 300);
        newStage.setScene(stageScene);
        newStage.setTitle("Past Enemy Moves");
        newStage.show();

    }

}