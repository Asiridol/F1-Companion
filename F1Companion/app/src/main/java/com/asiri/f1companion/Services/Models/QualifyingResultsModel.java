package com.asiri.f1companion.Services.Models;

import java.io.Serializable;

/**
 * Created by asiri on 3/22/2016.
 */
public class QualifyingResultsModel implements Serializable {
    private QualifyingResult[] qualifyingResults;

    public QualifyingResult[] getQualifyingResults ()
    {
        return qualifyingResults;
    }

    public void setQualifyingResults (QualifyingResult[] qualifyingResults)
    {
        this.qualifyingResults = qualifyingResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [qualifyingResults = "+qualifyingResults+"]";
    }

    public class QualifyingResult
    {
        private String Q1;

        private String Q2;

        private String Q3;

        private String driverId;

        public String getQ1 ()
        {
            return Q1;
        }

        public void setQ1 (String Q1)
        {
            this.Q1 = Q1;
        }

        public String getQ2 ()
        {
            return Q2;
        }

        public void setQ2 (String Q2)
        {
            this.Q2 = Q2;
        }

        public String getQ3 ()
        {
            return Q3;
        }

        public void setQ3 (String Q3)
        {
            this.Q3 = Q3;
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
            return "ClassPojo [Q1 = "+Q1+", Q2 = "+Q2+", Q3 = "+Q3+", driverId = "+driverId+"]";
        }
    }
}

