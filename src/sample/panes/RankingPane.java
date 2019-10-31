package sample.panes;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.objects.User;

public class RankingPane extends VBox {
    private Callback mCallback;
    private TableView<User> mUserTableView = new TableView<>();
    private final Button mButtonBack = new Button();
    private static RankingPane instance;
    private final TableColumn<User, Integer> pointsColumn = new TableColumn<>("Points");
    private final TableColumn<User, String> nameColumn = new TableColumn<>("Name");

    public static RankingPane geInstance(Callback callback, ObservableList<User> usersList) {
        return (instance == null) ? instance = new RankingPane(callback, usersList) : instance;
    }

    private RankingPane(Callback callback, ObservableList<User> usersList) {
        mCallback = callback;
        setPrefSize(500, 300);
        getChildren().addAll(createTableView(usersList), createBackButton());

    }

    private HBox createBackButton() {
        HBox hBox = new HBox();
        mButtonBack.setText("Back");
        mButtonBack.setPrefWidth(120);
        mButtonBack.setOnAction(event -> {
            mCallback.exitGame();
        });
        mButtonBack.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) mCallback.exitGame();
        });
        hBox.getChildren().addAll(mButtonBack);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private VBox createTableView(ObservableList<User> usersList) {
        VBox vBox = new VBox();
        nameColumn.setMinWidth(249);
        nameColumn.setMaxWidth(249);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        pointsColumn.setMinWidth(249);
        pointsColumn.setMaxWidth(249);
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        mUserTableView.setItems(usersList);
        mUserTableView.getColumns().addAll(nameColumn, pointsColumn);

        vBox.getChildren().addAll(mUserTableView);
        return vBox;
    }

    public void setUserList(ObservableList<User> usersList) {
        mUserTableView.setItems(usersList);


    }

    public interface Callback {

        void exitGame();
    }
}
