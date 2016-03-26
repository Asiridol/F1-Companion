package com.asiri.f1companion.Services.Models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by asiri on 3/25/2016.
 */
public class AllStatusesModel implements Serializable {
    private String driver_Id;

    private HashMap<String,Integer>[] carFaults;

    private String driverId;

    private HashMap<String,Integer>[] finished;

    private HashMap<String,Integer>[] driverFaults;

    public String getDriver_Id ()
    {
        return driver_Id;
    }

    public void setDriver_Id (String driver_Id)
    {
        this.driver_Id = driver_Id;
    }

    public HashMap<String, Integer>[] getCarFaults() {
        return carFaults;
    }

    public void setCarFaults(HashMap<String, Integer>[] carFaults) {
        this.carFaults = carFaults;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public HashMap<String, Integer>[] getFinished() {
        return finished;
    }

    public void setFinished(HashMap<String, Integer>[] finished) {
        this.finished = finished;
    }

    public HashMap<String, Integer>[] getDriverFaults() {
        return driverFaults;
    }

    public void setDriverFaults(HashMap<String, Integer>[] driverFaults) {
        this.driverFaults = driverFaults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [driver_Id = "+driver_Id+", carFaults = "+carFaults+", driverId = "+driverId+", finished = "+finished+", driverFaults = "+driverFaults+"]";
    }
}
