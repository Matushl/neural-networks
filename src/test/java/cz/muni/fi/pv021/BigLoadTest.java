package cz.muni.fi.pv021;

import cz.muni.fi.pv021.functions.HyperbolicTransfer;
import cz.muni.fi.pv021.network.MultiLayerPerceptron;

/**
 * Tests if the network is able to consume bigger inputs.
 *
 * Created by dhanak on 12/7/16.
 */
public class BigLoadTest {

    public static void main(String[] args)
    {
        int[] layers = new int[]{ 16*16, 16*16/2, 40 };

        MultiLayerPerceptron net = new MultiLayerPerceptron(layers, 0.6, new HyperbolicTransfer());

		/* Learning */
        for(int i = 0; i < 100000; i++)
        {
            double[] inputs = new double[16*16];
            double[] output = new double[40];
            double error;

            for(int j = 0; j < inputs.length; j++)
                inputs[j] = 0;

            error = net.backPropagate(inputs, output);
            System.out.println("Error at step " + i + " is " + error);
        }

        System.out.println("Learning completed!");



    }
}
