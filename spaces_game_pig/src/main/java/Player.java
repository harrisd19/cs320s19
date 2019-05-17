import com.gigaspaces.annotation.pojo.SpaceId;

import java.io.Serializable;

public class Player implements Serializable {

    private String  name;
    private Integer points;
    private Integer round;

    public Player() {
        this.name = "";
        this.points = 0;
        this.round = 1;
    }

    public Player(String name) {
        this.name = name;
        this.points = 0;
        this.round = 1;
    }

    @SpaceId
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", points=" + points +
                ", round=" + round +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Player other = (Player) obj;
        return name.equalsIgnoreCase(other.name);
    }
}
