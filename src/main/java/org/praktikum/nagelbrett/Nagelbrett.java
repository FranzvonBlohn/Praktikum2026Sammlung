package org.praktikum.nagelbrett;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Nagelbrett extends Application {

    // These lists stores all circles that have been created
    public List<PongCircle> pongCircleList = new ArrayList<>();
    public List<PhysicPongCircle> physicPongCircleList = new ArrayList<>();

    //Create Basic Simulation Parameters
    public double gravity = 7;

    // We need a GraphicsContext to draw with
    private GraphicsContext gc;

    private double dt; // Time between frames for consistent physics calculations
    private long lastTime; // Store the last update time

    private double windowWidth;
    private double windowHeight;

    Rectangle2D visual = Screen.getPrimary().getVisualBounds();
    {
        windowWidth = visual.getWidth() - 100;
        windowHeight = visual.getHeight() - 100;
    }

    private double canvasWidth;
    private double canvasHeight;

    @Override
    public void start(Stage stage) {

        // Create canvas where we draw everything
        Canvas canvas = new Canvas(windowWidth, windowHeight);
        gc = canvas.getGraphicsContext2D();

        // Create a container and scene
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, windowWidth, windowHeight);

        // Bind the canvas size to the container so it resizes with the window
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        // initialize cached canvas dimensions
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        //call function to create nagelbrett traingle
        makeTriangle(canvasWidth, canvasHeight, 10);

        // When user clicks mouse, create a new circle
        canvas.setOnMouseClicked(e -> {
            // Check if left mouse button was clicked
            if (e.getButton() == MouseButton.PRIMARY) {
                // Create new circle - it adds itself to pongCircleList automatically
                new PongCircle(e.getX(), e.getY(), 10, Color.BLUE, pongCircleList);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                // Create new circle - it adds itself to physicPongCircleList automatically
                new PhysicPongCircle(canvasWidth/2 - (((int)(Math.random() * 2)*2)-1), 0, 10, Color.BLACK, physicPongCircleList, pongCircleList);
            }
        });

        // Optionally update cached canvas size when it changes (not strictly necessary if you always use canvas.getWidth())
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> canvasWidth = newVal.doubleValue());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> canvasHeight = newVal.doubleValue());

        // This timer continuously runs ~60 FPS
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update cached canvas size (in case bindings haven't fired yet)
                canvasWidth = canvas.getWidth();
                canvasHeight = canvas.getHeight();

                // Calculate dt using the provided 'now' value
                if (lastTime == 0) {
                    dt = 0;
                } else {
                    dt = (now - lastTime) / 1_000_000_000.0; // seconds
                }
                lastTime = now;

                // Update and draw
                updateAll();
                drawAll();
            }
        };

        lastTime = 0;
        timer.start();

        //Declare the timer to spawn physics circles periodically
        Timer circleTimer = new Timer();
        circleTimer.scheduleAtFixedRate(new TimerTask() {
                                            @Override
                                            public void run() {
                                                new PhysicPongCircle(canvasWidth/2 - (((int)(Math.random() * 2)*2)-1), 0, 10, Color.BLACK, physicPongCircleList, pongCircleList);
                                            }
                                        },
                0,
                1000);

        // Set up and show the window
        stage.setTitle("nagelbrett");
        stage.setScene(scene);
        stage.show();
    }

    // This method updates all
    private void updateAll() {
        for (PhysicPongCircle circle : physicPongCircleList) {
            circle.update(dt, gravity, pongCircleList, physicPongCircleList, canvasWidth, canvasHeight);
        }
    }

    // This method draws all
    private void drawAll() {
        // Clear the canvas using the current canvas size
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Then, draw each circle in the list
        for (PongCircle circle : pongCircleList) {
            circle.draw(gc, pongCircleList);
        }
    }

    private void makeTriangle(double canvasWidth, double canvasHeight, int triangleRows) {
        double triangleSpacing = (canvasHeight * 0.4) / triangleRows;

        for (int i = 0; i <= triangleRows; i++)
            for (int c = 0; c < i; c++) {
                new PongCircle(((canvasWidth - i * triangleSpacing) / 2) + (c * triangleSpacing) + 0.5 * triangleSpacing, (triangleSpacing * i), 5, Color.TEAL, pongCircleList);
            }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

