package sample.panes;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.stages.Main;
import sample.objects.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionPane extends VBox {

    private static QuestionPane instance;
    private ProgressIndicator progressIndicator = new ProgressIndicator(0);
    private Callback mCallback;
    private Text mText = new Text();
    private List<Button> ButtonsList = new ArrayList<>();
    private Question mCurrentQuestion;
    private int mButtonsCount = 4;
    private int mProgressValue = 0;
    private Thread taskTread;
    private Task<Boolean> task;

    public static QuestionPane getInstance(Callback callback) {
        return (instance == null) ? instance = new QuestionPane(callback) : instance;
    }

    public int getProgressValue() {
        return mProgressValue;
    }

    public void setProgressValue(int progressValue) {
        mProgressValue = progressValue;
        progressIndicator.setProgress(progressValue / 10.0);
    }

    private QuestionPane(Callback callback) {
        super(20);
        mCallback = callback;
        mText.setFont(Main.FONT);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(10, 10, 10, 10));
        getChildren().addAll(mText, createButtons(), progressIndicator);
    }

    private HBox createButtons() {
        HBox hBox = new HBox();
        for (int count = 0; count < mButtonsCount; count++) {
            Button button = new Button();
            button.setPrefWidth(120);
            setOnClickListenerForButtons(button);
            ButtonsList.add(button);
            hBox.getChildren().add(button);
        }
        return hBox;
    }

    private void buttonAction(Button button) {
        if (Main.currentQuestion != 15 && button.getText().equals(mCurrentQuestion.getCorrectAnswer())) {
            mCallback.changeQuestion();
            setProgressValue(0);
        } else {
            task.cancel();
            setProgressValue(0);

        }

    }


    public void setQuestion(Question question) {
        mCurrentQuestion = question;
        mText.setText(question.getQuestion());
        Collections.shuffle(ButtonsList);
        for (int index = 0; index < mButtonsCount; index++) {
            ButtonsList.get(index).setText(question.getAnswers().get(index));
        }
    }

    private void setOnClickListenerForButtons(Button button) {
        button.setOnAction(event -> buttonAction(button));
        button.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) buttonAction(button);
                }
        );
    }

    public void startTimer() {
        task = createTask();
        taskTread = new Thread(task);
        taskTread.start();

    }

    private Task<Boolean> createTask() {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                for (; mProgressValue < 10; ) {
                    try {
                        setProgressValue(++mProgressValue);
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                return true;
            }
        };
        task.setOnSucceeded(event -> mCallback.gameOver());
        task.setOnCancelled(event -> mCallback.gameOver());
        return task;
    }

    public interface Callback {
        void changeQuestion();

        void gameOver();
    }
}
