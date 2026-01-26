package org.praktikum.simulation;

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

public class Simulation extends Application {

    // We need a GraphicsContext to draw with
    private GraphicsContext gc;

    //canvas width and height for everything
    private double canvasWidth = 800;
    private double canvasHeight = 500;

    private double dt; // Time between frames for consistent physics calculations
    private long lastTime;// Store the last update time;

    @Override
    public void start(Stage stage) {

        // Create canvas where we draw everything
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        gc = canvas.getGraphicsContext2D();

        // Create a container and scene
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, canvasWidth, canvasHeight);

        // Bind the canvas size to the container so it resizes with the window
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        // Optionally update cached canvas size when it changes (not strictly necessary if you always use canvas.getWidth())
        canvas.widthProperty().addListener((obs, oldVal, newVal) -> canvasWidth = newVal.doubleValue());
        canvas.heightProperty().addListener((obs, oldVal, newVal) -> canvasHeight = newVal.doubleValue());

        // This timer continuously runs ~60 FPS
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

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

        //enable fullscreen
        stage.setFullScreen(true);
        // Set up and show the window
        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.show();
    }

    // This method updates all
    private void updateAll() {

    }

    // This method draws all
    private void drawAll() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
    }


    public static void main(String[] args) {
        launch(args);
    }

}

