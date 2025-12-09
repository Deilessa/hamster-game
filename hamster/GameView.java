import javax.swing.*;
import java.awt.*;

// ---------------------------- VIEW ----------------------------
class GameView extends JPanel {
    GameModel model;

    public GameView(GameModel model) {
        this.model = model;
        setPreferredSize(new Dimension(model.width, model.height));
        setBackground(
                new Color(4, 208, 237, 147)
        );
        setFocusable(true);
    }

    // Cactus drawing method
    void drawCactus(Graphics2D g2, Rectangle r) {
        g2.setColor(new Color(0, 150, 0));

        // основной ствол
        g2.fillRect(r.x + r.width / 3, r.y, r.width / 3, r.height);

        // левая ветка
        g2.fillRect(r.x, r.y + r.height / 3, r.width / 3, r.height / 6);

        // правая ветка
        g2.fillRect(r.x + r.width * 2 / 3,
                r.y + r.height / 2,
                r.width / 3,
                r.height / 6);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // Enable some rendering hints for nicer text
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw ground
        g2.setColor(new Color(253, 197, 157, 189)); // sand color
        g2.fillRect(0, model.getGroundY(), model.width, model.height - model.getGroundY());


        Image dinoImg = new ImageIcon(getClass().getResource("hamster_clean.png")).getImage();
        Rectangle d = model.getDinoBounds();
        int dinoSize = Math.max(d.width, d.height);
        g2.drawImage(dinoImg, d.x, d.y, dinoSize, dinoSize, null);

// рисуем кактусы
        for (Rectangle r : model.getObstacles()) {
            drawCactus(g2, r);
        }


        // рисуем счёт
        g2.setColor(Color.black);
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        String scoreStr = "Score: " + model.getScore();
        g2.drawString(scoreStr, 10, 20);

        // рисуем подсказки
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.drawString("Space - Jump    P - Pause/Continue    R - Restart    Esc - Exit", 10, model.height - 10);

        // обрабатываем паузы
        if (model.isPaused() && !model.isGameOver()) {
            drawCenteredOverlay(g2, "PAUSED\nPress P to continue");
        }

        // проигрыш
        if (model.isGameOver()) {
            drawCenteredOverlay(g2, "GAME OVER\nPress R to restart");
        }

        g2.dispose();
    }

    private void drawCenteredOverlay(Graphics2D g2, String multiLine) {
        String[] lines = multiLine.split("\\n");
        int w = 320;
        int h = 120;
        int x = (model.width - w) / 2;
        int y = (model.height - h) / 2;

        // background box
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(x, y, w, h, 16, 16);

        g2.setColor(Color.white);
        g2.setFont(new Font("SansSerif", Font.BOLD, 28));

        FontMetrics fm = g2.getFontMetrics();
        int textY = y + 35;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Font font = (i == 0) ? new Font("SansSerif", Font.BOLD, 28) : new Font("SansSerif", Font.PLAIN, 16);
            g2.setFont(font);
            int tw = g2.getFontMetrics().stringWidth(line);
            g2.drawString(line, x + (w - tw) / 2, textY);
            textY += (i == 0) ? 40 : 24;
        }
    }
}
