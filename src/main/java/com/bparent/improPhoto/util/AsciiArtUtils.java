package com.bparent.improPhoto.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AsciiArtUtils {

    private static final int FONT_SIZE = 20;
    private static final int X_MARGIN = 5;

    public static void printMessage(String message) {
        AsciiArtUtils.printMessage(message, "$");
    }

    public static void printMessage(String message, String character) {
//        String message = "127.0.0.1:8080";

        int width = message.length() * (AsciiArtUtils.FONT_SIZE / 2) + AsciiArtUtils.X_MARGIN;
        int height = AsciiArtUtils.FONT_SIZE + 10;

        //BufferedImage image = ImageIO.read(new File("/Users/mkyong/Desktop/logo.jpg"));
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, AsciiArtUtils.FONT_SIZE));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(message, AsciiArtUtils.X_MARGIN, AsciiArtUtils.FONT_SIZE + 5);

        //save this image
        //ImageIO.write(image, "png", new File("/users/mkyong/ascii-art.png"));

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {
                sb.append(image.getRGB(x, y) == -16777216 ? " " : character);
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            sb.append("\n");
        }
        System.out.println(sb);

    }

}
