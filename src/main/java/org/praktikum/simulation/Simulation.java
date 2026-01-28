package org.praktikum.simulation;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class Simulation extends Application {

    // We need a GraphicsContext to draw with
    private GraphicsContext gc;

    //canvas width and height for everything
    private double canvasWidth = 800;
    private double canvasHeight = 500;

    private double dt; // Time between frames for consistent physics calculations
    private long lastTime;// Store the last update time;

    //store all bodys
    public List<Body> bodys = new ArrayList<>();
    public List<Body> walls = new ArrayList<>();


    //create gravity
    public Vector2D gravity = new Vector2D(0,2);

    @Override
    public void start(Stage stage) {

        // Stuff for the canvas
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

        //Inputs
                //mouse input
                canvas.setOnMouseClicked(e -> {

                    if (e.getButton() == MouseButton.PRIMARY) {
                        bodys.add(new Circle(e.getX(),e.getY(),true,false,true,0,1, Color.YELLOW, 50));
                    }
                    if (e.getButton() == MouseButton.SECONDARY) {
                        bodys.add(new Rectangle(e.getX(),e.getY(),true,false,true,0,1, Color.GREEN, 50, 50));
                    }

                });

                // get keyboard input
                scene.setOnKeyPressed((KeyEvent e) -> {
                    KeyCode code = e.getCode();
                    switch (code) {
                        case SPACE:
                            for (Body body : bodys) body.dead = true;
                    }


                });

        // Create game LOOP
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

        // create other objects

        //create walls !Objects who dont do collision ignore the walls But i have not yet decided if this is a bug or a feature!
        int wallWidth = 1000;

        Rectangle wallTop = new Rectangle(0 ,- wallWidth,false,false,true,0,1, Color.GREEN, canvasWidth, wallWidth); //top border
        Rectangle wallLeft = new Rectangle( - wallWidth,0,false,false,true,0,1, Color.GREEN, wallWidth, canvasHeight); //Left border
        Rectangle wallBottom = new Rectangle(0,canvasHeight,false,false,true,0,1, Color.GREEN,canvasWidth, wallWidth); //bottom border
        Rectangle wallRight = new Rectangle(canvasWidth,0,false,false,true,0,1, Color.GREEN, wallWidth, canvasHeight); //right border

        walls.add(wallTop); bodys.add(wallTop);
        walls.add(wallLeft); bodys.add(wallLeft);
        walls.add(wallRight);   bodys.add(wallRight);
        walls.add(wallBottom);  bodys.add(wallBottom);


    }

    // This method updates all
    private void updateAll() {

        for (int i = 0; i < bodys.size(); i++) {
            //kill self if it is dead
            if (bodys.get(i).dead) {
                bodys.remove(i);
                return;
            }


            //only update the ball if it does Physiks

                //apply gravity
                if (bodys.get(i).doGravity) {
                    bodys.get(i).gravity(gravity);
                }

                //check collions
                for (int other = i; other < bodys.size(); other++) {

                    //skip collisions with self
                    if (i == other) {
                        continue;
                    }

                    handelCollision(bodys.get(i),bodys.get(other));

                }

                //function for special behavor that get called every frame
                bodys.get(i).special();

                //update the position
                bodys.get(i).updatePos();


        }

    }

    // This method draws all
    private void drawAll() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        for (Body body : bodys) {
            body.draw(gc);
        }

    }

    public void handelCollision(Body a,Body b) {

        if ((a instanceof Circle) && (b instanceof Circle)) {
            //circle circle collision
            resolveCircleCircle( (Circle) a, (Circle) b);

        } else if (((a instanceof Circle) && (b instanceof Rectangle))) {
            //circle rectangle collision
            resolveRectangleCircle((Rectangle) b, (Circle) a);

        } else if (((a instanceof Rectangle) && (b instanceof Circle))) {
            //rectangle circle collision
            resolveRectangleCircle((Rectangle) a, (Circle) b);

        } else if (((a instanceof Rectangle) && (b instanceof Rectangle))) {
            //rectangle rectangle collision
            resolveRectangleRectangle((Rectangle) a, (Rectangle) b);

        }

    }

    public void resolveCircleCircle(Circle a,Circle b) {

        //detect collisions
        if (((a.doCollisions) && (b.doCollisions)) || (walls.contains(a) || walls.contains(b))) {
            a.onCollision(b);
            b.onCollision(a);

            //only do collision if both a and b do collision or if a or b is a wall
            if ((a.doCollisions) && (b.doCollisions)){
                //calculate the collision

                //apply the movment if the bodies are not static (doPhysiks)
                if (a.doPhysics) {
                    //changes to a
                }

                if (b.doPhysics) {
                    //changes to b
                }

            }

        }
    }

    public void resolveRectangleRectangle(Rectangle a, Rectangle b) {

        //detect collisions using aabb
        if (a.pos.x < b.pos.x + b.width &&
                a.pos.x + a.width > b.pos.x &&
                a.pos.y < b.pos.y + b.height &&
                a.pos.y + a.height > b.pos.y) {


            a.onCollision(b);
            b.onCollision(a);

            //only do collision if both a and b do collision or if a or b is a wall
            if (((a.doCollisions) && (b.doCollisions)) || (walls.contains(a) || walls.contains(b))) {

                //calculate the collision

                //apply the movment if the bodies are not static (doPhysiks)
                if (a.doPhysics) {
                    //changes to a
                }

                if (b.doPhysics) {
                    //changes to b
                }


            }
        }
    }


    public void resolveRectangleCircle(Rectangle a, Circle b) {

        //detect collisions using Blackmagic (fincding closet point on rect to circle and checking if it is within radius)
        if ( Math.pow(b.pos.x - Math.max(a.pos.x, Math.min(b.pos.x, a.pos.x + a.width)), 2) +
                Math.pow(b.pos.y - Math.max(a.pos.y, Math.min(b.pos.y, a.pos.y + a.height)), 2)
                <= b.radius * b.radius ) {

            a.onCollision((Body) b);
            b.onCollision((Body) a);

            //only do collision if both a and b do collision or if a or b is a wall
            if (((a.doCollisions) && (b.doCollisions)) || (walls.contains(a) || walls.contains(b))) {
                //calculate the collision

                //apply the movment if the bodies are not static (doPhysiks)
                if (a.doPhysics) {
                    //changes to a
                }

                if (b.doPhysics) {
                    //changes to b
                }
            }
        }
    }




    public static void main(String[] args) {
        launch(args);
    }

}

