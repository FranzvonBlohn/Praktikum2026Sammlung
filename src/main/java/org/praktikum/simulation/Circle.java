package org.praktikum.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Circle extends Body{
    protected double radius;

    public Circle(double x,double y, boolean doPhysics, boolean doGravity, boolean doCollisions,double bounciness,double mass, Color color, double radius) {
        super(x,y,doPhysics, doGravity, doCollisions,bounciness,mass, color);

        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {

        gc.setFill(this.color);
        // Draw a filled oval (circle)
        gc.fillOval(
                this.pos.x - this.radius,
                this.pos.y - this.radius,
                this.radius * 2,
                this.radius * 2
        );
    }

    @Override
    public void onCollision (Body other) {
        this.color = Color.BLUE;
        super.onCollision(other);
    }
    }
