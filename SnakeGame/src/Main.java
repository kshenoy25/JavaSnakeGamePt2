import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Snake Game");
        frame.setSize(800, 600);
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();

    }
}
