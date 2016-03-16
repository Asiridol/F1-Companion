package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/16/2016.
 */
public class RaceResultsModel
{
    private RaceResult[] raceResult;

    public RaceResult[] getRaceResult ()
    {
        return raceResult;
    }

    public void setRaceResult (RaceResult[] raceResult)
    {
        this.raceResult = raceResult;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [raceResult = "+raceResult+"]";
    }

    public class RaceResult
    {
        private String position;

        private String laps;

        private String time;

        private String status;

        private Fastest Fastest;

        private String driverId;

        private String grid;

        private String points;

        public String getPosition ()
        {
            return position;
        }

        public void setPosition (String position)
        {
            this.position = position;
        }

        public String getLaps ()
        {
            return laps;
        }

        public void setLaps (String laps)
        {
            this.laps = laps;
        }

        public String getTime ()
        {
            return time;
        }

        public void setTime (String time)
        {
            this.time = time;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public Fastest getFastest ()
        {
            return Fastest;
        }

        public void setFastest (Fastest Fastest)
        {
            this.Fastest = Fastest;
        }

        public String getDriverId ()
        {
            return driverId;
        }

        public void setDriverId (String driverId)
        {
            this.driverId = driverId;
        }

        public String getGrid ()
        {
            return grid;
        }

        public void setGrid (String grid)
        {
            this.grid = grid;
        }

        public String getPoints ()
        {
            return points;
        }

        public void setPoints (String points)
        {
            this.points = points;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [position = "+position+", laps = "+laps+", time = "+time+", status = "+status+", Fastest = "+Fastest+", driverId = "+driverId+", grid = "+grid+", points = "+points+"]";
        }
    }

    public class Fastest
    {
        private String time;

        private String rank;

        private String speed;

        private String lap;

        public String getTime ()
        {
            return time;
        }

        public void setTime (String time)
        {
            this.time = time;
        }

        public String getRank ()
        {
            return rank;
        }

        public void setRank (String rank)
        {
            this.rank = rank;
        }

        public String getSpeed ()
        {
            return speed;
        }

        public void setSpeed (String speed)
        {
            this.speed = speed;
        }

        public String getLap ()
        {
            return lap;
        }

        public void setLap (String lap)
        {
            this.lap = lap;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [time = "+time+", rank = "+rank+", speed = "+speed+", lap = "+lap+"]";
        }
    }
}
