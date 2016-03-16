package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/16/2016.
 */
public class DriversModel
{
    private Driver[] drivers;

    public Driver[] getDrivers ()
    {
        return drivers;
    }

    public void setDrivers (Driver[] drivers)
    {
        this.drivers = drivers;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [drivers = "+drivers+"]";
    }

    public class Driver
    {
        private String dateOfBirth;

        private String nationality;

        private String driverId;

        private String familyName;

        private String givenName;

        private String code;

        private String constructorId;

        private String url;

        private String permanentNumber;

        public String getDateOfBirth ()
        {
            return dateOfBirth;
        }

        public void setDateOfBirth (String dateOfBirth)
        {
            this.dateOfBirth = dateOfBirth;
        }

        public String getNationality ()
        {
            return nationality;
        }

        public void setNationality (String nationality)
        {
            this.nationality = nationality;
        }

        public String getDriverId ()
        {
            return driverId;
        }

        public void setDriverId (String driverId)
        {
            this.driverId = driverId;
        }

        public String getFamilyName ()
        {
            return familyName;
        }

        public void setFamilyName (String familyName)
        {
            this.familyName = familyName;
        }

        public String getGivenName ()
        {
            return givenName;
        }

        public void setGivenName (String givenName)
        {
            this.givenName = givenName;
        }

        public String getCode ()
        {
            return code;
        }

        public void setCode (String code)
        {
            this.code = code;
        }

        public String getConstructorId ()
        {
            return constructorId;
        }

        public void setConstructorId (String constructorId)
        {
            this.constructorId = constructorId;
        }

        public String getUrl ()
        {
            return url;
        }

        public void setUrl (String url)
        {
            this.url = url;
        }

        public String getPermanentNumber ()
        {
            return permanentNumber;
        }

        public void setPermanentNumber (String permanentNumber)
        {
            this.permanentNumber = permanentNumber;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [dateOfBirth = "+dateOfBirth+", nationality = "+nationality+", driverId = "+driverId+", familyName = "+familyName+", givenName = "+givenName+", code = "+code+", constructorId = "+constructorId+", url = "+url+", permanentNumber = "+permanentNumber+"]";
        }
    }
}
