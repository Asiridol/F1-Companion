package com.asiri.f1companion.Models;

import com.asiri.f1companion.Services.Models.DriversModel;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asiri on 3/14/2016.
 */
public class Driver extends RealmObject
{
    @PrimaryKey
    private String driverId;
    private String dateOfBirth;
    private String nationality;
    private String familyName;
    private String givenName;
    private String code;
    private String constructorId;
    private String url;
    private String permanentNumber;
    private byte[] driverImage;

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

    public byte[] getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(byte[] driverImage) {
        this.driverImage = driverImage;
    }

    public void setObject(DriversModel.Driver driver)
    {
        this.code=driver.getCode();
        this.constructorId=driver.getConstructorId();
        this.dateOfBirth=driver.getDateOfBirth();
        this.driverId=driver.getDriverId();
        this.familyName=driver.getFamilyName();
        this.givenName=driver.getGivenName();
        this.url=driver.getUrl();
        this.nationality=driver.getNationality();
        this.permanentNumber=driver.getPermanentNumber();
    }

    @Override
    public String toString() {
        return "Driver{" +
                "dateOfBirth='" + dateOfBirth + '\'' +
                ", nationality='" + nationality + '\'' +
                ", driverId='" + driverId + '\'' +
                ", familyName='" + familyName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", code='" + code + '\'' +
                ", constructorId='" + constructorId + '\'' +
                ", url='" + url + '\'' +
                ", permanentNumber='" + permanentNumber + '\'' +
                '}';
    }
}
