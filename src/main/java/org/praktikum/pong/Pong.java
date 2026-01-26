package org.praktikum.pong;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Pong extends Application {

    // These lists stores all circles that have been created
    public List<PongCircle> pongCircleList = new ArrayList<>();
    public List<PongRectangle> pongRectangleList = new ArrayList<>();


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

        // When user clicks mouse, create a new circle
        canvas.setOnMouseClicked(e -> {
            // Check if left mouse button was clicked
            if (e.getButton() == MouseButton.PRIMARY) {
                // Create new circle - it adds itself to pongCircleList automatically
                new PongCircle(e.getX(), e.getY(), 10, 0, 10, Color.BLUE, pongCircleList);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                new PongCircle(canvasWidth/2, canvasHeight/2,Math.random() < 0.5 ? -10 : 10,Math.random() < 0.5 ? -10 : 10, 25,Color.BLUE, pongCircleList);
            }
        });


        //generte the two bats
        PongRectangle p1Bat = new PongRectangle(0, canvasHeight / 2, 15, 250, 0, 0, Color.BLACK, pongRectangleList);
        PongRectangle p2Bat = new PongRectangle(canvasWidth, canvasHeight / 2, 15, 250, 0, 0, Color.BLACK, pongRectangleList);


        double batSpeed = 25;

        // get keyboard input
        scene.setOnKeyPressed((KeyEvent e) -> {
            KeyCode code = e.getCode();
            switch (code) {
                case SPACE:
                    for (PongCircle circle : pongCircleList) {
                        circle.dead = true;
                    }
                    break;
                case UP:
                    p2Bat.velY = -batSpeed;
                    break;
                case DOWN:
                    p2Bat.velY = batSpeed;
                    break;

                case W:
                    p1Bat.velY = -batSpeed;
                    break;
                case S:
                    p1Bat.velY = batSpeed;
                    break;
            }


        });

        //reset momentum when keys are realeased
        scene.setOnKeyReleased((KeyEvent e) -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.UP || code == KeyCode.DOWN) {
                p2Bat.velY = 0;
            }

            if (code == KeyCode.S || code == KeyCode.W) {
                p1Bat.velY = 0;
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
                updateAll(p1Bat, p2Bat);
                drawAll(p1Bat, p2Bat);
            }
        };

        lastTime = 0;
        timer.start();

        //enable fullscreen
        stage.setFullScreen(true);
        // Set up and show the window
        stage.setTitle("Nagelbrett");
        stage.setScene(scene);
        stage.show();
    }

    // This method updates all
    private void updateAll(PongRectangle p1Bat, PongRectangle p2Bat) {
        for (PongCircle circle : pongCircleList) {
            circle.update(pongCircleList, pongRectangleList, canvasWidth, canvasHeight, p1Bat, p2Bat);
        }

        for (PongRectangle rectangle : pongRectangleList) {
            rectangle.update(pongRectangleList, canvasWidth, canvasHeight);
        }
    }

    // This method draws all
    private void drawAll(PongRectangle p1Bat, PongRectangle p2Bat) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        for (PongCircle circle : pongCircleList) {
            circle.draw(gc);
        }

        for (PongRectangle rectangle : pongRectangleList) {
            rectangle.draw(gc);
        }

        // Draw text
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(50));

        gc.fillText(p1Bat.score + " - " + p2Bat.score, canvasWidth / 2 - 80, 50);
    }


    public static void main(String[] args) {
        launch(args);
    }

}

