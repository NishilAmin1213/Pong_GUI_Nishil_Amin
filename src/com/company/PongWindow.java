package com.company;

import org.w3c.dom.ls.LSOutput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import static java.lang.Math.random;
//canvas is 1002 x 463 + use method to tell me values when i need to use - canvas.getHeight() ect FIX THIS
//ball could hit tip or bottom of bat and we need to change ball y velocity
//i am storing coordinates top left corner because its easier to draw in java, but makes calculations more complex.


public class PongWindow extends JFrame {
    BufferedImage imageBall;
    private boolean[] buttons = {false, false, false, false};
    String msg;
    Ball ball;
    Bat rightBat;
    Bat leftBat;

    private void separate(Ball ball, Bat bat){
        if(ball.vx > 0){
            //ball moving right and is going in the correct direction
            //move LHS of ball so that its at the RHS of the left bat
            ball.x = leftBat.x_pos + leftBat.depth;
        }
        if(ball.vx < 0){
            //ball moving left and is going in the correct direction
            //move RHS of ball so that its at the LHS of the right bat
            ball.x = rightBat.x_pos - ball.rad;
        }
    }


    private void addVel(Ball ball, Bat bat){
        if(bat.moveDown){
            ball.vy = ball.vy + bat.speed;
        }else if(bat.moveUp){
            ball.vy = ball.vy - bat.speed;
        }
    }

    private void reset(){
        //reset ball
        ball.x = 500;
        ball.y = 250;
        ball.vx = random();
        ball.vy = random();
        //reset bats
        leftBat.y_pos = 200;
        rightBat.y_pos =200;

    }

    private int pointWon(Ball ball, Bat leftBat, Bat rightBat){
        if(ball.x < 0){
            //passed left bat, right player won point
            rightBat.points ++;
            return 1;
        }
        if(ball.x > 1000){
            //passed right bat, left player wins point
            leftBat.points ++;
            return 1;
        }
        return 0;
    }

    private void ballCollided(Ball ball, Bat bat){
        ball.vx *= -1;
    }

    private void edgeTest(Ball ball){
        //check if the ball is colliding with the top or bottom of the window
        if(ball.y <= 0 || ball.y >= 445){
            ball.vy *= -1;
        }
    }

    private void checkBat(Bat bat){

    }

    private boolean collisionTest(double[][] a,double[][] b){
        //4 possibilities to not overlap
        //min a > max b for x values
        boolean X = (a[0][0] > b[1][0]);
        //max a < min b for x values
        boolean Y = (a[1][0] < b[0][0]);
        //min a > max b for y values
        boolean Z = (a[0][1] > b[1][1]);
        //max a < min b for y values
        boolean W = (a[1][1] < b[0][1]);
        //System.out.println(X + " " +  Y + " "+ Z + " " + W);
        return !(X||Y||Z||W);
    }

    public PongWindow(){
        this.setTitle("Pong GUI");
        this.setResizable(false);
        this.setSize(1016,500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        content.setBackground(Color.BLACK);
        //Create Ball Image
        //load image to display
        imageBall = null;
        try{
            imageBall = ImageIO.read(new File("imageBall.png"));
            System.out.println(imageBall.getWidth() + " " + imageBall.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Create Bat Instances
        leftBat = new Bat(0);
        rightBat = new Bat(975);
        ball = new Ball();


        JPanel canvas = new JPanel(){

            @Override
            public void paint(Graphics g) {
                // clears previous pong table
                g.clearRect(0, 0, getWidth(), getHeight());
                // sets background to black
                g.setColor(Color.BLACK);
                g.fillRect(0,0,getWidth(), getHeight());
                //adds markings
                g.setColor(Color.WHITE);
                g.drawLine(500, 0, 500, 500);
                //draws 3 main objects
                g.setColor(Color.CYAN);
                //right bat
                g.fillRoundRect((int)rightBat.x_pos , (int)rightBat.y_pos, (int)rightBat.depth, (int)rightBat.width, 5, 5);
                //left bat
                g.fillRoundRect((int)leftBat.x_pos , (int)leftBat.y_pos, (int)leftBat.depth, (int)leftBat.width, 5, 5);
                //ball
                g.drawImage(imageBall, (int)ball.x, (int)ball.y, null);
                //Display Score
                g.setColor(Color.CYAN);
                g.setFont(new java.awt.Font("Century Schoolbook L", Font.PLAIN, 30));
                g.drawString(String.valueOf(rightBat.points), 505 , 25);
                g.drawString(String.valueOf(leftBat.points), 445 , 25);
                //draw bounding boxes
                double[][] ballBox = ball.getBox();
                double[][] rightBox = rightBat.getBox();
                double[][] leftBox = leftBat.getBox();

                g.setColor(Color.YELLOW);
                g.drawRect((int)ballBox[0][0], (int)ballBox[0][1], (int)(ballBox[1][0] - ballBox[0][0]), (int)(ballBox[1][1] - ballBox[0][1]));
                g.drawRect((int)rightBox[0][0], (int)rightBox[0][1], (int)(rightBox[1][0] - rightBox[0][0]), (int)(rightBox[1][1] - rightBox[0][1]));
                g.drawRect((int)leftBox[0][0], (int)leftBox[0][1], (int)(leftBox[1][0] - leftBox[0][0]), (int)(leftBox[1][1] - leftBox[0][1]));
            }
        };
        content.add(canvas, BorderLayout.CENTER);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                // up(Q):81  down(A):65  up(ARR):38  down(ARR):40
                switch(e.getKeyCode()){
                    case 81 : leftBat.moveUp = true; break;
                    case 65 : leftBat.moveDown = true; break;
                    case 38 : rightBat.moveUp = true; break;
                    case 40 : rightBat.moveDown = true; break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(e.getKeyCode());
                // up(Q):81  down(A):65  up(ARR):38  down(ARR):40
                switch (e.getKeyCode()) {
                    case 81: leftBat.moveUp = false;break;
                    case 65: leftBat.moveDown = false;break;
                    case 38: rightBat.moveUp = false;break;
                    case 40: rightBat.moveDown = false;break;
                }
            }
        });

        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                System.out.println(canvas.getWidth() + "  "+  canvas.getHeight());
                if(rightBat.moveUp){
                    rightBat.y_pos = Math.max(0, rightBat.y_pos - rightBat.speed); //CLAMPING THE y position
                    //rightBat.y_pos -= rightBat.speed;
                }else if(rightBat.moveDown){
                    rightBat.y_pos += rightBat.speed;
                    rightBat.y_pos -= Math.max(0, (rightBat.y_pos + rightBat.width)-canvas.getHeight());
                }
                if(leftBat.moveUp){
                    //leftBat.y_pos -= leftBat.speed;
                    leftBat.y_pos = Math.max(0, leftBat.y_pos - leftBat.speed);
                }else if(leftBat.moveDown){
                    leftBat.y_pos += leftBat.speed;
                    leftBat.y_pos -= Math.max(0, (leftBat.y_pos + leftBat.width)-canvas.getHeight());
                }

                ball.move();

                double[][] ballBox = ball.getBox();
                double[][] rightBox = rightBat.getBox();
                double[][] leftBox = leftBat.getBox();
                /*
                System.out.println("--->");
                System.out.println(Arrays.toString(ballBox[0]) + ", " + Arrays.toString(ballBox[1]));
                System.out.println(Arrays.toString(rightBox[0]) + ", " + Arrays.toString(rightBox[1]));
                System.out.println(Arrays.toString(leftBox[0]) + ", " + Arrays.toString(leftBox[1]));
                */

                //Check for collisions
                if(collisionTest(rightBat.getBox(), ballBox)){
                    //ball collided with right bat
                    ballCollided(ball, rightBat);
                    addVel(ball, rightBat);
                    separate(ball, rightBat);
                }else if(collisionTest(leftBat.getBox(), ballBox)){
                    //ball collided with left bat
                    ballCollided(ball, leftBat);
                    addVel(ball, leftBat);
                    separate(ball, leftBat);
                }
                edgeTest(ball);

                int x = pointWon(ball, leftBat, rightBat);
                if(x == 1){
                    //restart game and add speed to ball OR make paddles smaller
                    System.out.println("POINT ADDED");
                    reset();
                }
                canvas.repaint();
            }
        }, 0, 5);








        this.setVisible(true);
    }

}








//program pong in java
//2 sides
//2 players, with a bat on each size which move up and down on the table

//need to know bats 1D y coord position, width
// ball has position, x and y, radius, direction and 2D Velocity

//2 main physics for the ball
// - ball with bats and ball with top and bottom,
// - ball in air

//dont worry about exact collision with ball and rectangle
//possible things that might collide?
//how can i tell wether its worth collistion detection?
//axis aligned bounding box collision detection
//has box A overlapped with box B





//adding velocity of bat onto the ball, pixels/refresh
//current position, last position
//could hard code the velocity of the bat, but depends on direction of bat, up or down

//account for hitting the edge of the screen

//add points, wins / losses,

//targets to powerup or loss - change of speed of ball or width of bat
//staRt of ball movement







