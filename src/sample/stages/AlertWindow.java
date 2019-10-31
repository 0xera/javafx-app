package sample.stages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class AlertWindow {
    private static Button mExitButton = new Button("EXIT MENU");
    private static TextField mTextField = new TextField();
    private static Text mText = new Text("Enter your name");
    private static Callback mCallback;
    private static Stage mWindow;


    public static void display(Callback callback, String text) {
        mCallback = callback;
        if (mWindow == null) {
            initWindow(text);
        } else {
            mWindow.setTitle(text);
            mWindow.show();
        }
    }

    private static void initWindow(String text) {
        mWindow = new Stage();
        mWindow.setTitle(text);
        mWindow.initModality(Modality.APPLICATION_MODAL);
        mWindow.setScene(new Scene(createContent()));
        mWindow.setResizable(false);
        mWindow.show();
    }

    private static Parent createContent() {
        VBox root = new VBox();
        initActions();
        root.getChildren().addAll(mText, mTextField, mExitButton);
        root.setPrefSize(250, 100);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 10, 10, 10));
        return root;
    }

    private static void initActions() {
        mExitButton.setOnAction(event -> {
            mCallback.exitGame();
            try {
                mCallback.saveGamer(mTextField.getText());
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            AlertWindow.mWindow.close();
        });
        mTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                mCallback.exitGame();
                try {
                    mCallback.saveGamer(mTextField.getText());
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                AlertWindow.mWindow.close();
            }
        });
        AlertWindow.mWindow.setOnCloseRequest(event -> {
            mCallback.exitGame();
            try {
                mCallback.saveGamer(mTextField != null ? mTextField.getText() : null);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    interface Callback {
        void exitGame();

        void saveGamer(String name) throws FileNotFoundException, UnsupportedEncodingException;
    }
}

