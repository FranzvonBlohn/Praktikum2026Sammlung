package org.praktikum.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Rectangle extends Body{
    protected double width = 10;
    protected double height = 10;

    public Rectangle(double x,double y, boolean doPhysics, boolean doGravity, boolean doCollisions,double bouncyness,double mass, Color color, double width, double height) {
        super(x,y,doPhysics, doGravity, doCollisions,bouncyness,mass, color);

        this.width = width;
        this.height = height;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(this.color);
        //draw ze Rectangle Ja
        gc.fillRect(this.pos.x, this.pos.y, this.width, this.height);
    }

}
