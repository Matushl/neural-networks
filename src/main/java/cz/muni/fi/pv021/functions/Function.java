package cz.muni.fi.pv021.functions;

/**
 * Created by dhanak on 12/7/16.
 */
public interface Function {
    /**
     * Transfer function
     * @param value input
     * @return value of function
     */
    public double evaluate(double value);


    /**
     * Function derivative
     * @param value value in input
     * @return value of function derivative
     */
    public double evaluateDerivate(double value);
}
