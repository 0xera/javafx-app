package sample.panes;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Observable;
import java.util.Observer;

public class SidePane extends VBox implements Observer {

    private static SidePane instance;
    private int current = 1;
    private Callback mCallback;
    private final int questionsCount = 15;

    public static SidePane geInstance(Callback callback) {
        return (instance == null) ? instance = new SidePane(callback) : instance;
    }

    private SidePane(Callback callback) {
        mCallback = callback;
        createContent();

    }
    private void createContent() {
        for (int qNum = questionsCount; qNum > 0; qNum--) {
            Text text = new Text("Question - " + qNum);
            text.setFill(qNum == current ? Color.BLACK : Color.GRAY);
            getChildren().add(text);
            setPadding(new Insets(10, 10, 10, 0));
        }
    }

    public void exitGame() {
        current = 1;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public void update(Observable o, Object arg) {
        mCallback.selectNextQuestion();
    }
    public void moveToFirst() {
        Text text = (Text) getChildren().get(15 - current);
        text.setFill(Color.GRAY);
        exitGame();
        text = (Text) getChildren().get(14);
        text.setFill(Color.BLACK);
    }

    public interface Callback {
        void selectNextQuestion();
    }
}