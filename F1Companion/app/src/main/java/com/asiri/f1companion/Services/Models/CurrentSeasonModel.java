package com.asiri.f1companion.Services.Models;

import java.io.Serializable;

/**
 * Created by asiri on 3/25/2016.
 */
public class CurrentSeasonModel implements Serializable {
    private String driverId;

    private RoundData[] roundData;

    public String getDriverId ()
    {
        return driverId;
    }

    public void setDriverId (String driverId)
    {
        this.driverId = driverId;
    }

    public RoundData[] getRoundData ()
    {
        return roundData;
    }

    public void setRoundData (RoundData[] roundData)
    {
        this.roundData = roundData;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [driverId = "+driverId+", roundData = "+roundData+"]";
    }


    public class RoundData
    {
        private String position;

        private String track;

        private String round;

        private String points;

        private String date;

        public String getPosition ()
        {
            return position;
        }

        public void setPosition (String position)
        {
            this.position = position;
        }

        public String getTrack ()
        {
            return track;
        }

        public void setTrack (String track)
        {
            this.track = track;
        }

        public String getRound ()
        {
            return round;
        }

        public void setRound (String round)
        {
            this.round = round;
        }

        public String getPoints ()
        {
            return points;
        }

        public void setPoints (String points)
        {
            this.points = points;
        }

        public String getDate ()
        {
            return date;
        }

        public void setDate (String date)
        {
            this.date = date;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [position = "+position+", track = "+track+", round = "+round+", points = "+points+", date = "+date+"]";
        }
    }
}
