package org.praktikum.simulation;

import javafx.scene.paint.Color;

public class Border extends Rectangle{
    public Border(double x, double y, boolean doPhysics, boolean doGravity, boolean doCollisions, double bounciness, double mass, Color color, double width, double height) {
        super(x, y, doPhysics, doGravity, doCollisions, bounciness, mass, color, width, height);
    }

    public void onCollision() {
        //some logic that prevents the ball from clipping thrue the floor if(the collison code is to bad to handle it itself)
    };
}
