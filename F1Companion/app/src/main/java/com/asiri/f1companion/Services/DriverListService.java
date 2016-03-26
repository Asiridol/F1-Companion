package com.asiri.f1companion.Services;

import android.content.Context;
import android.preference.PreferenceActivity;

import com.asiri.f1companion.Models.Constructor;
import com.asiri.f1companion.Models.Driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

/**
 * Created by asiri on 3/19/2016.
 */
public class DriverListService
{
    public ArrayList<DriverRowModel> getTableUIList(Context context)
    {
        ArrayList<DriverRowModel> rows=new ArrayList<DriverRowModel>();

        List<Constructor> teams= Realm.getInstance(context).where(Constructor.class).findAll();

        for (Constructor team:teams){
            DriverRowModel headerRow=new DriverRowModel();
            headerRow.setRowType(headerRow.HEADER);
            headerRow.getData().put("Title",team.getName());
            headerRow.getData().put("Nationality",team.getNationality());

            rows.add(headerRow);

            for (Driver d:team.getDrivers())
            {
                DriverRowModel itemRow=new DriverRowModel();
                itemRow.setRowType(itemRow.ITEM);

                itemRow.getData().put("Name",d.getGivenName() + " " + d.getFamilyName());
                itemRow.getData().put("Code",d.getCode());
                itemRow.getData().put("Num",d.getPermanentNumber());
                itemRow.getData().put("Id",d.getDriverId());
                rows.add(itemRow);
            }
        }

        return rows;
    }

    public class DriverRowModel
    {
        public int HEADER=0;
        public int ITEM=1;
        private int rowType=0;
        private HashMap<String,String> data=new HashMap<String,String>();

        public int getRowType() {
            return rowType;
        }

        public void setRowType(int rowType) {
            this.rowType = rowType;
        }

        public HashMap<String, String> getData() {
            return data;
        }

        public void setData(HashMap<String, String> data) {
            this.data = data;
        }
    }
}
