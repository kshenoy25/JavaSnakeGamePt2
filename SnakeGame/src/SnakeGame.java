import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private static final int FRAME_RATE = 20;
    public SnakeGame(final int width, final int height) {
        // call super as we are inheriting from JPanel
            super();
            this.width = width;
            this.height = height;
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
    }

    public void startGame(){
        // gives the user the ability to press 'Enter'
        setFocusable(true);

        // focus on the window only
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Enter was pressed");
                }
            }
        });


        // repaint the layout after the game is considered started
        // application checks on the status of the game and its state
        new Timer(1000 / FRAME_RATE, this).start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
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

    @Override
    public void actionPerformed(final ActionEvent e) {
        // access to J components graphics object
        // call into Java Swing API

        repaint();
    }
}
