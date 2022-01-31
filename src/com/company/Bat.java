package com.company;

public class Bat {
    public double y_pos = 200;
    public double x_pos;
    public double width = 100;
    public double depth = 25;
    public boolean moveUp = false;
    public boolean moveDown = false;
    public int points = 0;
    public int speed = 1;

    public Bat(int a){
        this.x_pos = a;
    }

    public double[][] getBox(){
        return new double[][] {{x_pos, y_pos},{x_pos + depth, y_pos + width}};
    }
}
