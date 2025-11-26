package model;

import static java.lang.Thread.sleep;

public class Fire implements Runnable {
    private final Model model;
    private Thread thread;
    private boolean running;
    private boolean playing;
    private final int[][] pixels;

    public Fire(Model model) {
        this.model = model;
        this.running = false;
        this.playing = false;
        this.thread = null;
        this.pixels = new int[120][120];
    }

    // ------------------------------------- GETTERS & SETTERS -------------------------------------

    public int[][] getTemperaturesArray() {
        return pixels;
    }

    public Thread getThread() {
        return this.thread;
    }

    // ------------------------------------- THREAD MANAGING -------------------------------------

    public void startFire() {
        running = true;
        playing = true;
        this.thread = new Thread(this);
        thread.setName("Fire model thread - Calculates pixel temperatures");
        thread.start();
    }

    public void resumeFire() {
        playing = true;
    }

    public void pauseFire() {
        playing = false;
    }

    public void restartFire() {
        running = false;
        playing = false;

        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }

        thread = null;
        startFire();
    }

    // -------------------------------------- SIMULATION --------------------------------------

    @Override
    public void run() {
        final double decayingfactor = 0.985;
        final float sparkChance = 0.25f;
        final int pixelsHeight = pixels.length;
        final int pixelsWidth = pixels[0].length;

        while (running) {
            if (playing) {
                generateSparks(sparkChance); // 10% chance of generating a spark in each position
                propagateHeat(decayingfactor, pixelsHeight, pixelsWidth);
                coolLowerRow(pixelsHeight, pixelsWidth);
            }

            try {
                sleep(30);
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * Generates sparks randomly on each pixel of the lower line of the array
     * @param chance Chance of generating the spark on each pixel (1.00 = 100%, 0.5 = 50% ...)
     */
    private void generateSparks(float chance) {
        // Making sure chance is between 0 and 1 [0..1]
        if (chance < 0f) chance = 0f;
        if (chance > 1f) chance = 1f;

        //int generationBorder = (int) Math.floor(pixels[0].length *0.15);
        int generationBorder = 0;

        for (int i = generationBorder; i < pixels[0].length -1 -generationBorder; i++) {
            if (Math.random() < chance) {
                pixels[pixels.length -1][i] = 255;
            }
        }
    }

    private void propagateHeat(double decayingfactor, int pixelsHeight, int pixelsWidth) {
        for (int j = pixelsHeight -2; j >= 0; j--) {
            for (int i = 0; i < pixelsWidth; i++) {

                double heatFromDown = 0;
                double heatFromRight = 0;
                double heatFromLeft = 0;

                if (j != pixelsHeight -1) {
                    // If it's not part of the lower row
                    heatFromDown = (pixels[j+1][i] * 0.5);
                }
                if (i != 0) {
                    // If it's not part of the left row
                    heatFromLeft = (pixels[j][i-1] * 0.25);
                }
                if (i != pixelsWidth -1) {
                    // If it's not part of the right now
                    heatFromRight = (pixels[j][i+1] * 0.25);
                }

                pixels[j][i] = (int) Math.round(
                        (heatFromDown + heatFromRight + heatFromLeft) * decayingfactor
                );

            }
        }
    }

    private void coolLowerRow(int pixelsHeight, int pixelsWidth) {
        for (int i = 0; i < pixelsWidth -1; i++) {
            pixels[pixelsHeight -1][i] = (int) Math.round(pixels[pixelsHeight -1][i] * 0.8);
        }
    }

}
