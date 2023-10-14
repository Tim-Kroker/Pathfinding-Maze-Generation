package pathfinding;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class Grid extends Canvas {

    private final Canvas self = this;
    private final GraphicsContext graphicsContext = this.getGraphicsContext2D();

    private double FOVX = -300;
    private double FOVY = -300;
    private double FOVPixelWidth = 600;
    private double FOVPixelHeight = 600;


    private double boxPixelWidth = 5;
    private int gridWidth = 49;
    private int gridHeight = 49;

    private double clickX, clickY;

    private boolean[][] walls;

    public Grid(Stage stage) {
        walls = new boolean[gridWidth][gridHeight];
        MazeGenerator.insertMaze(walls,0,0,33,33);

        /* Resizing */
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                FOVPixelWidth = t1.doubleValue();
                self.setWidth(FOVPixelWidth);
                repaint();
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                FOVPixelHeight = t1.doubleValue();
                self.setHeight(FOVPixelHeight);
                repaint();
            }
        });

        /* Dragging */
        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                clickX = mouseEvent.getX();
                clickY = mouseEvent.getY();
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double diffX = clickX - mouseEvent.getX();
                FOVX += diffX;
                clickX = mouseEvent.getX();

                double diffY = clickY - mouseEvent.getY();
                FOVY += diffY;
                clickY = mouseEvent.getY();

                repaint();
            }
        });

        /* Scroll Listener */
        this.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if(scrollEvent.getDeltaY() != 0.0) {
                    boxPixelWidth *= (scrollEvent.getDeltaY() < 0) ? 0.9 : 1.1;
                    if(boxPixelWidth > 50) boxPixelWidth = 50;
                    else if(boxPixelWidth < 1) boxPixelWidth = 1;
                    System.out.println(boxPixelWidth);
                    repaint();
                }
            }
        });
    }

    private void repaint() {
        drawBackground();
        drawGrid();
        drawBlocks();
    }

    private void drawBackground() {
        graphicsContext.setFill(new Color(0.10,0.10,0.12,1));
        graphicsContext.fillRect(0,0,FOVPixelWidth,FOVPixelHeight);
    }
    private void drawGrid() {
        graphicsContext.setStroke(new Color(0.6, 1, 1 , boxPixelWidth * 0.01 + 0.1));
        double x = (int)(FOVX / boxPixelWidth) * boxPixelWidth; //round to next multiple of 20
        if(x < 0) x = 0;
        double rightBorder = FOVPixelWidth + FOVX;
        if(rightBorder > gridWidth * boxPixelWidth) rightBorder = gridWidth * boxPixelWidth;

        while(x < rightBorder + 0.1) {
            graphicsContext.strokeLine(x-FOVX, 0-FOVY,x-FOVX, gridWidth * boxPixelWidth - FOVY);
            x += boxPixelWidth;
        }

        double y = (int)(FOVY / boxPixelWidth) * boxPixelWidth; //round to next multiple of 20
        if(y < 0) y = 0;
        double bottomBorder = FOVPixelHeight + FOVY;
        if(bottomBorder > gridHeight * boxPixelWidth) bottomBorder = gridHeight * boxPixelWidth;
        while(y < bottomBorder + 0.1) {
            graphicsContext.strokeLine(0-FOVX,y-FOVY, gridWidth * boxPixelWidth -FOVX,y-FOVY);
            y += boxPixelWidth;
        }
    }
    private void drawBlocks() {
        //TODO: Calculate which x and y are relevant
        graphicsContext.setFill(new Color(0.22,0.18, 0.22, 1));
        for(int x = 0; x < gridWidth; x++) {
            for(int y = 0; y < gridHeight; y++) {
                if(walls[x][y]) {
                    int blockPixelX = (int)(x * boxPixelWidth);
                    int blockPixelY = (int)(y * boxPixelWidth);
                    if (blockPixelX < FOVX + FOVPixelWidth && blockPixelX + boxPixelWidth > FOVX && blockPixelY < FOVY + FOVPixelHeight && blockPixelY + boxPixelWidth > FOVY) {
                        graphicsContext.fillRect(blockPixelX - FOVX, blockPixelY - FOVY, boxPixelWidth, boxPixelWidth);
                    }

                }
            }
        }
    }
}
