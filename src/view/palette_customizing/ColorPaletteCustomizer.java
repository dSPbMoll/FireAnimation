package view.palette_customizing;

import _dto.ColorTarget;
import view.ControlPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ColorPaletteCustomizer extends JScrollPane {

    private final ControlPanel controlPanel;
    private final JPanel scrollPanelContent;
    private final JButton addColorRangeSliderButton;
    private final ArrayList<ColorTargetEditor> colorTargetEditorList;
    private ColorPaletteBuilder colorPaletteBuilder;

    public ColorPaletteCustomizer(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        this.colorTargetEditorList = new ArrayList<>();
        this.scrollPanelContent = new JPanel(new GridBagLayout());
        this.addColorRangeSliderButton = new JButton("+");

        getVerticalScrollBar().setUnitIncrement(20);

        scrollPanelContent.setBackground(new Color(20, 20, 20));
        scrollPanelContent.setBorder(new EmptyBorder(5, 5, 5, 5));

        buildTitleLabel("Color Palette Editor");
        buildAddColorTargetEditorButton();

        setViewportView(scrollPanelContent);

        buildDefaultColorTargets();
    }

    private void buildTitleLabel(String title) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,0,0,0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        scrollPanelContent.add(titleLabel, gbc);
    }

    private void buildDefaultColorTargets() {
        addColorTargetEditor(new ColorTargetEditor(this, new ColorTarget(0, new Color(0, 0, 0, 0))));
        addColorTargetEditor(new ColorTargetEditor(this, new ColorTarget(64, new Color(160, 0, 0, 100))));
        addColorTargetEditor(new ColorTargetEditor(this, new ColorTarget(128, new Color(196, 72, 0, 140))));
        addColorTargetEditor(new ColorTargetEditor(this, new ColorTarget(192, new Color(232, 143, 0, 180))));
        addColorTargetEditor(new ColorTargetEditor(this, new ColorTarget(255, new Color(255, 220, 71, 220))));

        updatePalette();
    }

    public void restart() {

        for (ColorTargetEditor cte : colorTargetEditorList) {
            scrollPanelContent.remove(cte);
        }
        colorTargetEditorList.clear();

        buildDefaultColorTargets();
    }

    private void buildAddColorTargetEditorButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,0,0,0);
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = getMaxGridyComponent() +1;

        scrollPanelContent.add(addColorRangeSliderButton, gbc);
        addAddColorTargetEditorButtonListener();
    }

    private void addAddColorTargetEditorButtonListener() {
        this.addColorRangeSliderButton.addActionListener(e -> {
            addColorTargetEditor(new ColorTargetEditor(this, new ColorTarget(0, new Color(0, 0, 0, 0))));
        });
    }

    private void addColorTargetEditor(ColorTargetEditor colorTargetEditor) {
        // Get the next free row
        int nextRow = getMaxGridyComponent() + 1;

        // Create a new slider and put it in that row
        GridBagConstraints gbcSlider = new GridBagConstraints();
        gbcSlider.fill = GridBagConstraints.HORIZONTAL;
        gbcSlider.insets = new Insets(5,0,0,0);
        gbcSlider.weightx = 1;
        gbcSlider.weighty = 0;
        gbcSlider.gridx = 0;
        gbcSlider.gridy = nextRow;
        scrollPanelContent.add(colorTargetEditor, gbcSlider);
        colorTargetEditorList.add(colorTargetEditor);

        // Move the button "+" to the next row
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.fill = GridBagConstraints.HORIZONTAL;
        gbcButton.insets = new Insets(5,0,0,0);
        gbcButton.weightx = 1;
        gbcButton.weighty = 0;
        gbcButton.gridx = 0;
        gbcButton.gridy = nextRow + 1;
        scrollPanelContent.add(addColorRangeSliderButton, gbcButton);

        // Update the panel
        scrollPanelContent.revalidate();
        scrollPanelContent.repaint();
    }

    public void removeColorTargetEditor(ColorTargetEditor colorTargetEditor) {
        if (colorTargetEditorList.size() > 2) {
            scrollPanelContent.remove(colorTargetEditor);
            colorTargetEditorList.remove(colorTargetEditor);
            controlPanel.setColorPalette(colorPaletteBuilder.getPalette());
            revalidate();
            repaint();
            updatePalette();
        }
    }

    public void updatePalette() {
        if (colorTargetEditorList.size() < 2) return;
        ArrayList<ColorTarget> targetsList = new ArrayList<>();

        for (ColorTargetEditor targetEditor : colorTargetEditorList) {
            targetsList.add(targetEditor.buildColorTarget());
        }

        this.colorPaletteBuilder = new ColorPaletteBuilder(targetsList);
        controlPanel.setColorPalette(colorPaletteBuilder.getPalette());
    }

    private int getMaxGridyComponent() {
        GridBagLayout layout = (GridBagLayout) scrollPanelContent.getLayout();
        Component[] comps = scrollPanelContent.getComponents();

        int maxGridY = -1;

        for (Component comp : comps) {
            GridBagConstraints gbc = layout.getConstraints(comp);

            if (gbc.gridy > maxGridY) {
                maxGridY = gbc.gridy;
            }
        }

        return maxGridY;
    }
}


