package cz.muni.fi.pv021.functions;

/**
 * Sigmoidal transfer function.
 *
 * Created by dhanak on 12/7/16.
 */
public class SigmoidalTransfer implements Function {
    @Override
    public double evaluate(double value)
    {
        return 1 / (1 + Math.pow(Math.E, - value));
    }

    @Override
    public double evaluateDerivate(double value)
    {
        return (value - Math.pow(value, 2));
    }
}
