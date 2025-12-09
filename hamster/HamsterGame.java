import javax.swing.*;
public class HamsterGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel(800, 300);
            GameView view = new GameView(model);
            GameController controller = new GameController(model, view);

            JFrame frame = new JFrame("Hamster Jump");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            controller.start();
        });
    }


}
