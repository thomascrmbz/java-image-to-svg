package com.trikthom.image_to_SVG;

import com.trikthom.image_to_SVG.annotations.Required;
import com.trikthom.image_to_SVG.classes.SVG;
import com.trikthom.image_to_SVG.classes.Settings;
import com.trikthom.image_to_SVG.exceptions.MissingRequiredPropertiesException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageToSVG {

    private final @Required String filename;
    private final Settings settings;
    private BufferedImage image;

    public static void main(String[] args) {

        String filename = args.length >= 1 ? args[0] : null;

        ImageToSVG imageToSVG = new ImageToSVG(filename);

    }

    public ImageToSVG(String filename) {
        this(filename, null);
    }

    public ImageToSVG(String filename, Settings settings) {
        this.filename = filename;
        this.settings = settings;

        checkAttributes();

        System.out.println("Loaded Image to SVG");
        System.out.println(filename);

        File file = new File(filename);

        try {
            image = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Color color = new Color(image.getRGB(0,0));

        System.out.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());

        createSVG();
    }

    private void createSVG() {
        try {
            FileWriter myWriter = new FileWriter(filename.replace("png", "svg"));
            new SVG(myWriter);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void checkAttributes() {

        List<String> errors = new ArrayList<>();

        for (java.lang.reflect.Field field : this.getClass().getDeclaredFields()) {
            if (field.getDeclaredAnnotation(Required.class) != null) {
                try {
                    field.setAccessible(true);
                    if (field.get(this) == null || "".equals(field.get(this))) errors.add(field.getName());
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (errors.size() > 0) throw new MissingRequiredPropertiesException(errors);
    }

}