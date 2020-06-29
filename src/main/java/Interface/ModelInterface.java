package Interface;

import Model.GameState;
import View.GView;

import java.util.List;

public interface ModelInterface {
    void start();

    void registerObserver(ModelObserver gView);

    public void notifyObservers(GameState gameState);

    int getTile(int x, int y);

    int getSize();

    GameState getState();

    void slideUp();

    void slideDown();

    void slideRight();

    void slideLeft();

    Long getScore();

    boolean isGameOver();

    List<String> getScoreTable();

    String getAuthorsMessage();

    void addNewScore();

    void writeScores();

    void setName(String name);
}
