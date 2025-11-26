package controller;

import model.Model;
import view.View;

public class Controller {
    private final View view;
    private final Model model;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);

        this.model.setFire();
        startFire();
        this.view.startRendering();
    }

    // ------------------------------------- LINKING METHODS -------------------------------------

    public int[][] getTemperaturesArray() {
        return model.getTemperaturesArray();
    }

    public Thread getFireThread() {
        return model.getFireThread();
    }

    public void startFire() {
        model.startFire();
    }

    public void resumeFire() {
        model.resumeFire();
    }

    public void pauseFire() {
        model.pauseFire();
    }

    public void restartFire() {
        model.restartFire();
    }
}
