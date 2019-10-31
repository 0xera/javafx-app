package sample.panes;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.stages.Main;

import java.util.concurrent.*;

public class WaitTheQPane extends HBox {

    private final String READY_TEXT = "Ready?";
    private ProgressIndicator mProgressIndicator = new ProgressIndicator(0);
    private Text mText = new Text(READY_TEXT);
    private int mProgressValue = 0;
    private static WaitTheQPane instance;

    public static WaitTheQPane getInstance() {
        return (instance == null) ? instance = new WaitTheQPane() : instance;
    }

    private WaitTheQPane() {
        super(20);
        setPrefSize(500, 300);
        setAlignment(Pos.CENTER);
        getChildren().add(createContent());
    }

    private VBox createContent() {
        VBox vBox = new VBox();
        mText.setFont(Main.FONT);
        vBox.getChildren().addAll(mText, mProgressIndicator);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public int getProgressValue() {
        return mProgressValue;
    }

    public void setProgressValue(int progressValue) {
        mProgressValue = progressValue;
        mProgressIndicator.setProgress(mProgressValue / 100.0);
    }

    public Task<Boolean> startTimer() {
        Task<Boolean> booleanTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                for (int y = 0; y < 4; y++) {
                    try {
                        setProgressValue(mProgressValue + 25);
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        };
        new Thread(booleanTask).start();
        return booleanTask;
    }
}

