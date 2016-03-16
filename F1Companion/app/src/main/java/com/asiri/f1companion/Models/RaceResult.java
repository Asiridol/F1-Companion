package com.asiri.f1companion.Models;

import io.realm.annotations.PrimaryKey;

/**
 * Created by asiri on 3/14/2016.
 */
public class RaceResult {

    @PrimaryKey
    private String gridPosition;
    private String status;
    private String points;
    private String position;
    private String averageSpeed;
    private FastestLap fastestLap;

    private Driver driver;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    class FastestLap
    {
        private String rank;
        private String lap;
        private String time;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getLap() {
            return lap;
        }

        public void setLap(String lap) {
            this.lap = lap;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public String getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(String gridPosition) {
        this.gridPosition = gridPosition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public FastestLap getFastestLap() {
        return fastestLap;
    }

    public void setFastestLap(FastestLap fastestLap) {
        this.fastestLap = fastestLap;
    }

    @Override
    public String toString() {
        return "RaceResult{" +
                "gridPosition='" + gridPosition + '\'' +
                ", status='" + status + '\'' +
                ", points='" + points + '\'' +
                ", position='" + position + '\'' +
                ", averageSpeed='" + averageSpeed + '\'' +
                ", fastestLap=" + fastestLap +
                ", driver=" + driver +
                '}';
    }

    public void setObject()
    {

    }

}
