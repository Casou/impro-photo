package com.bparent.improPhoto.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AsciiArtUtils {

    private static final int FONT_SIZE = 13;
    private static final int X_MARGIN = 5;

    public static void printMessage(String message) {
        AsciiArtUtils.printMessage(message, "$");
    }

    public static void printMessage(String message, String artChar) {
        final int width = (message.length() * AsciiArtUtils.FONT_SIZE) + AsciiArtUtils.X_MARGIN;
        final int height = AsciiArtUtils.FONT_SIZE + 10;
        Font font = new Font("SansSerif", Font.PLAIN, AsciiArtUtils.FONT_SIZE);

        Settings settings = new Settings(font, width, height);

        BufferedImage image = getImageIntegerMode(settings.width, settings.height);

        Graphics2D graphics2D = getGraphics2D(image.getGraphics(), settings);
        graphics2D.drawString(message, 6, ((int) (settings.height * 0.67)));

        for (int y = 0; y < settings.height; y++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int x = 0; x < settings.width; x++) {
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : artChar);
            }

            if (stringBuilder.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(stringBuilder);
        }

    }

    private static BufferedImage getImageIntegerMode(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private static Graphics2D getGraphics2D(Graphics graphics, Settings settings) {
        graphics.setFont(settings.font);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        return graphics2D;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class Settings {
        public Font font;
        public int width;
        public int height;
    }

}
