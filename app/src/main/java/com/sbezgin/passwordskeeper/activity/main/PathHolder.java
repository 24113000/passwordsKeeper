package com.sbezgin.passwordskeeper.activity.main;

/**
 * Created by sbezgin on 11.06.2016.
 */
public class PathHolder {
    private String group;
    private String name;
    private int level;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public int setNextLevel() {
        if (level < 2) {
            level++;
        }
        return level;
    }

    public int setPreviousLevel() {
        if (level > 0) {
            level--;
        }
        return level;
    }
}
