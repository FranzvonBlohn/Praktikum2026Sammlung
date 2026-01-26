package org.praktikum.pong;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

// PongCircle class - represents a single circle
public class PongCircle {

    // Variables to store circle position and size
    protected double x;
    protected double y;
    protected double radius;
    protected double velX;
    protected double velY;

    protected double maxVel = 15;

    protected boolean dead;

    protected Color color;

    // Constructor - this runs when we create a new PongCircle
    public PongCircle(double x, double y, double velX, double velY, int radius, Color color, List<PongCircle> list) {
        this.x = x;
        this.y = y;
        this.radius = radius;

        this.dead = false;

        this.velX = velX;
        this.velY = velY;

        this.color = color;
        
        // Automatically add this circle to the list
        list.add(this);
    }

    // This method updates the circle
    public void update(List<PongCircle> pongCircleList,List<PongRectangle> pongRectangleList, double canvasWidth, double canvasHeight, PongRectangle p1Bat, PongRectangle p2Bat) {


        //delete ball if it is dead
        if (this.dead) {
            pongCircleList.remove(this);
            return;
        }

        //check for rectangle collisons
        circleRectangleCollision(pongRectangleList);

        //check for collisions with the border
        // Check left and right boundaries (account for radius)
        if (this.x - this.radius < 0) {
            this.velX = -this.velX;
            this.x = this.radius;

            resetBall(canvasWidth, canvasHeight,pongCircleList);
            p2Bat.score += 1;

        } else if (this.x + this.radius > canvasWidth) {
            this.velX = -this.velX;
            this.x = canvasWidth - this.radius;

            resetBall(canvasWidth, canvasHeight,pongCircleList);
            p1Bat.score += 1;

        }

        // Check top and bottom boundaries (account for radius)
        if (this.y - this.radius < 0) {
            this.velY = -this.velY;
            this.y = this.radius;


        } else if (this.y + this.radius > canvasHeight) {
            this.velY = -this.velY;
            this.y = canvasHeight - this.radius;
        }

        //limit movemnt if it is to big
        if (this.velY > maxVel) {
            this.velY = maxVel;
        }

        //apply movment
        this.x += this.velX;
        this.y += this.velY;
    }

    public void circleRectangleCollision(List<PongRectangle> pongRectangleList) {

        for (PongRectangle rectangle : pongRectangleList) {

            // closest point on the rect to the circle center
            double closestX = clamp(this.x, rectangle.x, rectangle.x + rectangle.width);
            double closestY = clamp(this.y, rectangle.y, rectangle.y + rectangle.height);

            double dx = this.x - closestX;
            double dy = this.y - closestY;
            if (dx*dx + dy*dy <= this.radius * this.radius) {

                this.velX = -this.velX;
                this.velY += rectangle.velY;
            }

        }

    }

    // Helper clamp used above
    private static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }


    private void resetBall(double canvasWidth, double canvasHeight, List<PongCircle> pongCircleList) {
        this.dead = true;
        new PongCircle(canvasWidth/2, canvasHeight/2,Math.random() < 0.5 ? -10 : 10,Math.random() < 0.5 ? -10 : 10, 25,Color.BLUE, pongCircleList);
    }

    // This method draws the circle on the canvas
    public void draw(GraphicsContext gc) {
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
