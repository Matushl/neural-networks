package cz.muni.fi.pv021.functions;

/**
 *
 * Heavyside transfer function.
 *
 * Created by dhanak on 12/7/16.
 */
public class HeavysideTransfer implements Function {

    @Override
    public double evaluate(double value)
    {
        if(value >= 0.0)
            return 1.0;
        else
            return 0.0;
    }

    @Override
    public double evaluateDerivate(double value)
    {
        return 1.0;
    }
}
