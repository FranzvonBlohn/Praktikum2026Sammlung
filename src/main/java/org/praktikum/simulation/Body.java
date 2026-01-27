package org.praktikum.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Body {
    protected Vector2D pos = new Vector2D(500,500);
    protected Vector2D vel = new Vector2D(0,0);

    protected boolean doPhysics = false;
    protected boolean doGravity = false;
    protected boolean doCollisions = false;

    protected double bouncyness = 0;
    protected double mass = 1;

    protected boolean dead = false;

    protected Color color= Color.RED;

    public Body (double x,double y, boolean doPhysics, boolean doGravity, boolean doCollisions,double bouncyness,double mass, Color color) {

        this.pos.x = x;
        this.pos.y = y;

        this.doPhysics = doPhysics;
        this.doGravity = doGravity;
        this.doCollisions = doCollisions;

        this.bouncyness = bouncyness;
        this.mass = mass;

        this.color = color;
    }

    //Methode to be overriden by child methodes
    public void draw(GraphicsContext gc) {}

    public void gravity (Vector2D gravity) {
        this.vel.add(gravity);
    }

    public void updatePos () {
        this.pos.add(this.vel);
    }

    public void onCollision(Body other) {}

}
