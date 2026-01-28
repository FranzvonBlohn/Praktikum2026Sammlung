package org.praktikum.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Body {
    protected Vector2D pos;
    protected Vector2D vel = new Vector2D(0,0);

    protected boolean doPhysics;
    protected boolean doGravity;
    protected boolean doCollisions;

    protected double bounciness;
    protected double mass;

    protected boolean dead = false;

    protected Color color;

    public Body (double x,double y, boolean doPhysics, boolean doGravity, boolean doCollisions,double bounciness ,double mass, Color color) {

        this.pos = new Vector2D(x,y);

        this.doPhysics = doPhysics;
        this.doGravity = doGravity;
        this.doCollisions = doCollisions;

        this.bounciness = bounciness;
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

    public void special() {}

}
