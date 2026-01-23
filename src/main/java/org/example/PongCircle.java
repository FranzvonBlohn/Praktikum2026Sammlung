package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

// PongCircle class - represents a single circle
public class PongCircle {
    // Variables to store circle position and size
    // Use 'protected' so child classes can access them
    protected double x;
    protected double y;
    protected double radius;
    protected double bouncyness;
    protected double velX;
    protected double velY;

    protected boolean dead;
    protected boolean infected;

    protected Color color;

    // Constructor - this runs when we create a new PongCircle
    public PongCircle(double x, double y, int radius, Color color, List<PongCircle> list) {
        this.x = x;
        this.y = y;
        this.radius = radius;

        this.dead = false;
        this.infected = false;

        this.bouncyness = 0.5;

        this.velX = 0;
        this.velY = 0;
        // Set the color to blue
        this.color = color;
        
        // Automatically add this circle to the list
        list.add(this);
    }

    // This method draws the circle on the canvas
    public void draw(GraphicsContext gc, List<PongCircle> pongList) {

        //delete ball if it is dead
        if (this.dead) {
            pongList.remove(this);
            return;
        }

        // Set the color to black
        gc.setFill(this.color);
        
        // Draw a filled oval (circle)
        gc.fillOval(
            this.x - this.radius,
            this.y - this.radius,
            this.radius * 2,
            this.radius * 2
        );

    }
}
