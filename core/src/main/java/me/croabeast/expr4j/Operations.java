package me.croabeast.expr4j;

import lombok.experimental.UtilityClass;
import me.croabeast.expr4j.expression.Parameters;
import me.croabeast.expr4j.token.*;

@UtilityClass
public class Operations {

    public <T> Operator<T> positive() {
        return new Operator<>("+", Operator.Type.PREFIX, Integer.MAX_VALUE, Parameters::result);
    }

    public <T> Operator<T> negative(UnaryOperation<T> operation) {
        return new Operator<>("-", Operator.Type.PREFIX, Integer.MAX_VALUE, operation);
    }

    public <T> Operator<T> add(BinaryOperation<T> operation) {
        return new Operator<>("+", Operator.Type.INFIX, 1, operation);
    }

    public <T> Operator<T> subtract(BinaryOperation<T> operation) {
        return new Operator<>("-", Operator.Type.INFIX, 1, operation);
    }

    public <T> Operator<T> multiply(BinaryOperation<T> operation) {
        return new Operator<>("*", Operator.Type.INFIX, 2, operation);
    }

    public <T> Operator<T> divide(BinaryOperation<T> operation) {
        return new Operator<>("/", Operator.Type.INFIX, 2, operation);
    }

    public <T> Operator<T> percent(BinaryOperation<T> operation) {
        return new Operator<>("%", Operator.Type.INFIX, 2, operation);
    }

    public <T> Operator<T> power(BinaryOperation<T> operation) {
        return new Operator<>("^", Operator.Type.INFIX_RTL, 3, operation);
    }

    public <T> Operator<T> factorial(UnaryOperation<T> operation) {
        return new Operator<>("!", Operator.Type.POSTFIX, 5, operation);
    }

    public <T> Operator<T> absolute(UnaryOperation<T> operation) {
        return new Operator<>("abs", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> sine(UnaryOperation<T> operation) {
        return new Operator<>("sin", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> cosine(UnaryOperation<T> operation) {
        return new Operator<>("cos", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> tangent(UnaryOperation<T> operation) {
        return new Operator<>("tan", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> arcsine(UnaryOperation<T> operation) {
        return new Operator<>("asin", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> arccosine(UnaryOperation<T> operation) {
        return new Operator<>("acos", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> arctangent(UnaryOperation<T> operation) {
        return new Operator<>("atan", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> hSine(UnaryOperation<T> operation) {
        return new Operator<>("sinh", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> hCosine(UnaryOperation<T> operation) {
        return new Operator<>("cosh", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> hTangent(UnaryOperation<T> operation) {
        return new Operator<>("tanh", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> arcHSine(UnaryOperation<T> operation) {
        return new Operator<>("asinh", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> arcHCosine(UnaryOperation<T> operation) {
        return new Operator<>("acosh", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> arcHTangent(UnaryOperation<T> operation) {
        return new Operator<>("atanh", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> round(UnaryOperation<T> operation) {
        return new Operator<>("round", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> floor(UnaryOperation<T> operation) {
        return new Operator<>("floor", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> ceiling(UnaryOperation<T> operation) {
        return new Operator<>("ceil", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> naturalLogarithm(UnaryOperation<T> operation) {
        return new Operator<>("ln", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> base10Logarithm(UnaryOperation<T> operation) {
        return new Operator<>("log10", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> squareRoot(UnaryOperation<T> operation) {
        return new Operator<>("sqrt", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Operator<T> cubeRoot(UnaryOperation<T> operation) {
        return new Operator<>("cbrt", Operator.Type.PREFIX, 4, operation);
    }

    public <T> Function<T> degrees(UnaryOperation<T> operation) {
        return new Function<>("deg", 1, operation);
    }

    public <T> Function<T> radians(UnaryOperation<T> operation) {
        return new Function<>("rad", 1, operation);
    }

    public <T> Function<T> logarithm(BinaryOperation<T> operation) {
        return new Function<>("log", 2, operation);
    }

    public <T> Function<T> exponential(UnaryOperation<T> operation) {
        return new Function<>("exp", 1, operation);
    }

    public <T> Function<T> maximum(Operation<T> operation) {
        return new Function<>("max", operation);
    }

    public <T> Function<T> minimum(Operation<T> operation) {
        return new Function<>("min", operation);
    }

    public <T> Function<T> average(Operation<T> operation) {
        return new Function<>("average", operation);
    }

    public <T> Function<T> mean(Operation<T> operation) {
        return new Function<>("mean", operation);
    }

    public <T> Function<T> random(Operation<T> operation) {
        return new Function<>("rand", 0, operation);
    }
}
