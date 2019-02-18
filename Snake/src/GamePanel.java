import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class GamePanel extends JPanel implements Runnable{

    private static final long serialVersionUID = 1L;
    private final int WIDTH = 600, HEIGHT = 600;
    private Thread thread;
    private boolean running;
    private ArrayList<SnakePart> snake;
    private Point point;

    GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        snake = new ArrayList<>();
        start();
    }

    void start(){
        running = true;
        thread = new Thread(this);
        SnakePart snakePart = new SnakePart(WIDTH/2, HEIGHT/2, Direction.VERTICAL_R);
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
        for(int i=0; i<HEIGHT; i+=10){
            for(int j=0; j<WIDTH; j+=10){
                g.drawLine(j,i, j+10, i);
            }
        }

        for(int i=0; i<WIDTH; i+=10){
            for(int j=0; j<HEIGHT; j+=10){
                g.drawLine(j,i, j, i+10);
            }
        }

        for(SnakePart snakePart: snake){
            g.setColor(Color.MAGENTA);
            int x = snakePart.getX();
            int y = snakePart.getY();
            for(int i=0; i<10; i++){
                g.drawLine(x+i, y, x+i, y+10);
            }
        }

        g.setColor(Color.YELLOW);
        int x= point.getX();
        int y= point.getY();
        for(int i=0; i<10; i++){
            g.drawLine(x+i, y, x+i, y+10);
        }

    }

    void tick(){

    }

    private Point getRandomCorsPoint(){
        Random rand = new Random();
        int x,y;
        do {
            x = rand.nextInt(WIDTH/10)*10;
            y = rand.nextInt(HEIGHT/10)*10;
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
            tick();
            repaint();
        }
    }
}
