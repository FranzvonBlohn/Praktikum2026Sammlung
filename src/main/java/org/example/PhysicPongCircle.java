package org.example;

import javafx.scene.paint.Color;
import java.util.List;

import static javafx.scene.paint.Color.*;

// PhysicPongCircle class - extends PongCircle and creates black circles
public class PhysicPongCircle extends PongCircle {
     
    protected boolean physicsDead;


    // Constructor - this runs when we create a new PhysicPongCircle
    public PhysicPongCircle(double x, double y, int radius, Color color, List<PhysicPongCircle> list, List<PongCircle> pongList) {
        // Call the parent (PongCircle) constructor
        super(x, y, radius, color, pongList);
        
        this.physicsDead = false;

        // Automatically add this circle to the PhysicPongCircle list for physics updates
        list.add(this);
    }

    // This method updates the circle
    public void update(double dt, double gravity, List<PongCircle> pongList, List<PhysicPongCircle> physicPongList, double canvasWidth, double canvasHeight) {

        //kill ball if it is physics dead
        if (this.dead) {
            this.physicsDead = true;
        }

        //check if infected
        if (this.infected) {
            this.color = GREEN;
            this.physicsDead = true;
        }



        //delete ball if it is physics dead
        if (this.physicsDead) {
            this.velX = 0;
            this.velY = 0;

            physicPongList.remove(this);
            return;
        }


        
        
        //move circles downward according to gravity
        this.velY += gravity * dt;

        //check for circle circle collision
        circleCircleCollisions(pongList);


        //optional remove gravitational accelerating if ball is to fast

        if (this.velY >= gravity*0.6){
            this.velX = this.velX /2;
        }



        //apply movment
        this.x += this.velX;
        this.y += this.velY;


        //check for collisions with the border
        // Check left and right boundaries (account for radius)
        if (this.x - this.radius < 0) {
            this.velX = -this.velX * this.bouncyness;
            this.x = this.radius;

        } else if (this.x + this.radius > canvasWidth) {
            this.velX = -this.velX * this.bouncyness;
            this.x = canvasWidth - this.radius;

        }

        // Check top and bottom boundaries (account for radius)
        if (this.y - this.radius < 0) {
            this.velY = -this.velY * this.bouncyness;
            this.y = this.radius;

        } else if (this.y + this.radius > canvasHeight) {
            this.velY = -this.velY * this.bouncyness;
            this.y = canvasHeight - this.radius;

            //this.infected = true;
        }
    }

    // This method checks for circle circl collisions
    public void circleCircleCollisions(List<PongCircle> pongList) {
        
        for (PongCircle circle : pongList) {
            // Skip checking collision with itself

            if (this == circle){
                continue;
            }
            else if (getCircleDistance(circle) < this.radius + circle.radius) {

                //spread the infection
                if (circle.infected == true) {
                    this.infected = true;
                }

                // Calculate collision normal (direction from this ball to other ball)
                double dx = circle.x - this.x;
                double dy = circle.y - this.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                // Normalize the collision normal
                if (distance > 0) {
                    double nx = dx / distance;
                    double ny = dy / distance;
                    
                    // Calculate relative velocity
                    double relVelX = circle.velX - this.velX;
                    double relVelY = circle.velY - this.velY;
                    
                    // Calculate relative velocity along collision normal
                    double velAlongNormal = relVelX * nx + relVelY * ny;
                    
                    // Don't process collision if balls are moving apart
                    if (velAlongNormal < 0) {
                        // Apply bounciness (coefficient of restitution)
                        double restitution = (this.bouncyness + circle.bouncyness) / 2;
                        double impulse = -(1 + restitution) * velAlongNormal;
                        
                        // Update velocities for both balls
                        this.velX -= impulse * nx;
                        this.velY -= impulse * ny;

                        //only update other balls velocity if it is a physics ball otherwise set it to 0
                        if (circle instanceof PhysicPongCircle) {
                        circle.velX += impulse * nx;
                        circle.velY += impulse * ny;
                        }


                    }
                }
            }
        }
    }

    
    
    // Helper method to check if this circle collides with another circle
    private double getCircleDistance(PongCircle other) {
        // Calculate distance between the two centers
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Circles collide if distance is less than sum of radii
        return distance;
    }
}

