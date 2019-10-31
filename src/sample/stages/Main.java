package sample.stages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.objects.Question;
import sample.objects.User;
import sample.panes.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


@SuppressWarnings("ALL")
public class Main extends Application {
    public static final Font FONT = Font.font(25);
    private final String PATH_TO_QUESTIONS = "/sample/resource/QuestionsMap";
    private final String PATH_TO_RANKING = "UsersRanking.txt";
    private Stage mainStage;
    private User user;
    private ObservableList<User> mUsersList;
    private Map<Integer, Question> questionMap = new HashMap<>();
    private CallbackСatcher callbackСatcher = new CallbackСatcher();
    private QuestionPane questionPane = QuestionPane.getInstance(callbackСatcher);
    private SidePane sidePane = SidePane.geInstance(callbackСatcher);
    private StartPane startPane = StartPane.getInstance(callbackСatcher);
    private WaitTheQPane waitTheQPane = WaitTheQPane.getInstance();
    private RankingPane rankingPane = RankingPane.geInstance(callbackСatcher, mUsersList);
    private Scene startPaneScene = new Scene(startPane);
    private Scene waitTheQPaneScene = new Scene(waitTheQPane);
    private Scene rankingPaneScene = new Scene(rankingPane);
    private Question emptyQuestion = new Question("", "", "", "", "", "");
    private Task<Boolean> taskReadFile = new Task<Boolean>() {
        @Override
        protected Boolean call() throws Exception {
            return readMapOfQuestion();
        }
    };
    private Task<Boolean> taskWriteFile = new Task<Boolean>() {
        @Override
        protected Boolean call() throws Exception {
            return writeFile();
        }
    };
    private Type itemsMapType = new TypeToken<Map<Integer, Question>>() {
    }.getType();
    private Type itemsListType = new TypeToken<ArrayList<User>>() {
    }.getType();
    private Comparator<User> comparator = Comparator.comparingInt(User::getPoints);
    public static int currentQuestion = 1;
    private final Gson gson = new Gson();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Thread(taskReadFile).start();
        callbackСatcher.addObserver(sidePane);
        createStartStage(primaryStage);
    }

    private Boolean readMapOfQuestion() throws IOException {
        questionMap = gson.fromJson(readQuestionsJsonFile(), itemsMapType);
        ArrayList<User> arrayList = gson.fromJson(readUserJsonFile(), itemsListType);
        if (arrayList == null) {
            mUsersList = FXCollections.observableArrayList();
        } else {
            mUsersList = FXCollections.observableArrayList(arrayList);
            mUsersList.sort(comparator.reversed());
            rankingPane.setUserList(mUsersList);
        }
        return true;
    }

    private String readUserJsonFile() throws IOException, FileNotFoundException {
        File file = new File(PATH_TO_RANKING);
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String string = readFileToString(reader);
        reader.close();
        return string;
    }


    private String readQuestionsJsonFile() throws IOException {
        InputStream is = this.getClass().getResourceAsStream(PATH_TO_QUESTIONS);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String string = readFileToString(reader);
        reader.close();
        return string;
    }

    private String readFileToString(BufferedReader reader) throws IOException {
        String string;
        StringBuilder stringBuilder = new StringBuilder();
        while ((string = reader.readLine()) != null) {
            stringBuilder.append(string);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private void createStartStage(Stage primaryStage) {
        mainStage = primaryStage;
        mainStage.setResizable(false);
        mainStage.setTitle("WWTBTM");
        mainStage.setScene(startPaneScene);
        mainStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        mainStage.show();
    }

    private Scene createGameScene() {
        HBox root = new HBox();
        root.setPrefSize(500, 300);
        root.getChildren().addAll(questionPane, sidePane);
        return new Scene(root);
    }

    private Boolean writeFile() throws FileNotFoundException {
        PrintWriter pw;
        pw = new PrintWriter(PATH_TO_RANKING);
        pw.write("");
        pw.write(gson.toJson(mUsersList));
        pw.close();
        return true;
    }

    public void preExitGame() {
        questionPane.setQuestion(emptyQuestion);
        sidePane.moveToFirst();
        if (currentQuestion == 15) AlertWindow.display(callbackСatcher, "You win");
        else AlertWindow.display(callbackСatcher, "You loose");
        currentQuestion = 1;

    }

    private class CallbackСatcher extends Observable implements QuestionPane.Callback, SidePane.Callback, StartPane.Callback, AlertWindow.Callback, RankingPane.Callback {

        private Task<Boolean> booleanTask;

        @Override
        public void changeQuestion() {
            user.increasePoints(10 - questionPane.getProgressValue());
            if (currentQuestion < 15) currentQuestion++;
            questionPane.setQuestion(questionMap.get(currentQuestion));
            questionPane.setProgressValue(0);
            setChanged();
            notifyObservers();

        }


        @Override
        public void selectNextQuestion() {
            if (sidePane.getCurrent() == 15) return;
            Text text = (Text) sidePane.getChildren().get(15 - sidePane.getCurrent());
            text.setFill(Color.GRAY);
            sidePane.setCurrent(sidePane.getCurrent() + 1);
            text = (Text) sidePane.getChildren().get(15 - sidePane.getCurrent());
            text.setFill(Color.BLACK);
        }

        @Override
        public void openRating() {
            mainStage.setScene(rankingPaneScene);
        }

        @Override
        public void startGameReady() {
            mainStage.setScene(waitTheQPaneScene);
            questionPane.setQuestion(questionMap.get(currentQuestion));
            user = new User();
            booleanTask = waitTheQPane.startTimer();
            startGame();
        }

        private void startGame() {
            booleanTask.setOnSucceeded(event -> {
                waitTheQPane.setProgressValue(0);
                mainStage.setScene(createGameScene());
                questionPane.startTimer();
            });
        }

        @Override
        public void gameOver() {
            preExitGame();
        }

        @Override
        public void exitApp() {
            Platform.exit();
            System.exit(0);
        }

        @Override
        public void exitGame() {
            mainStage.setScene(startPaneScene);
        }

        @Override
        public void saveGamer(String name) throws FileNotFoundException {
            if (!name.equals("")) {
                user.setName(name);
                mUsersList.add(user);
                mUsersList.sort(comparator.reversed());
                rankingPane.setUserList(mUsersList);
                new Thread(taskWriteFile);
            }
        }
    }
}

