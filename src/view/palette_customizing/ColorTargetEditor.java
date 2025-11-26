package view.palette_customizing;

import _dto.ColorTarget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * This class represents a complex component which mainly contains 4 sliders, 5 text fields, and 1 button.
 * Additionaly, the 4 sliders that configurate the intensity of the color channels is linked to its text
 * field for updating it automatically and every single slider or textField ends up calling his parent for
 * updating the palette to a new one with the new values
 */
public class ColorTargetEditor extends JPanel {
    private final ColorPaletteCustomizer parent;
    private final JTextField targetTextField;
    private final JButton removeSelfButton;
    private final JPanel colorPreviewer;

    private final JSlider redSlider;
    private final JSlider greenSlider;
    private final JSlider blueSlider;
    private final JSlider alphaSlider;

    private final JTextField redTF;
    private final JTextField greenTF;
    private final JTextField blueTF;
    private final JTextField alphaTF;

    private boolean updating = false;

    /**
     *
     * @param parent ColorPaletteCustomizer that this object belong's to
     * @param colorTarget default values that this object will take
     */
    public ColorTargetEditor(ColorPaletteCustomizer parent, ColorTarget colorTarget) {
        this.parent = parent;

        this.targetTextField = new JTextField(3);
        this.removeSelfButton = new JButton("x");
        this.colorPreviewer = new JPanel();

        this.redSlider = new JSlider(0, 255);
        this.greenSlider = new JSlider(0, 255);
        this.blueSlider = new JSlider(0, 255);
        this.alphaSlider = new JSlider(0, 255);

        this.redTF = new JTextField(3);
        this.greenTF = new JTextField(3);
        this.blueTF = new JTextField(3);
        this.alphaTF = new JTextField(3);

        // Set values given
        int target = colorTarget.heatValue;
        Color color = colorTarget.color;

        this.targetTextField.setText(String.valueOf(target));

        this.redSlider.setValue(color.getRed());
        this.redTF.setText(String.valueOf(color.getRed()));

        this.greenSlider.setValue(color.getGreen());
        this.greenTF.setText(String.valueOf(color.getGreen()));

        this.blueSlider.setValue(color.getBlue());
        this.blueTF.setText(String.valueOf(color.getBlue()));

        this.alphaSlider.setValue(color.getAlpha());
        this.alphaTF.setText(String.valueOf(color.getAlpha()));

        buildContent();
        updateColorPreviewer();
        addListeners();
    }

    // ------------------------------------- LAYOUT BUILDING -------------------------------------

    /**
     * Main method for building all the component layout
     */
    private void buildContent() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));

        buildUpperPanel();
        buildColorPreviewer();
        buildSliders();
        buildSliderTextFields();
    }

    /**
     * Build the first row of the GridBagLayout
     */
    private void buildUpperPanel() {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Target: "), gbc);

        gbc.gridx = 1;
        add(targetTextField, gbc);

        gbc.gridx = 2;
        add(new JLabel("(0 - 255)"), gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(removeSelfButton, gbc);

    }

    /**
     * Build the color previewer occupying 4 rows (extended vertically)
     */
    private void buildColorPreviewer() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(27, 5, 27, 15);
        gbc.gridheight = 4;

        colorPreviewer.setMinimumSize(new Dimension(25, 25));
        colorPreviewer.setBorder(new LineBorder(Color.BLACK));
        colorPreviewer.setOpaque(true);
        add(colorPreviewer, gbc);

    }

    /**
     * Build the 4 sliders ocuppying 3 columns (2 to 4) and 4 rows (2 to 5)
     */
    private void buildSliders() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(redSlider, gbc);

        gbc.gridy = 2;
        add(greenSlider, gbc);

        gbc.gridy = 3;
        add(blueSlider, gbc);

        gbc.gridy = 4;
        add(alphaSlider, gbc);
    }

    /**
     * Build the TextFields that show the slider's selected value at the right of them
     */
    private void buildSliderTextFields() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 4;
        gbc.gridy = 1;
        add(redTF, gbc);

        gbc.gridy = 2;
        add(greenTF, gbc);

        gbc.gridy = 3;
        add(blueTF, gbc);

        gbc.gridy = 4;
        add(alphaTF, gbc);
    }

    /**
     * Update colorPreviewer's background with all the component's values (no alpha channer
     * due to lack of support by java swing)
     */
    private void updateColorPreviewer() {
        Color rgbaColor = buildColor();
        int r = rgbaColor.getRed();
        int g = rgbaColor.getGreen();
        int b = rgbaColor.getBlue();
        colorPreviewer.setBackground(new Color(r, g, b));
    }

    // ------------------------------------- LISTENERS -------------------------------------

    /**
     * General method for adding all the listeners to their respective objects
     */
    private void addListeners() {
        addTargetTFListener();
        addRemoveSelfButtonListener();

        addSliderListener(redSlider, redTF);
        addSliderListener(greenSlider, greenTF);
        addSliderListener(blueSlider, blueTF);
        addSliderListener(alphaSlider, alphaTF);

        addColorTFListener(redTF, redSlider);
        addColorTFListener(greenTF, greenSlider);
        addColorTFListener(blueTF, blueSlider);
        addColorTFListener(alphaTF, alphaSlider);
    }

    /**
     * Add a listener to the targetTextField for updating the palette when its content changes into a valid
     * numerical value
     */
    private void addTargetTFListener() {
        this.targetTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                contentChanged();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                contentChanged();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }

            private void contentChanged() {
                try {
                    if (targetTextField.getText().isEmpty()) return;
                    int newContent = Integer.parseInt(targetTextField.getText());
                    if (newContent >= 0 && newContent <= 255 ) {
                        parent.updatePalette();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }

            }
        });
    }

    /**
     * Add a listener to a textField for updating its respective slider value with its new one when the value
     * is valid
     * @param TF textField that has been changed
     * @param slider slider that will be updated with the new value of the TF
     */
    private void addColorTFListener(JTextField TF, JSlider slider) {
        TF.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                contentChanged(TF, slider);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                contentChanged(TF, slider);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }

            private void contentChanged(JTextField TF, JSlider slider) {
                if (updating) return;
                try {
                    if (TF.getText().isEmpty()) return;
                    int newContent = Integer.parseInt(TF.getText());
                    if (newContent >= 0 && newContent <= 255 ) {

                        updating = true;
                        slider.setValue(newContent);
                        updating = false;
                        parent.updatePalette();
                        updateColorPreviewer();

                    }
                } catch (Exception e) {
                    System.err.println(e);
                }

            }
        });
    }

    /**
     * Listener for updating the textField with the new slider value
     * @param slider slider that has been updated
     * @param TF textField that will take the new sliderValue
     */
    private void addSliderListener(JSlider slider, JTextField TF) {
        slider.addChangeListener(e -> {
            if (updating) return;

            int sliderValue = slider.getValue();
            updating = true;
            TF.setText(String.valueOf(sliderValue));
            updating = false;
            parent.updatePalette();
            updateColorPreviewer();

        });
    }

    /**
     * Listener for making this object's parent remove this object from it's ColorTargetEditorList
     */
    private void addRemoveSelfButtonListener() {
        removeSelfButton.addActionListener(e -> {
            parent.removeColorTargetEditor(this);
        });
    }

    // ----------------------------------- GENERATING THEES'S RESULTS -----------------------------------

    /**
     * Build a color with all the slider's values
     * @return Color built
     */
    public Color buildColor() {
        try {
            int r = redSlider.getValue();
            int g = greenSlider.getValue();
            int b = blueSlider.getValue();
            int a = alphaSlider.getValue();

            return new Color(r, g, b, a);

        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Build a ColorTarget with all the component's values
     * @return ColorTarget built
     */
    public ColorTarget buildColorTarget() {
        try {
            int target = Integer.parseInt(targetTextField.getText());
            return new ColorTarget(target, buildColor());

        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


}