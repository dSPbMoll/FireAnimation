package view;

import view.palette_customizing.ColorPaletteCustomizer;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final View view;
    private final JButton PLAY_BUTTON;
    private final JButton PAUSE_BUTTON;
    private final JButton RESTART_BUTTON;
    private final ColorPaletteCustomizer colorPaletteCustomizer;

    public ControlPanel(View view) {
        this.view = view;
        this.PLAY_BUTTON = new JButton("▶");
        this.PAUSE_BUTTON = new JButton("||");
        this.RESTART_BUTTON = new JButton("◯");
        this.colorPaletteCustomizer = new ColorPaletteCustomizer(this);

        setLayout(new GridBagLayout());
        setBackground(new Color(20, 20, 20));
        setPreferredSize(new Dimension(350, 0));
        setMinimumSize(new Dimension(350, 0));

        buildReproductionPanel();
        buildColoPaletteCustomizer();
    }

    // ------------------------------------ LAYOUT BUILDING ---------------------------------------------

    private void buildReproductionPanel() {
        JPanel reproductionPanel = new JPanel();
        reproductionPanel.setLayout(new GridLayout(1,3,2,2));
        reproductionPanel.add(PLAY_BUTTON);
        reproductionPanel.add(PAUSE_BUTTON);
        reproductionPanel.add(RESTART_BUTTON);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        add(reproductionPanel, gbc);
        addPlayButtonListener();
        addPauseButtonListener();
        addRestartButtonListener();
    }

    private void buildColoPaletteCustomizer() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;

        add(colorPaletteCustomizer, gbc);
    }

    // --------------------------------- LISTENERS ---------------------------------------

    private void addPlayButtonListener() {
        this.PLAY_BUTTON.addActionListener(e -> {

            Thread viewerThread = view.getViewerThread();
            Thread fireThread = view.getFireThread();
            if (viewerThread == null && fireThread == null) {
                System.out.println("start");
                view.startViewer();
                view.startFire();
            } else {
                System.out.println("Resume");
                view.resumeViewer();
                view.resumeFire();
            }
        });
    }

    private void addPauseButtonListener() {
        this.PAUSE_BUTTON.addActionListener(e -> {
            if (view.getViewerThread() != null) {
                view.pauseViewer();
                view.pauseFire();
            }
        });
    }

    private void addRestartButtonListener() {
        this.RESTART_BUTTON.addActionListener(e -> {
            view.restartViewer();
            colorPaletteCustomizer.restart();
            view.restartFire();
        });
    }

    // ---------------------------------- LINKING METHODS ----------------------------------

    public void setColorPalette(Color[] palette) {
        view.setColorPalette(palette);
    }
}
