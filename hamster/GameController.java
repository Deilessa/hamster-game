import javax.swing.*;
import java.awt.event.*;

// ---------------------------- CONTROLLER ----------------------------
class GameController implements ActionListener, KeyListener, MouseListener {
    GameModel model;
    GameView view;
    Timer timer;

    final int FPS = 60;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.view.addKeyListener(this);
        this.view.addMouseListener(this);

        timer = new Timer(1000 / FPS, this);
    }

    public void start() {
        timer.start();
        view.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // game loop tick
        model.update();
        view.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_SPACE) {
            model.jump();
        } else if (kc == KeyEvent.VK_P) {
            model.togglePause();
        } else if (kc == KeyEvent.VK_R) {
            model.reset();
        } else if (kc == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // If game over, clicking the view restarts
        if (model.isGameOver()) {
            model.reset();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
