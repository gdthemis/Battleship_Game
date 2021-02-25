package sample.controller;

import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.model.Cell;
import sample.model.Grid;
import sample.model.Player;
import sample.model.Quartet;


import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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
    private int _old_x;
    private int _old_y;

    private char[][] _player;
    private char[][] _enemy;
    public Player person, pc;
    private boolean has_started = false;
    private boolean _first_move = false;
    private int random;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Grid player = new Grid();
        Grid computer = new Grid();

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
    public void submit_move_controller(javafx.event.ActionEvent e){
        if (!has_started)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You should Start the Game first");
            alert.showAndWait();
            return;
        }


//        if (random == 0) {
            update(Integer.parseInt(row_id.getText().toString()), Integer.parseInt(column_id.getText().toString()));
            update_pc();

            if (pc.remaining_hits == 0)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Your moves ended, player wins");
                alert.setContentText("Player Won");
                alert.showAndWait();

                javafx.application.Platform.exit();
            }

        if (person.remaining_hits == 0)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Your moves ended, pc wins");
            alert.setContentText("Pc Won");
            alert.showAndWait();

            javafx.application.Platform.exit();
        }

        if (person.points == 5200)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You won");
            alert.setContentText("You sinked all ships, so you are the winner!");
            alert.showAndWait();

            javafx.application.Platform.exit();
        }

        if (pc.points == 5200)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You lost");
            alert.setContentText("Pc sinked all your ships, so you lose!");
            alert.showAndWait();

            javafx.application.Platform.exit();
        }
    }

    @FXML
    public void start_controller(javafx.event.ActionEvent e){
        if (has_started)
        {
            person = new Player();
            pc = new Player();

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    player_table.add((person.my_grid.get_cell(i, j)), j, i);
                    enemy_table.add((person.shooting_grid.get_cell(i, j)), j, i);
                }
            }
        }

            random= ThreadLocalRandom.current().nextInt(0, 3);
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
    }

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

    private void update_pc() {

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
        }
        else {
            person.update_after_hit_enemy(x, y, false);
        }

//        float ratio
        pc.past_moves.add(new Quartet(x, y, res == 0 ? false : true, res));
        comp_ratio.setText(Double.toString((1.0*pc.hits/(40 - pc.remaining_hits))*100) + " %");
    }


    @FXML
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

    }

    private char[][] reader (String text, String decider) throws Exception {
        String path = "/Users/georgiosthemelis/IdeaProjects/proj3/project10/src/sample/scenario_" + text;

        final File input = new File(path + decider);
        final BufferedReader br = new BufferedReader(new FileReader(input));
        String temp;
        final char[][] world = new char[11][11];
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

    public void enemy_ships_controller(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        String s;
        s = "is hit : " + (pc.get_carrier().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_carrier().is_hit() ? "yes\n" : "no\n");
        Text nameField = new Text("Carrier :\n" + s);
        comp.getChildren().add(nameField);

        s = "is hit : " + (pc.get_battleship().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_battleship().is_hit() ? "yes\n" : "no\n");
        Text name1 = new Text("Battleship :\n" + s);
        comp.getChildren().add(name1);

        s = "is hit : " + (pc.get_cruiser().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_cruiser().is_hit() ? "yes\n" : "no\n");
        Text name4 = new Text("Cruiser :\n" + s);
        comp.getChildren().add(name4);

        s = "is hit : " + (pc.get_submarine().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!pc.get_submarine().is_hit() ? "yes\n" : "no\n");
        Text name3 = new Text("Submarine :\n" + s);
        comp.getChildren().add(name3);

        s = "is hit : " + (pc.get_destroyer().is_hit() ? "yes\n" : "no \n") + "iis not touched :" +
                (!pc.get_destroyer().is_hit() ? "yes\n" : "no\n");
        Text name2 = new Text("Destroyer :\n" + s);
        comp.getChildren().add(name2);

        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();

    }

    public void ps_controller(ActionEvent actionEvent) throws IOException {
        Stage newStage = new Stage();
        VBox comp = new VBox();
        String s;
        s = "is hit : " + (person.get_carrier().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_carrier().is_hit() ? "yes\n" : "no\n");
        Text nameField = new Text("Carrier :\n" + s);
        comp.getChildren().add(nameField);

        s = "is hit : " + (person.get_battleship().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_battleship().is_hit() ? "yes\n" : "no\n");
        Text name1 = new Text("Battleship :\n" + s);
        comp.getChildren().add(name1);

        s = "is hit : " + (person.get_cruiser().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_cruiser().is_hit() ? "yes\n" : "no\n");
        Text name4 = new Text("Cruiser :\n" + s);
        comp.getChildren().add(name4);

        s = "is hit : " + (person.get_submarine().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_submarine().is_hit() ? "yes\n" : "no\n");
        Text name3 = new Text("Submarine :\n" + s);
        comp.getChildren().add(name3);

        s = "is hit : " + (person.get_destroyer().is_hit() ? "yes\n" : "no \n") + "is not touched :" +
                (!person.get_destroyer().is_hit() ? "yes\n" : "no\n");
        Text name2 = new Text("Destroyer :\n" + s);
        comp.getChildren().add(name2);

        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();

    }

    public void ppm_controller(ActionEvent actionEvent) throws IOException {
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

        Scene stageScene = new Scene(comp, 300, 300);
        newStage.setScene(stageScene);
        newStage.show();

    }

}