import javax.swing.*;
import java.awt.event.*;

// ---------------------------- CONTROLLER ----------------------------
class GameController implements ActionListener {
    GameModel model;
    GameView view;
    Timer timer;

    final int FPS = 60;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        timer = new Timer(1000 / FPS, this);
        setupKeyBindings();
        setupMouseBinding();
    }

    public void start() {
        timer.start();
        view.requestFocusInWindow();
    }

    private void bind(String key, Runnable action) {
        ActionMap am = view.getActionMap();
        am.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    public void setupKeyBindings() {
        InputMap im = view.getInputMap();

        bind("jump", model::jump);
        bind("pause", model::togglePause);
        bind("reset", model::reset);
        bind("exit", () -> System.exit(0));
        bind("mouseClick", () -> {if (model.isGameOver()) model.reset();});

        im.put(KeyStroke.getKeyStroke("P"), "pause");
        im.put(KeyStroke.getKeyStroke("R"), "reset");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        im.put(KeyStroke.getKeyStroke("SPACE"), "jump");
        im.put(KeyStroke.getKeyStroke("W"), "jump");
        im.put(KeyStroke.getKeyStroke("BUTTON1"), "mouseClick");
    }

    private void setupMouseBinding() {
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Action a = view.getActionMap().get("mouseClick");
                if (a != null) a.actionPerformed(null);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.update();
        view.repaint();
    }
}
