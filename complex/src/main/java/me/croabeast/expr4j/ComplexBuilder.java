package me.croabeast.expr4j;

import lombok.experimental.UtilityClass;
import me.croabeast.expr4j.expression.Builder;
import me.croabeast.expr4j.expression.Codec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ComplexBuilder extends Builder<Complex> {

    public ComplexBuilder(boolean initialize) {
        super(new Codec<Complex>() {
            @NotNull
            public Complex toOperand(String string) {
                try {
                    return new Complex(string);
                } catch (Exception e) {
                    return new Complex(0.0, 0.0);
                }
            }

            @NotNull
            public String toString(Complex operand) {
                return operand.toString();
            }
        }, initialize);
    }

    public ComplexBuilder() {
        this(true);
    }

    @Override
    protected void initialize() {
        getDictionary()
                .addOperator(Operations.positive()).addOperator(Operations.negative(Complex::negate))
                .addOperator(Operations.add(Complex::add))
                .addOperator(Operations.subtract(Complex::subtract))
                .addOperator(Operations.multiply(Complex::multiply))
                .addOperator(Operations.divide(Complex::divide))
                .addOperator(Operations.power(Complex::pow))
                .addOperator(Operations.absolute(p -> new Complex(p.abs(), 0.0)))
                .addOperator(Operations.sine(Complex::sin))
                .addOperator(Operations.cosine(Complex::cos))
                .addOperator(Operations.tangent(Complex::tan))
                .addOperator(Operations.arcsine(Complex::asin))
                .addOperator(Operations.arccosine(Complex::acos))
                .addOperator(Operations.arctangent(Complex::atan))
                .addOperator(Operations.hSine(Complex::sinh))
                .addOperator(Operations.hCosine(Complex::cosh))
                .addOperator(Operations.hTangent(Complex::tanh))
                .addOperator(Operations.arcHSine(Complex::asinh))
                .addOperator(Operations.arcHCosine(Complex::acosh))
                .addOperator(Operations.arcHTangent(Complex::atanh))
                .addOperator(Operations.naturalLogarithm(Complex::log))
                .addOperator(Operations.base10Logarithm(Complex::log10))
                .addOperator(Operations.squareRoot(Complex::sqrt))
                .addOperator(Operations.cubeRoot(Utils::cbrt))
                .addFunction(Operations.degrees(Utils::toDegrees))
                .addFunction(Operations.radians(Utils::toRadians))
                .addFunction(Operations.logarithm(Utils::log))
                .addFunction(Operations.exponential(Complex::exp))
                .addFunction(Operations.maximum(p -> p.isEmpty() ? Complex.ZERO : Utils.max(p.results())))
                .addFunction(Operations.minimum(p -> p.isEmpty() ? Complex.ZERO : Utils.min(p.results())))
                .addFunction(Operations.average(p -> p.isEmpty() ? Complex.ZERO : Utils.average(p.results())))
                .addFunction(Operations.mean(p -> p.isEmpty() ? Complex.ZERO : Utils.average(p.results())))
                .addFunction(Operations.random(p -> Complex.random()))
                .addConstant("pi", Complex.PI).addConstant("e", Complex.E).addConstant("i", Complex.I);
    }

    @UtilityClass
    class Utils {

        Complex toDegrees(Complex complex) {
            return new Complex(Math.toDegrees(complex.getReal()), Math.toDegrees(complex.getImaginary()));
        }

        Complex toRadians(Complex complex) {
            return new Complex(Math.toRadians(complex.getReal()), Math.toRadians(complex.getImaginary()));
        }

        Complex log(Complex base, Complex value) {
            return value.log().divide(base.log());
        }

        Complex cbrt(Complex complex) {
            return complex.pow(1.0 / 3.0);
        }

        Complex max(List<Complex> complexList) {
            Complex max = null;

            for (Complex complex : complexList)
                if (max == null || complex.abs() > max.abs())
                    max = complex;

            return max;
        }

        Complex min(List<Complex> complexList) {
            Complex min = null;

            for (Complex complex : complexList)
                if (min == null || complex.abs() < min.abs())
                    min = complex;

            return min;
        }

        Complex average(List<Complex> list) {
            Complex sum = list.stream().map(Objects::requireNonNull).reduce(Complex.ZERO, Complex::add);
            return sum.divide(new Complex(list.size(), 0));
        }
    }
}
