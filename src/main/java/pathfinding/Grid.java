package pathfinding;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

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

    private EditOption currentEditOption = EditOption.DRAG;
    private Vec2D selectedCell = null;

    public void setCurrentEditOption(EditOption editOption) {
        this.currentEditOption = editOption;
    }

    private Optional<Vec2D> getCellAtPosition(int x, int y) {
        int cellX = (int)((x + FOVX)/boxPixelWidth);
        int cellY = (int)((y + FOVY)/boxPixelWidth);
        if(cellX >= 0 && cellX < gridWidth && cellY >= 0 && cellY < gridHeight) return Optional.of(new Vec2D(cellX, cellY));
        else return Optional.empty();
    }

    public Grid(Stage stage) {
        walls = new boolean[gridWidth][gridHeight];
        MazeGenerator.insertMaze(walls,0,0,3,3);

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

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                clickX = mouseEvent.getX();
                clickY = mouseEvent.getY();

                // Action to perform except dragging
                if(mouseEvent.getButton() == MouseButton.PRIMARY && currentEditOption != EditOption.DRAG) {
                    getCellAtPosition((int)clickX,(int)clickY).ifPresent(vec -> selectedCell = vec);
                }
            }
        });

        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                    switch(currentEditOption) {
                        case GOAL:
                        case WALL:
                        case START:
                            getCellAtPosition((int)mouseEvent.getX(),(int)mouseEvent.getY())
                                    .ifPresent(vec -> fillArea(true, selectedCell.x(), selectedCell.y(), vec.x(), vec.y()));
                            break;
                        case DELETE:
                            getCellAtPosition((int)mouseEvent.getX(),(int)mouseEvent.getY())
                                    .ifPresent(vec -> fillArea(false, selectedCell.x(), selectedCell.y(), vec.x(), vec.y()));
                            break;
                        case MAZE:
                            getCellAtPosition((int)mouseEvent.getX(),(int)mouseEvent.getY())
                                    .ifPresent(vec -> MazeGenerator.insertMaze(walls, selectedCell.x(), selectedCell.y(), Math.abs(selectedCell.x() - vec.x()), Math.abs(selectedCell.y() - vec.y())));
                        default:
                    }
                    selectedCell = null;
                    repaint();
                }
            }
        });

        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Dragging
                if(currentEditOption == EditOption.DRAG || mouseEvent.getButton() == MouseButton.SECONDARY) {
                    double diffX = clickX - mouseEvent.getX();
                    FOVX += diffX;
                    clickX = mouseEvent.getX();

                    double diffY = clickY - mouseEvent.getY();
                    FOVY += diffY;
                    clickY = mouseEvent.getY();

                    repaint();
                }

                //Drag area that has to be edited
                if(mouseEvent.getButton() == MouseButton.PRIMARY && currentEditOption != EditOption.DRAG) {

                }
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
                    repaint();
                }
            }
        });
    }

    private void fillArea(boolean fill, int x0, int y0, int x1, int y1) {
        if(x1 < x0) {
            int temp = x0;
            x0 = x1;
            x1 = temp;
        }
        if(y1 < y0) {
            int temp = y0;
            y0 = y1;
            y1 = temp;
        }
        for(int x = x0; x <= x1; x++) {
            for(int y = y0; y <= y1; y++) {
                walls[x][y] = fill;
            }
        }
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
