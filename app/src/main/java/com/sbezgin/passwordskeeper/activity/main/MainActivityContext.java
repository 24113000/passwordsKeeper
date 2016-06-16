package com.sbezgin.passwordskeeper.activity.main;

import com.sbezgin.passwordskeeper.service.properties.PropertiesDataHolder;

/**
 * Created by sbezgin on 11.06.2016.
 */
public class MainActivityContext {

    private PropertiesDataHolder propertiesDataHolder;
    private MainState currentState = MainState.DATA_NOT_SET;

    private static MainActivityContext ourInstance = new MainActivityContext();

    private boolean flagReturnMain;
    private PathHolder pathHolder;

    public static MainActivityContext getInstance() {
        return ourInstance;
    }

    private MainActivityContext() {
    }

    public PropertiesDataHolder getPropertiesDataHolder() {
        return propertiesDataHolder;
    }

    public void setPropertiesDataHolder(PropertiesDataHolder propertiesDataHolder) {
        this.propertiesDataHolder = propertiesDataHolder;
    }

    public void inValidate() {
        propertiesDataHolder = null;
    }

    public boolean isDataValid() {
        return propertiesDataHolder != null;
    }

    public MainState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(MainState currentState) {
        this.currentState = currentState;
    }

    public void setFlagReturnMain() {
        this.flagReturnMain = true;
    }

    public boolean isFlagReturnMain() {
        return flagReturnMain;
    }

    public void resetFlagReturnMain() {
        this.flagReturnMain = false;
    }

    public void setPathHolder(PathHolder pathHolder) {
        this.pathHolder = pathHolder;
    }

    public PathHolder getPathHolder() {
        return pathHolder;
    }
}
