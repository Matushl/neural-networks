package cz.muni.fi.pv021.samples;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
/**
 * Created by Pavel on 09-Dec-16.
 */
public class PersonalSamples {
    String path;
    public double[][] parsedImage = new double[10][];
    public PersonalSamples(String path){
        this.path = path;
        String[] fileNames = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        for (int i = 0; i < 10; i++){
            this.parsedImage[i] = getImg(path + fileNames[i] + ".png");
        }
    }

    public double[]  getImg(String fileName){
        BufferedImage img = null;
        double[] matrixImage = new double[28*28];
        try {
            img = ImageIO.read(new File(fileName));
            for(int i = 0; i < 28; i++){
                for (int j = 0; j < 28; j++){
                    int rgb = img.getRGB(j, i);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);
                    int gray = Math.abs((r + g + b)/3-255);
                    matrixImage[(i*28) + j] = ((double)gray)/1000;
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return matrixImage;
    }
}
