package Model;

import Interface.ModelInterface;
import Interface.ModelObserver;
import View.GView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model implements ModelInterface {

    private Matrix tilesMap;

    private int TARGET_VAL = 2048;
    private int SIZE = 4;
    private static Random random = new Random();

    private GameState gameState;
    private boolean moved;
    private Long score;

    private ArrayList<ModelObserver> modelObservers;

    private ScoreWorker scoreTable;

    public Model() {
        modelObservers = new ArrayList<>();
        tilesMap = new Matrix(SIZE);
        scoreTable = new ScoreWorker();
    }

    public void start() {
        reset();
        gameState = GameState.PLAYING;
        for(int i = 0; i < 2; i++)
            setRandomTile();
    }

    private void reset() {
        tilesMap = new Matrix(SIZE);
        for(int x = 0; x < SIZE; x++)
            for(int y = 0; y < SIZE; y++)
                tilesMap.set(x, y, 0);

        score = Long.valueOf(0);
    }

    private void setRandomTile() {
        if(gameState != GameState.PLAYING)
            return;

        while(true) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);
            if(tilesMap.get(x, y) != 0) {
                continue;
            }
            tilesMap.set(x, y, 2);
            break;
        }
    }

    public boolean isGameOver() {
        if(gameState != GameState.PLAYING)
            return true;

        for(int y = 0; y < SIZE; y++)
            for(int x = 0; x < SIZE; x++)
                if(tilesMap.get(x, y) == 0)
                    return false;

        for(int y = 0; y < SIZE; y++)
            for(int x = 0; x < SIZE; x++)
                if (tilesMap.get(x, y) == tilesMap.get(x + 1, y) ||
                    tilesMap.get(x, y) == tilesMap.get(x, y + 1))
                    return false;

        gameState = GameState.LOSER;
        return true;
    }

    public void registerObserver(ModelObserver observer) {
        modelObservers.add(observer);
    }

    public void notifyObservers(GameState gameState) {
        for(ModelObserver subscriber : modelObservers)
            subscriber.update(gameState);
    }


    public int getTile(int x, int y) {
        return tilesMap.get(x, y);
    }

    public int getSize() { return SIZE; }

    public GameState getState() {
        return gameState;
    }

    public void slideUp() {
        moved = false;
        for(int x = 0; x < SIZE; x++) {
            for (int y = 1; y < SIZE; y++)
                move(x, y, 0, -1);
            for (int y = 1; y < SIZE; y++)
                join(x, y, 0, -1);
        }

        if(!isGameOver()) {
            if (moved)
                setRandomTile();
        }

        notifyObservers(gameState);
    }

    public void slideDown() {
        moved = false;
        for(int x = 0; x < SIZE; x++) {
            for (int y = SIZE - 2; y >= 0; y--)
                move(x, y, 0, +1);
            for (int y = SIZE - 2; y >= 0; y--)
                join(x, y, 0, +1);
        }

        if(!isGameOver()) {
            if (moved)
                setRandomTile();
        }

        notifyObservers(gameState);
    }

    public void slideRight() {
        moved = false;
        for(int y = 0; y < SIZE; y++) {
            for (int x = SIZE - 2; x >= 0; x--)
                move(x, y, +1, 0);
            for (int x = SIZE - 2; x >= 0; x--)
                join(x, y, +1, 0);
        }

        if(!isGameOver()) {
            if (moved)
                setRandomTile();
        }

        notifyObservers(gameState);
    }

    public void slideLeft() {
        moved = false;
        for(int y = 0; y < SIZE; y++) {
            for (int x = 1; x < SIZE; x++)
                move(x, y, -1, 0);
            for (int x = 1; x < SIZE; x++)
                join(x, y, -1, 0);
        }

        if(!isGameOver()) {
            if (moved)
                setRandomTile();
        }

        notifyObservers(gameState);
    }

    private void move(int x, int y, int dx, int dy) {
        if(tilesMap.get(x, y) > 0) {
            while(tilesMap.get(x + dx, y + dy) == 0) {
                tilesMap.set(x + dx, y + dy, tilesMap.get(x, y));
                tilesMap.set(x, y, 0);
                x += dx;
                y += dy;
                moved = true;
            }
        }
    }

    private void join(int x, int y, int dx, int dy) {
        if(tilesMap.get(x, y) > 0) {
            if(tilesMap.get(x + dx, y + dy) == tilesMap.get(x, y)) {
                score = score + 2 * tilesMap.get(x, y);
                if(2 * tilesMap.get(x, y) == TARGET_VAL) {
                    gameState = GameState.WINNER;
                }
                tilesMap.set(x + dx, y + dy, 2 * tilesMap.get(x, y));
                while(tilesMap.get(x - dx, y - dy) > 0) {
                    tilesMap.set(x, y, tilesMap.get(x - dx,  y - dy));
                    x -= dx;
                    y -= dy;
                }
                tilesMap.set(x, y, 0);
                moved = true;
            }
        }
    }

    @Override
    public Long getScore() {
        return score;
    }

    @Override
    public List<String> getScoreTable() {
        return scoreTable.getScoreTable();
    }

    @Override
    public String getAuthorsMessage() {
        String message = "This game was made in a super short time, \n" +
                "as all deadlines were already burning.\n" +
                "All rights reserved\n" +
                "@bazzzilick 2020";
        return message;
    }

    @Override
    public void addNewScore() {
        scoreTable.addNewScore(score);
    }

    @Override
    public void writeScores() {
        scoreTable.writeScoreTableToFile();
    }

    @Override
    public void setName(String name) {
        scoreTable.setNamePlayer(name);
    }

}
