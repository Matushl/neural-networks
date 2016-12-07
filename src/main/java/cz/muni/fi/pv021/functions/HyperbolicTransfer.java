package cz.muni.fi.pv021.functions;

/**
 * Hyperbolic transfer function.
 *
 * Created by dhanak on 12/7/16.
 */
public class HyperbolicTransfer implements Function {

    @Override
    public double evaluate(double value)
    {
        return Math.tanh(value);
    }

    @Override
    public double evaluateDerivate(double value)
    {
        return 1 - Math.pow(value, 2);
    }
}
