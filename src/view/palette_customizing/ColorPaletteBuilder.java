package view.palette_customizing;

import _dto.ColorTarget;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ColorPaletteBuilder {
    private final Color[] palette;
    private ArrayList<ColorTarget> targetsList;

    public ColorPaletteBuilder(ArrayList<ColorTarget> targetsList) {
        palette = new Color[256];
        this.targetsList = targetsList;
        fillPalette();
    }

    // ---------------------------------- PALETTE GRADIENTS BUILDING LOGIC ----------------------------------

    public void fillPalette() {
        targetsList = sortTargetsList(targetsList);

        // Check that there is a maximum and a minimum
        if (targetsList.get(0).heatValue != 0) {
            targetsList.add(0, new ColorTarget(0, new Color(0, 0, 0, 0)));
        }
        if (targetsList.get(targetsList.size() - 1).heatValue != 255) {
            targetsList.add(new ColorTarget(255, new Color(255, 255, 255, 255)));
        }

        for (int i=0; i < targetsList.size() -1; i++) {
            fillPaletteRange(targetsList, i, i+1);
        }
    }

    private ArrayList<ColorTarget> sortTargetsList(ArrayList<ColorTarget> unorderedList) {
        ArrayList<ColorTarget> orderedList = new ArrayList<>(unorderedList);
        orderedList.sort(Comparator.comparingInt(t -> t.heatValue));
        return orderedList;
    }

    private void fillPaletteRange(ArrayList<ColorTarget> targetsList, int lowerTargetIndex, int higherTargetIndex) {
        ColorTarget lowerTarget = targetsList.get(lowerTargetIndex);
        ColorTarget higherTarget = targetsList.get(higherTargetIndex);

        int steps = higherTarget.heatValue - lowerTarget.heatValue;

        float stepR = (float)(higherTarget.color.getRed() - lowerTarget.color.getRed()) / steps;
        float stepG = (float)(higherTarget.color.getGreen() - lowerTarget.color.getGreen()) / steps;
        float stepB = (float)(higherTarget.color.getBlue() - lowerTarget.color.getBlue()) / steps;
        float stepA = (float)(higherTarget.color.getAlpha() - lowerTarget.color.getAlpha()) / steps;

        for (int i = 0; i < steps; i++) {
            int r = Math.round(lowerTarget.color.getRed() + stepR * i);
            int g = Math.round(lowerTarget.color.getGreen() + stepG * i);
            int b = Math.round(lowerTarget.color.getBlue() + stepB * i);
            int a = Math.round(lowerTarget.color.getAlpha() + stepA * i);

            palette[lowerTarget.heatValue + i] = new Color(r, g, b, a);
        }
    }

    // ----------------------------------- GETTERS & SETTERS -----------------------------------

    public Color[] getPalette() {
        return this.palette;
    }

}
