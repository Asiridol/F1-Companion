package com.asiri.f1companion.Models;

import com.asiri.f1companion.Services.Models.RacesModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asiri on 3/14/2016.
 */
public class Race extends RealmObject {

    @PrimaryKey
    private String round;
    private String raceName;
    private String circuitName;
    private String country;
    private String date;
    private String time;

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Race{" +
                "round='" + round + '\'' +
                ", raceName='" + raceName + '\'' +
                ", circuitName='" + circuitName + '\'' +
                ", country='" + country + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public void setObject(RacesModel.Race race)
    {
        this.circuitName=race.getCircuit();
        this.country=race.getCountry();
        this.date=race.getDate();
        this.raceName=race.getRaceName();
        this.round=race.getRound();
        this.time=race.getTime();
    }
}
