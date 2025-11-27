package me.croabeast.expr4j;

import lombok.experimental.UtilityClass;
import me.croabeast.expr4j.expression.Parameters;
import me.croabeast.expr4j.token.*;

/**
 * Convenience factory for commonly used operators and functions. The methods
 * in this utility class only wire metadata (labels, precedence, arity) to the
 * provided {@link UnaryOperation}, {@link BinaryOperation}, or {@link Operation}
 * implementations, leaving the mathematical logic to the caller.
 */
@UtilityClass
public class Operations {

    /**
     * Builds a unary prefix operator representing a no-op positive sign.
     *
     * @param <T> evaluation type handled by the operator
     * @return prefix operator wired to return its operand untouched
     */
    public <T> Operator<T> positive() {
        return new Operator<>("+", Operator.Type.PREFIX, Integer.MAX_VALUE, Parameters::result);
    }

    /**
     * Builds a unary prefix operator that negates its operand.
     *
     * @param operation negation implementation
     * @param <T>       evaluation type handled by the operator
     * @return configured operator instance
     */
    public <T> Operator<T> negative(UnaryOperation<T> operation) {
        return new Operator<>("-", Operator.Type.PREFIX, Integer.MAX_VALUE, operation);
    }

    /**
     * Creates an infix addition operator.
     */
    public <T> Operator<T> add(BinaryOperation<T> operation) {
        return new Operator<>("+", Operator.Type.INFIX, 1, operation);
    }

    /**
     * Creates an infix subtraction operator.
     */
    public <T> Operator<T> subtract(BinaryOperation<T> operation) {
        return new Operator<>("-", Operator.Type.INFIX, 1, operation);
    }

    /**
     * Creates an infix multiplication operator.
     */
    public <T> Operator<T> multiply(BinaryOperation<T> operation) {
        return new Operator<>("*", Operator.Type.INFIX, 2, operation);
    }

    /**
     * Creates an infix division operator.
     */
    public <T> Operator<T> divide(BinaryOperation<T> operation) {
        return new Operator<>("/", Operator.Type.INFIX, 2, operation);
    }

    /**
     * Creates an infix operator representing the remainder or percentage
     * operation.
     */
    public <T> Operator<T> percent(BinaryOperation<T> operation) {
        return new Operator<>("%", Operator.Type.INFIX, 2, operation);
    }

    /**
     * Creates a right-to-left exponentiation operator.
     */
    public <T> Operator<T> power(BinaryOperation<T> operation) {
        return new Operator<>("^", Operator.Type.INFIX_RTL, 3, operation);
    }

    /**
     * Creates a postfix factorial operator.
     */
    public <T> Operator<T> factorial(UnaryOperation<T> operation) {
        return new Operator<>("!", Operator.Type.POSTFIX, 5, operation);
    }

    /**
     * Creates a prefix absolute value operator.
     */
    public <T> Operator<T> absolute(UnaryOperation<T> operation) {
        return new Operator<>("abs", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix sine operator.
     */
    public <T> Operator<T> sine(UnaryOperation<T> operation) {
        return new Operator<>("sin", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix cosine operator.
     */
    public <T> Operator<T> cosine(UnaryOperation<T> operation) {
        return new Operator<>("cos", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix tangent operator.
     */
    public <T> Operator<T> tangent(UnaryOperation<T> operation) {
        return new Operator<>("tan", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix arcsine operator.
     */
    public <T> Operator<T> arcsine(UnaryOperation<T> operation) {
        return new Operator<>("asin", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix arccosine operator.
     */
    public <T> Operator<T> arccosine(UnaryOperation<T> operation) {
        return new Operator<>("acos", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix arctangent operator.
     */
    public <T> Operator<T> arctangent(UnaryOperation<T> operation) {
        return new Operator<>("atan", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix hyperbolic sine operator.
     */
    public <T> Operator<T> hSine(UnaryOperation<T> operation) {
        return new Operator<>("sinh", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix hyperbolic cosine operator.
     */
    public <T> Operator<T> hCosine(UnaryOperation<T> operation) {
        return new Operator<>("cosh", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix hyperbolic tangent operator.
     */
    public <T> Operator<T> hTangent(UnaryOperation<T> operation) {
        return new Operator<>("tanh", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix inverse hyperbolic sine operator.
     */
    public <T> Operator<T> arcHSine(UnaryOperation<T> operation) {
        return new Operator<>("asinh", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix inverse hyperbolic cosine operator.
     */
    public <T> Operator<T> arcHCosine(UnaryOperation<T> operation) {
        return new Operator<>("acosh", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix inverse hyperbolic tangent operator.
     */
    public <T> Operator<T> arcHTangent(UnaryOperation<T> operation) {
        return new Operator<>("atanh", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix rounding operator.
     */
    public <T> Operator<T> round(UnaryOperation<T> operation) {
        return new Operator<>("round", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix floor operator.
     */
    public <T> Operator<T> floor(UnaryOperation<T> operation) {
        return new Operator<>("floor", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix ceiling operator.
     */
    public <T> Operator<T> ceiling(UnaryOperation<T> operation) {
        return new Operator<>("ceil", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix natural logarithm operator.
     */
    public <T> Operator<T> naturalLogarithm(UnaryOperation<T> operation) {
        return new Operator<>("ln", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix base-10 logarithm operator.
     */
    public <T> Operator<T> base10Logarithm(UnaryOperation<T> operation) {
        return new Operator<>("log10", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix square-root operator.
     */
    public <T> Operator<T> squareRoot(UnaryOperation<T> operation) {
        return new Operator<>("sqrt", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Creates a prefix cube-root operator.
     */
    public <T> Operator<T> cubeRoot(UnaryOperation<T> operation) {
        return new Operator<>("cbrt", Operator.Type.PREFIX, 4, operation);
    }

    /**
     * Declares a one-argument function that converts radians to degrees.
     */
    public <T> Function<T> degrees(UnaryOperation<T> operation) {
        return new Function<>("deg", 1, operation);
    }

    /**
     * Declares a one-argument function that converts degrees to radians.
     */
    public <T> Function<T> radians(UnaryOperation<T> operation) {
        return new Function<>("rad", 1, operation);
    }

    /**
     * Declares a two-argument logarithm function with a configurable base.
     */
    public <T> Function<T> logarithm(BinaryOperation<T> operation) {
        return new Function<>("log", 2, operation);
    }

    /**
     * Declares a one-argument exponential function.
     */
    public <T> Function<T> exponential(UnaryOperation<T> operation) {
        return new Function<>("exp", 1, operation);
    }

    /**
     * Declares a variadic maximum function.
     */
    public <T> Function<T> maximum(Operation<T> operation) {
        return new Function<>("max", operation);
    }

    /**
     * Declares a variadic minimum function.
     */
    public <T> Function<T> minimum(Operation<T> operation) {
        return new Function<>("min", operation);
    }

    /**
     * Declares a variadic average function.
     */
    public <T> Function<T> average(Operation<T> operation) {
        return new Function<>("average", operation);
    }

    /**
     * Declares a variadic mean function (alias of average).
     */
    public <T> Function<T> mean(Operation<T> operation) {
        return new Function<>("mean", operation);
    }

    /**
     * Declares a random-number generator function without parameters.
     */
    public <T> Function<T> random(Operation<T> operation) {
        return new Function<>("rand", 0, operation);
    }
}
