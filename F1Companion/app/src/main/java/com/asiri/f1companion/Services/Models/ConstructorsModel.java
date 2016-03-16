package com.asiri.f1companion.Services.Models;

/**
 * Created by asiri on 3/16/2016.
 */
public class ConstructorsModel
{
    private Constructor[] constructors;

    public Constructor[] getConstructors ()
    {
        return constructors;
    }

    public void setConstructors (Constructor[] constructors)
    {
        this.constructors = constructors;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [constructors = "+constructors+"]";
    }

    public class Constructor
    {
        private String nationality;

        private String name;

        private String constructorId;

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

        @Override
        public String toString()
        {
            return "ClassPojo [nationality = "+nationality+", name = "+name+", constructorId = "+constructorId+"]";
        }
    }
}

