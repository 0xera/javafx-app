package sample.objects;

import java.io.Serializable;

public class User extends Human implements Player, Serializable {
    private String name;
    private int points;

    public User(String name, int points) {
        this.name = name;
        this.points = points;
    }
    public User(){}

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void increasePoints(int points) {
        this.points += points;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }
}
