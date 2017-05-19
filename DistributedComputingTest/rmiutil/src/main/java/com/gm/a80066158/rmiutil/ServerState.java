package com.gm.a80066158.rmiutil;

import java.io.Serializable;

/**
 * Created by 80066158 on 2017-05-19.
 */

public class ServerState implements Serializable {
    private float freeMemory = 100;
    private float freeCpu = 100;

    public float getFreeCpu() {
        return freeCpu;
    }

    public void setFreeCpu(float freeCpu) {
        this.freeCpu = freeCpu;
    }

    public float getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(float freeMemory) {
        this.freeMemory = freeMemory;
    }
}
