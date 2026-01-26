package org.praktikum.pong;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

// PongRectangle class - represents a single circle
public class PongRectangle {
    // Variables to store circle position and size
    protected double x;
    protected double y;
    protected double width;
    protected  double height;
    protected double velX;
    protected double velY;

    protected boolean dead;

    protected Color color;

    protected int score = 0;

    // Constructor - this runs when we create a new PongCircle
    public PongRectangle(double x, double y,double width, double height, double velX, double velY, Color color, List<PongRectangle> list) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.dead = false;

        this.velX = velX;
        this.velY = velY;

        this.color = color;
        
        // Automatically add this rectangle to the list
        list.add(this);
    }

    // This method updates the circle
    public void update(List<PongRectangle> pongRectangleList, double canvasWidth, double canvasHeight) {

        //delete ball if it is dead
        if (this.dead) {
            pongRectangleList.remove(this);
            return;
        }

        //apply movment
        this.x += this.velX;
        this.y += this.velY;


        //clamp bats to the border if it is in the center
        if ((this.x != 0) && (this.x != this.width)) {
            if (this.x < canvasWidth/2) {
                this.x = 0;
            } else if (this.x > canvasWidth/2) {
                this.x = canvasWidth- this.width;
            }
        }

        //check for collisions with the border
        if (this.x < 0) {
            this.x = 0;

        } else if (this.x + this.width > canvasWidth) {
            this.x = canvasWidth - this.width;

        }

        // Check top and bottom boundaries (account for radius)
        if (this.y < 0) {
            this.y = 0;

        } else if (this.y + this.height > canvasHeight) {
            this.y = canvasHeight - this.height;

        }
    }

    // This method draws the circle on the canvas
    public void draw(GraphicsContext gc) {

        gc.setFill(this.color);
        //draw ze Rectangle Ja
        gc.fillRect(this.x, this.y, this.width, this.height);

    }
}
