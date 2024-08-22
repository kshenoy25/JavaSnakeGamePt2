import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private final int cellSize;
    private final Random random = new Random();
    private static final int FRAME_RATE = 20;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private GamePoint food;
    private Direction direction = Direction.UP;
    private Direction newDirection = Direction.UP;
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
                handleKeyEvent(e.getKeyCode());
            }
        });

        // repaint the layout after the game is considered started
        // application checks on the status of the game and its state
        new Timer(1000 / FRAME_RATE, this).start();
    }

    private void handleKeyEvent(final int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            gameStarted = true;
        }
        else if (!gameOver){
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    // if statement avoids 180 turn for the snake
                    if (direction != Direction.DOWN)  {
                        newDirection = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) {
                        newDirection = Direction.DOWN;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) {
                        newDirection = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) {
                        newDirection = Direction.LEFT;
                    }
                    break;
            }
        }
    }

    private void resetGameData() {
        // list of boxes
        // boxes will grow over time
        snake.clear();
        snake.add(new GamePoint(width / 2, height / 2));
        generateFood();
    }

    private void generateFood(){
        do {
            // as long as snake is not sitting on the food (contains)
            food = new GamePoint(random.nextInt(width / cellSize) * cellSize,
                    random.nextInt(height / cellSize) * cellSize);
        } while (snake.contains(food));
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
            graphics.setColor(Color.WHITE);
            graphics.fillRect(food.x, food.y, cellSize ,cellSize);

            graphics.setColor(Color.RED);
            for (final var point : snake) {
                // as snake grows, each time user enters game loop, snake is redrawn
                // set of consecutive squares
                graphics.fillRect(point.x, point.y, cellSize, cellSize);
            }
        }
    }
    private void move() {
        final GamePoint head = snake.getFirst();
        final GamePoint newHead = switch(direction){
            // takes direction we are in
            // maps the new value of the head of the snake in the current frame
            case UP -> new GamePoint(head.x, head.y - cellSize);
            case DOWN -> new GamePoint(head.x, head.y + cellSize);
            case LEFT -> new GamePoint(head.x - cellSize, head.y);
            case RIGHT -> new GamePoint(head.x + cellSize, head.y);
        };
        snake.addFirst(newHead);
        if (newHead.equals(food)){
            generateFood();
        } else if (checkCollision()){{}
            // indicate that the game is over
            gameOver = true;
            snake.removeFirst();
        }
        else {
            snake.removeLast();
        }
        direction = newDirection;

    }

    private boolean checkCollision() {
        // check to see if head of the snake is out of bounds of the space
        final GamePoint head = snake.getFirst();
        final var invalidWidth = (head.x < 0) || (head.x >= width);
        final var invalidHeight = (head.y < 0) || (head.y >= height);
        return (invalidWidth || invalidHeight);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
        }

        // access to J components graphics object
        // call into Java Swing API

        repaint();
    }


    private record GamePoint(int x, int y) {
    }
    private enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }
}
