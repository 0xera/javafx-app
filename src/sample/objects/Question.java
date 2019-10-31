package sample.objects;

import java.util.Arrays;
import java.util.List;

public class Question {

    private String question;
    private List<String> answers;


    public Question(String question, String... answers) {
        this.question = question;
        this.answers = Arrays.asList(answers);
    }


    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return answers.get(0);
    }
}