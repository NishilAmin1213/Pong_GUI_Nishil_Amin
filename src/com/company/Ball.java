package com.company;

import static java.lang.Math.random;

public class Ball {
    public double x = 500;
    public double y = 250;
    public double vx = random();
    public double vy = random();
    public int rad = 25;

    public void move(){
        this.x = this.x + this.vx;  // *t, however t can be one refresh/frame
        this.y = this.y + this.vy;
    }

    public double[][] getBox(){
        return new double[][] {{x, y}, {x + rad, y + rad},};
    }

}

// (x, y)
// (vx, vy)
// (x', y') = (x+vx*t, y+vy*t)


//need to know bats 1D y coord position, width
// ball has position, x and y, radius, direction and 2D Velocity

//2 main physics for the ball
// - ball with bats and ball with top and bottom,
// - ball in air


