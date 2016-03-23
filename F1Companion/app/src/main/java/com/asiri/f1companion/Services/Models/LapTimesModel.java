package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/23/2016.
 */
public class LapTimesModel
{
    private String driverId;

    private LapTime[] lapTimes;

    public String getDriverId ()
    {
        return driverId;
    }

    public void setDriverId (String driverId)
    {
        this.driverId = driverId;
    }

    public LapTime[] getLapTimes ()
    {
        return lapTimes;
    }

    public void setLapTimes (LapTime[] lapTimes)
    {
        this.lapTimes = lapTimes;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [driverId = "+driverId+", lapTimes = "+lapTimes+"]";
    }

    public class LapTime
    {
        private String position;

        private String time;

        private String lap;

        public String getPosition ()
        {
            return position;
        }

        public void setPosition (String position)
        {
            this.position = position;
        }

        public String getTime ()
        {
            return time;
        }

        public void setTime (String time)
        {
            this.time = time;
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
            return "ClassPojo [position = "+position+", time = "+time+", lap = "+lap+"]";
        }
    }
}