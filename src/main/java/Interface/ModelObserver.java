package Interface;

import Model.GameState;

public interface ModelObserver {

    void update(GameState gameState);
}
