package com.asiri.f1companion.Services.Interfaces;

import com.asiri.f1companion.Commons.Defaults;

import java.util.Objects;

/**
 * Created by asiri on 5/8/2016.
 */
public interface LoadDataListener
{
    public void finishedLoadingData();

    public void finishedLoadingDataWith(Object object, Defaults.RequestType requestType);

    public void finishedLoadingDataWithError(String error);

}
