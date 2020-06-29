package View;

import Interface.ControllerInterface;
import Interface.ModelInterface;
import Interface.ModelObserver;
import Model.GameState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class GView extends JFrame implements ModelObserver, ActionListener {

    private final int IMAGE_SIZE = 50;
    private Map<Integer, Image> imageMap;
    private GameAdapter gameAdapter;

    private ModelInterface model;
    private ControllerInterface controller;

    private JPanel panel;
    private JLabel label;
    private JMenuBar bar;
    private JMenu mainMenu;
    private JMenu infoMenu;
    private JMenuItem newGame;
    private JMenuItem exitGame;
    private JMenuItem score;
    private JMenuItem about;

    public GView(ModelInterface model, ControllerInterface controller) {

        this.model = model;
        this.model.registerObserver(this);
        this.controller = controller;

        imageMap = new HashMap<>();
        setImages();
    }

    public void update(GameState gameState) {
        panel.repaint();
        label.setText("Score: " + model.getScore());
        if (gameState == GameState.LOSER) {
            showGameOverMessage();
        }
        if (gameState == GameState.WINNER) {
            showWinnerMessage();
        }

    }

    private void showWinnerMessage() {
        String result = JOptionPane.showInputDialog(null,
                "Congratulations you won, your result:\n" + model.getScore() + " points"
                        + "\nwould you like to save it?", "anon");
        controller.doGameOver(result);
    }

    public void showGameOverMessage() {
        String result = JOptionPane.showInputDialog(null,
                "Unfortunately you lost, your result:\n" + model.getScore() + " points"
                        + "\nwould you like to save it?", "anon");
        controller.doGameOver(result);
    }

    public void showScoreTable(StringBuilder str) {
        JOptionPane.showMessageDialog(null, str, "High Scores",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void showAboutMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(newGame)) {
            controller.startNewGame();
        } else if (event.getSource().equals(exitGame)) {
            controller.exitGame();
        } else if (event.getSource().equals(score)) {
            controller.showScoreTable();
        } else if (event.getSource().equals(about)) {
            controller.showAboutMessage();
        }
    }

    public class GameAdapter implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) { }
        @Override
        public void keyPressed(KeyEvent e) {
            controller.gameControl(e);
        }
        @Override
        public void keyReleased(KeyEvent e) { }
    }

    private void setImages() {
        imageMap.put((Integer) 0, getImage(((Integer) 0).toString()));
        for(Integer i = 2; i < 2049; i = i * 2)
            imageMap.put(i, getImage(i.toString()));
    }

    private Image getImage(String imageName) {
        System.out.println(imageName);
        String filename = "img/" + imageName.toLowerCase() + ".png";
        ImageIcon icon = new ImageIcon(this.getClass().getClassLoader().getResource(filename));
        return icon.getImage();
    }

    public void initMenu() {
        label = new JLabel("Score: " + 0);
        infoMenu = new JMenu("Info");
        score = new JMenuItem("Score");
        score.addActionListener(this);
        about = new JMenuItem("About");
        about.addActionListener(this);

        infoMenu.add(score);
        infoMenu.add(about);
        infoMenu.addSeparator();

        mainMenu = new JMenu("Game");
        newGame = new JMenuItem("New Game");
        newGame.addActionListener(this);
        exitGame = new JMenuItem("Exit Game");
        exitGame.addActionListener(this);

        mainMenu.add(newGame);
        mainMenu.add(exitGame);
        mainMenu.addSeparator();

        bar = new JMenuBar();
        bar.add(mainMenu);
        bar.add(infoMenu);
        bar.add(label, BorderLayout.EAST);
        setJMenuBar(bar);
    }

    public void initPanel() {
        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(int y = 0;  y < model.getSize(); y++)
                    for(int x = 0; x < model.getSize(); x++)
                        g.drawImage((Image) imageMap.get(model.getTile(x, y)),
                                x * IMAGE_SIZE, y * IMAGE_SIZE, this);
            }
        };

        panel.setPreferredSize(new Dimension(
                model.getSize() * IMAGE_SIZE,
                model.getSize() * IMAGE_SIZE));
        add(panel);
    }

    public void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("2048");
        setResizable(false);
        setVisible(true);
        pack();
        gameAdapter = new GameAdapter();
        addKeyListener(gameAdapter);
        setLocationRelativeTo(null);
    }
}
