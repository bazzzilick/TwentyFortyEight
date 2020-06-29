package Interface;

import java.awt.event.KeyEvent;

public interface ControllerInterface {

    void gameControl(KeyEvent event);

    void startNewGame();

    void exitGame();

    void showScoreTable();

    void showAboutMessage();

    void doGameOver(String score);
}
