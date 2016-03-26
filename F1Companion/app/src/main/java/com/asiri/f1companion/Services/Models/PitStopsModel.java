package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/25/2016.
 */
public class PitStopsModel {
        private PitStop[] pitStops;

        private String driverId;

        public PitStop[] getPitStops ()
        {
            return pitStops;
        }

        public void setPitStops (PitStop[] pitStops)
        {
            this.pitStops = pitStops;
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
            return "ClassPojo [pitStops = "+pitStops+", driverId = "+driverId+"]";
        }

    public class PitStop
    {
        private String Time;

        private String Duration;

        private String Lap;

        private String Stop;

        public String getTime ()
        {
            return Time;
        }

        public void setTime (String Time)
        {
            this.Time = Time;
        }

        public String getDuration ()
        {
            return Duration;
        }

        public void setDuration (String Duration)
        {
            this.Duration = Duration;
        }

        public String getLap ()
        {
            return Lap;
        }

        public void setLap (String Lap)
        {
            this.Lap = Lap;
        }

        public String getStop ()
        {
            return Stop;
        }

        public void setStop (String Stop)
        {
            this.Stop = Stop;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Time = "+Time+", Duration = "+Duration+", Lap = "+Lap+", Stop = "+Stop+"]";
        }
    }
}
