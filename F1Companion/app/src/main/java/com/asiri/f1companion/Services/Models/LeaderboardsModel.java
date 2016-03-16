package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/16/2016.
 */
public class LeaderboardsModel
{
    private Leaderboard[] leaderboard;

    public Leaderboard[] getLeaderboard ()
    {
        return leaderboard;
    }

    public void setLeaderboard (Leaderboard[] leaderboard)
    {
        this.leaderboard = leaderboard;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [leaderboard = "+leaderboard+"]";
    }

    public class Leaderboard
    {
        private String position;

        private String driverId;

        private String points;

        private String wins;

        public String getPosition ()
        {
            return position;
        }

        public void setPosition (String position)
        {
            this.position = position;
        }

        public String getDriverId ()
        {
            return driverId;
        }

        public void setDriverId (String driverId)
        {
            this.driverId = driverId;
        }

        public String getPoints ()
        {
            return points;
        }

        public void setPoints (String points)
        {
            this.points = points;
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
            return "ClassPojo [position = "+position+", driverId = "+driverId+", points = "+points+", wins = "+wins+"]";
        }
    }
}
