import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

class GamePanel extends JPanel implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;
    private final int WIDTH = 600, HEIGHT = 600;
    private int step =20;
    private Thread thread;
    private boolean running;
    private ArrayList<SnakePart> snake;
    private Point point;
    private boolean right, left, up, down;

    GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        snake = new ArrayList<>();
        setFocusable(true);
        addKeyListener(this);
        right=true;
        left=false;
        up=false;
        down=false;
        start();
    }

    void start(){
        running = true;
        thread = new Thread(this);
        SnakePart snakePart = new SnakePart(WIDTH/2, HEIGHT/2, Direction.RIGHT);
        snake.add(snakePart);
        point = getRandomCorsPoint();
        thread.start();
    }

    void stop() throws InterruptedException {
        running=false;
        thread.join();
    }

    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WIDTH, HEIGHT);

        g.setColor(Color.GRAY);
        for(int i=0; i<HEIGHT; i+=step){
            for(int j=0; j<WIDTH; j+=step){
                g.drawLine(j,i, j+step, i);
            }
        }

        for(int i=0; i<WIDTH; i+=step){
            for(int j=0; j<HEIGHT; j+=step){
                g.drawLine(j,i, j, i+step);
            }
        }

        for(SnakePart snakePart: snake){
            g.setColor(Color.MAGENTA);
            int x = snakePart.getX();
            int y = snakePart.getY();
            for(int i=0; i<step; i++){
                g.drawLine(x+i, y, x+i, y+step);
            }
        }

        g.setColor(Color.YELLOW);
        int x= point.getX();
        int y= point.getY();
        for(int i=0; i<step; i++){
            g.drawLine(x+i, y, x+i, y+step);
        }

    }

    void tick() throws InterruptedException {
        //Get direction
        for(int i=snake.size()-1; i>=0; i--) {
            SnakePart snakePart = snake.get(i);
            if(snakePart.equals(snake.get(0))){
                if(left) {
                    snakePart.setDirection(Direction.LEFT);
                } else if(right){
                    snakePart.setDirection(Direction.RIGHT);
                } else if(up){
                    snakePart.setDirection(Direction.UP);
                }else if(down){
                    snakePart.setDirection(Direction.DOWN);
                }
            } else {
                SnakePart snakePrev = snake.get(i-1);
                snakePart.setDirection(snakePrev.getDirection());
            }
        }

        //Calculate evr part direction
        for(SnakePart snakePart: snake){
            Direction direction = snakePart.getDirection();
            if(direction.equals(Direction.RIGHT)){
                snakePart.setX(snakePart.getX()+step);
            } else if(direction.equals(Direction.LEFT)){
                snakePart.setX(snakePart.getX()-step);
            } else if(direction.equals(Direction.UP)){
                snakePart.setY(snakePart.getY()-step);
            } else if(direction.equals(Direction.DOWN)){
                snakePart.setY(snakePart.getY()+step);
            }
        }

        SnakePart snakeHead = snake.get(0);
        int snakeHeadX = snake.get(0).getX();
        int snakeHeadY = snake.get(0).getY();

        //Point collision
        if(snakeHeadX == point.getX() && snakeHeadY == point.getY()){
            SnakePart snakeLast = snake.get(snake.size()-1);
            Direction direction = snakeLast.getDirection();
            int newX, newY;
            if(direction.equals(Direction.RIGHT)){
                newX = snakeLast.getX()-step;
                newY = snakeLast.getY();
            } else if(direction.equals(Direction.LEFT)){
                newX = snakeLast.getX()+step;
                newY = snakeLast.getY();
            } else if(direction.equals(Direction.UP)){
                newX = snakeLast.getX();
                newY = snakeLast.getY()+step;
            } else {
                newX = snakeLast.getX();
                newY = snakeLast.getY()-step;
            }
            SnakePart snakePart = new SnakePart(newX, newY, direction);
            snake.add(snakePart);

            point= getRandomCorsPoint();
        }

        //Body collision
        for(SnakePart snakePart : snake){
            if(!snakeHead.equals(snakePart)){
                if(snakePart.getX()==snakeHeadX && snakePart.getY()==snakeHeadY){
                    stop();
                }
            }
        }

        //Walls collision
        if(snakeHeadX >= WIDTH || snakeHeadX < 0 || snakeHeadY >= HEIGHT || snakeHeadY <0){
            stop();
        }
        Thread.sleep(80);

    }

    private Point getRandomCorsPoint(){
        Random rand = new Random();
        int x,y;
        do {
            x = rand.nextInt(WIDTH/step)*step;
            y = rand.nextInt(HEIGHT/step)*step;
        } while (! isCorsOk(x, y));
        return new Point(x, y);
    }

    private boolean isCorsOk(int x, int y){
        for(SnakePart snakePart : snake){
            if (x==snakePart.getX() && y==snakePart.getY())
                return false;
        }
        return true;
    }

    @Override
    public void run() {
        while (running){
            try {
                tick();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key==KeyEvent.VK_RIGHT && !left){
            right=true;
            up=false;
            down=false;
        } else if(key==KeyEvent.VK_LEFT && !right){
            left=true;
            up=false;
            down=false;
        } else if(key==KeyEvent.VK_UP && !down){
            right=false;
            left=false;
            up=true;
        }else if(key==KeyEvent.VK_DOWN && !up){
            right=false;
            left=false;
            down=true;
        }
    }
}
