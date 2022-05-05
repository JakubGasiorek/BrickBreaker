import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class MyPanel extends JPanel implements KeyListener, ActionListener{

    private boolean play = false;
    private int score = 0;
    private int bricks = 21;

    private  int playerX = 350;

    private int ballPosX = 390;
    private int ballPosY = 480;

    private double ballXdir = -3;
    private double ballYdir = -2;

    private final Timer time;

    private BrickGenerator map;

    public MyPanel(){
        map = new BrickGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 8;
        time = new Timer(delay, this);
        time.start();
    }

    public void paint(Graphics g){
        //background
        g.setColor(Color.gray);
        g.fillRect(1,1, 792,592);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,692);
        g.fillRect(0,0,792,3);
        g.fillRect(781,0,3,592);

        //drawing bricks
        map.draw((Graphics2D)g);

        //score
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,692,30);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 530, 100, 8);

        //ball
        g.setColor(Color.red);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        //winning
        if(bricks <=0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You Won" ,300,250);
            g.drawString("Score: "+score,300,290);
            g.drawString("Press Enter to restart",300,330);
        }

        //losing
        if(ballPosY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over" ,300,250);
            g.drawString("Score: "+score,300,290);
            g.drawString("Press Enter to restart",300,330);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if(play){
            //odbicie z platformą
            if(new Rectangle(ballPosX,ballPosY,20,20)
                    .intersects(new Rectangle(playerX,530,100,8))){
                ballYdir = -ballYdir;
            }

            A :for(int i = 0; i < map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        //odbicie z prostokątami
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX,ballPosY,20,20);
                        if(ballRect.intersects(rect)){
                            map.setBrickValue(0,i,j);
                            bricks--;
                            score+=10;

                            //przyśpiesza piłke przy zniszczeniu(utrudnienie)
                            ballXdir+=0.5;

                            if (ballPosX + 15 <= rect.x || ballPosX + 3 >= rect.x + rect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            //poruszanie się piłeczki
            ballPosX += ballXdir;
            ballPosY += ballYdir;

            //odbijanie od ścian
            if(ballPosX < 3){
                ballXdir = -ballXdir;
            }
            if(ballPosY < 3){
                ballYdir = -ballYdir;
            }

            if(ballPosX > 759){
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 681){
                playerX = 681;
            }else{
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 20){
                playerX = 3;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                playerX = 350;
                ballPosX = 390;
                ballPosY = 480;
                ballXdir = -3;
                ballYdir = -2;
                score = 0;
                bricks = 21;
                map = new BrickGenerator(3,7);
                repaint();
            }
        }
    }

    public void moveRight(){
        play = true;
        playerX+=20;
    }

    public void moveLeft(){
        play = true;
        playerX-=20;
    }

    @Override
    public void keyReleased(KeyEvent e) {}


}
