package view;

import controller.Controller;
import _dto.ImageDto;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class View extends JFrame {
    private final Controller controller;
    private final Viewer viewer;
    private final ControlPanel controlPanel;

    public View (Controller controller) {
        this.controller = controller;
        this.viewer = new Viewer(this, getBackgroundImage());
        this.controlPanel = new ControlPanel(this);

        prepareWindow();
    }

    // --------------------------------------- LAYOUT BUILDING ---------------------------------------

    /**
     * Method for preparing all the layout of View
     */
    private void prepareWindow() {
        setTitle("Fire animation");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        Container content = this.getContentPane();

        buildControlPanel(content);
        buildViewer(content);

        setVisible(true);
    }

    /**
     * @return The background image (house with a campfire)
     */
    private ImageDto getBackgroundImage() {
        String uri = "src/_images/";
        String fileName = "chimney.png";
        ImageDto imageDto;

        try {
            imageDto = new ImageDto(uri, ImageIO.read(new File(uri + fileName)));

        } catch (IOException e) {
            System.err.println("Error loading background image: " + e);
            imageDto = null;
        }

        return imageDto; //-------------------------------------------------------------------->
    }

    /**
     * Layout method for adding a ControlPanel to the View
     * @param content Container where the controlPanel will be added
     */
    private void buildControlPanel(Container content) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        content.add(this.controlPanel, gbc);
    }

    /**
     * Layout method for adding a Viewer to the View
     * @param content Container where the viewer will be added
     */
    private void buildViewer(Container content) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        content.add(this.viewer, gbc);
    }

    // --------------------------------------- LINKING METHODS ---------------------------------------
    // ------------ VIEWER RELATED

    /**
     * Activation of the viewer's thread
     */
    public void startRendering() {
        this.viewer.start();
    }

    public int[][] getTemperaturesArray() {
        return controller.getTemperaturesArray();
    }

    public Thread getViewerThread() {
        return viewer.getThread();
    }

    public void startViewer() {
        this.viewer.start();
    }

    public void pauseViewer() {
        this.viewer.pause();
    }

    public void resumeViewer() {
        this.viewer.resume();
    }

    public void restartViewer() {
        this.viewer.restart();
    }

    // ------------ CONTROL PANEL RELATED

    public void setColorPalette(Color[] palette) {
        viewer.setColorPalette(palette);
    }

    // ------------ MODEL RELATED

    public Thread getFireThread() {
        return controller.getFireThread();
    }

    public void startFire() {
        controller.startFire();
    }

    public void resumeFire() {
        controller.resumeFire();
    }

    public void pauseFire() {
        controller.pauseFire();
    }

    public void restartFire() {
        controller.restartFire();
    }

}
