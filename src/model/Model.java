package model;

import controller.Controller;

public class Model {
    private final Controller controller;
    private Fire fire;

    public Model(Controller controller) {
        this.controller = controller;
    }

    // -------------------------------------- LINKING METHODS --------------------------------------

    public void setFire() {
        this.fire = new Fire(this);
    }

    public int[][] getTemperaturesArray() {
        return fire.getTemperaturesArray();
    }

    public Thread getFireThread() {
        return fire.getThread();
    }

    public void startFire() {
        fire.startFire();
    }

    public void resumeFire() {
        fire.resumeFire();
    }

    public void pauseFire() {
        fire.pauseFire();
    }

    public void restartFire() {
        fire.restartFire();
    }
}
