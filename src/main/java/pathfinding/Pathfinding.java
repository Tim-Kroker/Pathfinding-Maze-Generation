package pathfinding;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Pathfinding extends Application {

    private double width = 600;
    private double height = 600;

    private Grid grid;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Pathfinding");
        grid = new Grid(stage);

        Pane root = new Pane();
        stage.setScene(new Scene(root, width, height));

        grid.setWidth(width);
        grid.setHeight(height);

        root.getChildren().add(grid);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}