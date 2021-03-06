package com.mobilesolutionworks.android.bolts;

import android.os.Bundle;

import com.mobilesolutionworks.android.app.WorksController;
import com.mobilesolutionworks.android.app.WorksControllerManager;

import java.util.Map;

/**
 * Created by yunarta on 19/11/15.
 */
public class MapWorksController extends WorksController
{
    Map<String, Object> mInstances;

    void putAll(Map<String, Object> instances)
    {
        mInstances = instances;
    }

    public Object get(String name)
    {
        return mInstances.get(name);
    }

    public Object set(String name, Object object)
    {
        return mInstances.put(name, object);
    }

    public static class ManagerCallback implements WorksControllerManager.ControllerCallbacks<MapWorksController>
    {
        @Override
        public MapWorksController onCreateController(int id, Bundle args)
        {
            return new MapWorksController();
        }

        @Override
        public void onCreated(int id, MapWorksController controller)
        {

        }

        @Override
        public void onReset(MapWorksController loader)
        {

        }
    }
}