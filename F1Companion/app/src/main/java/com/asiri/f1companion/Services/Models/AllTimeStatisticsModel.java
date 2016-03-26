package com.asiri.f1companion.Services.Models;

import java.io.Serializable;

/**
 * Created by asiri on 3/25/2016.
 */
public class AllTimeStatisticsModel implements Serializable {
    private Season[] seasons;

    private String driverId;

    public Season[] getSeasons ()
    {
        return seasons;
    }

    public void setSeasons (Season[] seasons)
    {
        this.seasons = seasons;
    }

    public String getDriverId ()
    {
        return driverId;
    }

    public void setDriverId (String driverId)
    {
        this.driverId = driverId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [seasons = "+seasons+", driverId = "+driverId+"]";
    }

    public class Season
    {
        private String position;

        private String season;

        private String rounds;

        private String points;

        private String constructor;

        private String wins;

        public String getPosition ()
        {
            return position;
        }

        public void setPosition (String position)
        {
            this.position = position;
        }

        public String getSeason ()
        {
            return season;
        }

        public void setSeason (String season)
        {
            this.season = season;
        }

        public String getRounds ()
        {
            return rounds;
        }

        public void setRounds (String rounds)
        {
            this.rounds = rounds;
        }

        public String getPoints ()
        {
            return points;
        }

        public void setPoints (String points)
        {
            this.points = points;
        }

        public String getConstructor ()
        {
            return constructor;
        }

        public void setConstructor (String constructor)
        {
            this.constructor = constructor;
        }

        public String getWins ()
        {
            return wins;
        }

        public void setWins (String wins)
        {
            this.wins = wins;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [position = "+position+", season = "+season+", rounds = "+rounds+", points = "+points+", constructor = "+constructor+", wins = "+wins+"]";
        }
    }
}
