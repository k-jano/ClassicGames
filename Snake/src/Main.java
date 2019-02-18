import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Snake");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
