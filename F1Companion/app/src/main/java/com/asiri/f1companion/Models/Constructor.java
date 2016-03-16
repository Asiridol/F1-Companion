package com.asiri.f1companion.Models;

import com.asiri.f1companion.Services.Models.ConstructorsModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asiri on 3/14/2016.
 */
public class Constructor extends RealmObject {

    @PrimaryKey
    private String constructorId;
    private RealmList<Driver> drivers;
    private String nationality;
    private String name;

    public RealmList<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(RealmList<Driver> drivers) {
        this.drivers = drivers;
    }

    public String getNationality ()
    {
        return nationality;
    }

    public void setNationality (String nationality)
    {
        this.nationality = nationality;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getConstructorId ()
    {
        return constructorId;
    }

    public void setConstructorId (String constructorId)
    {
        this.constructorId = constructorId;
    }

    public void setObject(ConstructorsModel.Constructor constructor)
    {
        this.nationality=constructor.getNationality();
        this.name=constructor.getName();
        this.constructorId=constructor.getConstructorId();
    }

    @Override
    public String toString() {
        return "Constructor{" +
                "constructorId='" + constructorId + '\'' +
                ", drivers=" + drivers.toString() +
                ", nationality='" + nationality + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
