package Controller;

import Interface.ControllerInterface;
import Interface.ModelInterface;
import Model.Model;
import Model.GameState;
import View.GView;

import java.awt.event.KeyEvent;
import java.util.List;

public class Controller implements ControllerInterface {

    private ModelInterface model;
    private GView view;

    private boolean isGameOver;

    public Controller(Model model){

        this.model = model;
        isGameOver = false;
        this.model.start();

        view = new GView(model, this);
        view.initMenu();
        view.initPanel();
        view.initFrame();
    }

    public void gameControl(KeyEvent event) {
        if(model.getState() == GameState.PLAYING) {
            if (event.getKeyCode() == KeyEvent.VK_UP) {
                model.slideUp();
            }
            if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                model.slideDown();
            }
            if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                model.slideRight();
            }
            if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                model.slideLeft();
            }
            System.out.println("Nothing((");
        } /*else {
            doGameOver();
        }*/
    }

    @Override
    public void startNewGame() {
        model.start();
        model.notifyObservers(model.getState());
    }

    @Override
    public void exitGame() {
        view.removeAll();
        System.exit(0);
    }

    @Override
    public void showScoreTable() {
        List<String> table = model.getScoreTable();
        int iter = 0;
        StringBuilder strForView = new StringBuilder();
        for (String str : table) {
            if (5 == iter) {
                break;
            }
            strForView.append(str + "\n");
            ++iter;
        }
        view.showScoreTable(strForView);
    }

    @Override
    public void showAboutMessage() {
        view.showAboutMessage(model.getAuthorsMessage());
    }

    public void doGameOver(String name) {
        if (name == null)
            name = "anonymous";
        model.setName(name);
        model.addNewScore();
        model.writeScores();
    }
}
