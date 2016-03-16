package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/16/2016.
 */
public class RacesModel {
        private Race[] races;

        public Race[] getRaces ()
        {
            return races;
        }

        public void setRaces (Race[] races)
        {
            this.races = races;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [races = "+races+"]";
        }

    public class Race
    {
        private String time;

        private String raceName;

        private String circuit;

        private String round;

        private String date;

        private String country;

        public String getTime ()
        {
            return time;
        }

        public void setTime (String time)
        {
            this.time = time;
        }

        public String getRaceName ()
        {
            return raceName;
        }

        public void setRaceName (String raceName)
        {
            this.raceName = raceName;
        }

        public String getCircuit ()
        {
            return circuit;
        }

        public void setCircuit (String circuit)
        {
            this.circuit = circuit;
        }

        public String getRound ()
        {
            return round;
        }

        public void setRound (String round)
        {
            this.round = round;
        }

        public String getDate ()
        {
            return date;
        }

        public void setDate (String date)
        {
            this.date = date;
        }

        public String getCountry ()
        {
            return country;
        }

        public void setCountry (String country)
        {
            this.country = country;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [time = "+time+", raceName = "+raceName+", circuit = "+circuit+", round = "+round+", date = "+date+", country = "+country+"]";
        }
    }
}
