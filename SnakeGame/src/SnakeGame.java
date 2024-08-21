import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private final int cellSize;
    private static final int FRAME_RATE = 20;
    private boolean gameStarted = false;
    private final List<GamePoint> snake = new ArrayList<>();



    public SnakeGame(final int width, final int height) {
        // call super as we are inheriting from JPanel
            super();
            this.width = width;
            this.height = height;
            // give users 2 seconds if the snake goes from one end of the width to the other
            this.cellSize = width / (FRAME_RATE * 2);
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
    }

    public void startGame(){
        resetGameData();
        // gives the user the ability to press 'Enter'
        setFocusable(true);

        // focus on the window only
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameStarted = true;
                }
            }
        });


        // repaint the layout after the game is considered started
        // application checks on the status of the game and its state
        new Timer(1000 / FRAME_RATE, this).start();
    }

    private void resetGameData() {
        // list of boxes
        // boxes will grow over time
        snake.clear();
        snake.add(new GamePoint(width / 2, height / 2));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (!gameStarted) {


            graphics.setColor(Color.WHITE);
            graphics.setFont(graphics.getFont().deriveFont(30f));

            int currentHeight = height / 3;
            final var graphics2D = (Graphics2D) graphics;
            final var frc = graphics2D.getFontRenderContext();

            // sample message
            final String message = "Press Enter to Play";
            for (final var line : message.split("\n")) {

                final var layout = new TextLayout(line, graphics2D.getFont(), frc);
                final var bounds = layout.getBounds();
                final var targetWidth = (float) (width - bounds.getWidth()) / 2;
                layout.draw(graphics2D, targetWidth, currentHeight);
                currentHeight += graphics2D.getFont().getSize();

            }

        }
        else {
            graphics.setColor(Color.GREEN);
            for (final var point : snake) {
                // as snake grows, each time user enters game loop, snake is redrawn
                // set of consecutive squares
                graphics.fillRect(point.x, point.y, cellSize, cellSize);
            }
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // access to J components graphics object
        // call into Java Swing API

        repaint();
    }
    private record GamePoint(int x, int y) {

    }
}
