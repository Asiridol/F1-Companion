package com.asiri.f1companion.Models;

import com.asiri.f1companion.Services.Models.ConstructorsModel;
import com.asiri.f1companion.Services.Models.LeaderboardsModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asiri on 3/14/2016.
 */
public class Leaderboard extends RealmObject{
    private String points;
    private String position;
    private String wins;

    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "points='" + points + '\'' +
                ", position='" + position + '\'' +
                ", wins='" + wins + '\'' +
                ", driver=" + driver.toString() +
                '}';
    }

    public void setObject(LeaderboardsModel.Leaderboard leaderboard)
    {
        this.points= leaderboard.getPoints();
        this.wins=leaderboard.getWins();
        this.position=leaderboard.getPosition();
    }
}
