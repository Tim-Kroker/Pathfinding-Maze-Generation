package pathfinding;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Pathfinding extends Application {

    private double width = 1000;
    private double height = 800;

    private Grid grid;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Pathfinding");
        grid = new Grid(stage);

        StackPane root = new StackPane();
        Scene scene = new Scene(root,width,height);
        stage.setScene(scene);

        URL url = this.getClass().getResource("/style.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
            System.exit(-1);
        }
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);

        grid.setWidth(width);
        grid.setHeight(height);

        root.getChildren().add(grid);
        root.setAlignment(Pos.TOP_RIGHT);
        root.getChildren().add(getEditOptions());

        stage.show();
    }

    private Region getEditOptions() {
        TilePane options = new TilePane();
        options.setPrefColumns(1);
        options.setVgap(10);
        options.setPadding(new Insets(10));

        ToggleGroup menu = new ToggleGroup();
        RadioButton drag = new RadioButton("DRAG");
        RadioButton delete = new RadioButton("DELETE");
        RadioButton addWall = new RadioButton("WALL");
        RadioButton addMaze = new RadioButton("MAZE");
        RadioButton addStart = new RadioButton("START");
        RadioButton addGoal = new RadioButton("GOAL");
        drag.getStyleClass().remove("radio-button");
        delete.getStyleClass().remove("radio-button");
        addWall.getStyleClass().remove("radio-button");
        addMaze.getStyleClass().remove("radio-button");
        addStart.getStyleClass().remove("radio-button");
        addGoal.getStyleClass().remove("radio-button");
        drag.setToggleGroup(menu);
        delete.setToggleGroup(menu);
        addWall.setToggleGroup(menu);
        addMaze.setToggleGroup(menu);
        addStart.setToggleGroup(menu);
        addGoal.setToggleGroup(menu);
        drag.setSelected(true);
        menu.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if(t1 instanceof RadioButton) {
                    RadioButton button = (RadioButton)(t1);
                    String label = button.getText();
                    EditOption editOption = switch (label) {
                        case "DELETE" -> EditOption.DELETE;
                        case "WALL" -> EditOption.WALL;
                        case "MAZE" -> EditOption.MAZE;
                        case "START" -> EditOption.START;
                        case "GOAL" -> EditOption.GOAL;
                        default -> EditOption.DRAG;
                    };
                    grid.setCurrentEditOption(editOption);
                }
            }
        });

        options.getChildren().addAll(drag, delete, addWall, addMaze, addStart, addGoal);

        options.setMaxWidth(Control.USE_PREF_SIZE);
        options.setMaxHeight(Control.USE_PREF_SIZE);

        return options;
    }

    public static void main(String[] args) {
        launch();
    }

}