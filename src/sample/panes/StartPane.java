package sample.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.stages.Main;

public class StartPane extends VBox {
    private final String START = "Start";
    private final String RANKING = "Raking";
    private final String WWTBTM = "Who wants to be the m";
    private final String EXIT = "Exit";
    private Button mStarButton = new Button();
    private Button mShowRakingButton = new Button();
    private Button mExitButton = new Button();
    private Text mText = new Text();
    private Callback mCallback;
    private static StartPane instance;

    public static StartPane getInstance(Callback callback) {
        return (instance == null) ? instance = new StartPane(callback) : instance;
    }

    private StartPane(Callback callback) {
        mCallback = callback;
        setPrefSize(500, 300);
        setAlignment(Pos.CENTER);
        getChildren().addAll(createText(), createButtons());

    }

    private HBox createText() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(20, 20, 20, 20));
        mText.setText(WWTBTM);
        mText.setFont(Main.FONT);
        hBox.getChildren().add(mText);
        return hBox;
    }

    private HBox createButtons() {
        HBox hbox = new HBox();
        mStarButton.setPrefWidth(120);
        mShowRakingButton.setPrefWidth(120);
        mExitButton.setPrefWidth(120);
        mExitButton.setText(EXIT);
        mStarButton.setText(START);
        mShowRakingButton.setText(RANKING);
        setOnClickListenerForButtons();
        hbox.getChildren().addAll(mStarButton, mShowRakingButton, mExitButton);
        hbox.setAlignment(Pos.CENTER);
        return hbox;

    }

    private void setOnClickListenerForButtons() {
        mStarButton.setOnAction(event -> {
            mCallback.startGameReady();
        });
        mShowRakingButton.setOnAction(event -> {
            mCallback.openRating();
        });
        mExitButton.setOnAction(event -> {
            mCallback.exitApp();
        });
        mExitButton.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                mCallback.exitApp();
            }
        });
        mStarButton.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                mCallback.startGameReady();
            }
        });
        mShowRakingButton.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                mCallback.openRating();
            }
        });
    }

    public interface Callback {
        void openRating();

        void startGameReady();

        void exitApp();
    }
}
