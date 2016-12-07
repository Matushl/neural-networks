package cz.muni.fi.pv021.reader.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Processor for images.
 *
 * Created by dhanak on 12/7/16.
 */
public class ImageProcessor {

    public static double[] loadImage(String path, int sizex, int sizey, boolean color) {
        File imgLoc = new File(path);

        BufferedImage formattedImage;
        BufferedImage loadedImage;

        if (color) {
            formattedImage = new BufferedImage(
                    sizex,
                    sizey,
                    BufferedImage.TYPE_INT_ARGB);
        } else {
            formattedImage = new BufferedImage(
                    sizex,
                    sizey,
                    BufferedImage.TYPE_BYTE_GRAY);
        }

        try {
            loadedImage = ImageIO.read(imgLoc);
        } catch (IOException ex) {
            System.out.println(path + " was not loaded");
            return null;
        }

        Graphics2D g = formattedImage.createGraphics();
        g.drawImage(loadedImage, 0, 0, null);
        g.dispose();

        double[] data = new double[sizex*sizey];

        for(int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                int[] d = new int[3];
                formattedImage.getRaster().getPixel(i, j, d);
                data[i * sizex + j] = ((double) d[0]) / 255.0;
            }
        }
        return data;
    }

    public static boolean saveImage(String path, double[] data, int sizex, int sizey, boolean color) {
        BufferedImage bufferedImage;

        if (color) {
            bufferedImage = new BufferedImage(
                    sizex,
                    sizey,
                    BufferedImage.TYPE_INT_ARGB);
        } else {
            bufferedImage = new BufferedImage(
                    sizex,
                    sizey,
                    BufferedImage.TYPE_BYTE_GRAY);
        }

        for(int i = 0; i < sizex; i++)
            for(int j = 0; j < sizey; j++) {
                int[] d = new int[3];
                d[0] = (int) (data[i*sizex + j] * 255);
                d[1] = 0;
                d[2] = 0;
                bufferedImage.getRaster().setPixel(i, j, d);
            }

        try {
            File outputfile = new File(path);
            ImageIO.write(bufferedImage, "png", outputfile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
