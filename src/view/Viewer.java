package view;

import _dto.ImageDto;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Viewer extends Canvas implements Runnable {
    private final View view;
    private Thread thread;
    private boolean threadRunning;
    private boolean playing;
    private final BufferedImage backgroundImage;
    private Color[] colorPalette;

    public Viewer(View view, ImageDto backgroundImage) {
        this.view = view;
        this.backgroundImage = resize(toBufferedImage(backgroundImage), 512, 512);
    }

    // ------------------------------------ GETTERS & SETTERS ------------------------------------

    public Thread getThread() {
        return this.thread;
    }

    public void setColorPalette(Color[] palette) {
        this.colorPalette = palette;
    }

    // ------------------------------------ LAYOUT BUILDING ------------------------------------

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose(); // libera recursos
        return resized; // =======================================================>>
    }

    /**
     * Converting ImageDto into BufferedImage for painting them
     * @param dto ImageDto
     * @return BufferedImage
     */
    private static BufferedImage toBufferedImage(ImageDto dto) {
        Image img = dto.image;
        if (img instanceof BufferedImage) {
            return (BufferedImage) img; //======================================================>
        }

        ImageIcon icon = new ImageIcon(img);
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("Image is not loaded or has invalid size");
        }

        BufferedImage bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimg.createGraphics();
        try {
            g.setComposite(AlphaComposite.Src);
            g.drawImage(img, 0, 0, null);
        } finally {
            g.dispose();
        }
        return bimg; // =======================================================>>
    }

    // ------------------------------------ FIRE PAINTING ------------------------------------

    @Override
    public void run() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            System.out.println("No BufferStrategy found");
            return;
        }

        while (threadRunning) {
            if (playing) {
                paintFrame(bs, new Dimension(196, 230));
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * The logic of painting an entire frame
     * @param bs BufferStrategy of the canvas
     * @param drawStartPosition top-left location of the start of the fire
     */
    private void paintFrame(BufferStrategy bs, Dimension drawStartPosition) {
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        try {
            g.setComposite(AlphaComposite.SrcOver);
            g.drawImage(backgroundImage, 0, 0, null);

            paintCurrentFireFrame(g, drawStartPosition);
            printPalette(g, new Dimension(0, 550));
        } finally {
            g.dispose();
        }
        bs.show();
    }

    /**
     * The logic for painting a fire frame
     * @param g Graphics2D of the canvas
     * @param drawStartPosition Position where the fire will start to be rendered
     */
    private void paintCurrentFireFrame(Graphics2D g, Dimension drawStartPosition) {
        int[][] temperaturesArray = this.view.getTemperaturesArray();
        int startX = (int) drawStartPosition.getWidth();
        int startY = (int) drawStartPosition.getHeight();

        for (int i=0; i < temperaturesArray.length; i++) {
            for (int j=0; j < temperaturesArray[i].length; j++) {

                Color paintingcolor = colorPalette[temperaturesArray[i][j]];
                g.setColor(paintingcolor);
                g.fillRect(startX + j, startY + i, 1, 1);

            }
        }
    }

    // ------------------------------------ THREAD MANAGING ------------------------------------

    /**
     * Activation of the thread
     */
    public void start() {
        this.createBufferStrategy(2);
        threadRunning = true;
        playing = true;
        thread = new Thread(this);
        thread.setName("Viewer thread: A bucle that paints background image and fire");
        thread.start();
    }

    public void pause(){
        playing = false;
    }

    public void resume() {
        playing = true;
    }

    public void restart() {
        // Pedimos que pare
        threadRunning = false;
        playing = false;

        try {
            if (thread != null && thread.isAlive()) {
                thread.join();   // ← Esperar a que pare completamente
            }
        } catch (InterruptedException ignored) {}

        // Crear un nuevo thread limpio
        thread = new Thread(this);
        thread.setName("Viewer thread: ...");

        // Reiniciar estado
        threadRunning = true;
        playing = true;

        // Crear nuevo buffer strategy antes de iniciar thread
        this.createBufferStrategy(2);

        thread.start();
    }

    // --------------------------------------- DEBUG ---------------------------------------

    /**
     * Debugging method - Draws the colorPalette
     * @param g Graphics2D object for drawing
     * @param drawStartPosition Position where the palette will be drawed
     */
    private void printPalette(Graphics2D g, Dimension drawStartPosition) {
        for (int i=0; i < colorPalette.length; i++) {

            Color paintingcolor = colorPalette[i];
            g.setColor(paintingcolor);
            g.fillRect((int) drawStartPosition.getWidth() + i*2, (int) drawStartPosition.getHeight(), 2, 12);

        }
    }

}
