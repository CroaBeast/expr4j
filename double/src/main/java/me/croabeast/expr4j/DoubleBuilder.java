package me.croabeast.expr4j;

import lombok.experimental.UtilityClass;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.expression.Builder;
import me.croabeast.expr4j.expression.Codec;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DoubleBuilder extends Builder<Double> {

    public DoubleBuilder(boolean initialize) {
        super(new Codec<Double>() {
            @NotNull
            public Double toOperand(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (Exception e) {
                    return 0.0;
                }
            }

            @NotNull
            public String toString(Double operand) {
                if (operand == null) return "";

                int i = operand.intValue();
                return operand == i ? String.valueOf(i) : operand.toString();
            }
        }, initialize);
    }

    public DoubleBuilder() {
        this(true);
    }

    @Override
    protected void initialize() {
        getDictionary()
                .addOperator(Operations.positive()).addOperator(Operations.negative(p -> -p))
                .addOperator(Operations.add(Double::sum))
                .addOperator(Operations.subtract((a, b) -> a - b))
                .addOperator(Operations.multiply((a, b) -> a * b))
                .addOperator(Operations.divide((a, b) -> a / b))
                .addOperator(Operations.percent((a, b) -> a % b))
                .addOperator(Operations.power(Math::pow))
                .addOperator(Operations.factorial(Utils::factorial))
                .addOperator(Operations.absolute(Math::abs))
                .addOperator(Operations.sine(Math::sin))
                .addOperator(Operations.cosine(Math::cos))
                .addOperator(Operations.tangent(Math::tan))
                .addOperator(Operations.arcsine(Math::asin))
                .addOperator(Operations.arccosine(Math::acos))
                .addOperator(Operations.arctangent(Math::atan))
                .addOperator(Operations.hSine(Math::sinh))
                .addOperator(Operations.hCosine(Math::cosh))
                .addOperator(Operations.hTangent(Math::tanh))
                .addOperator(Operations.arcHSine(Utils::asinh))
                .addOperator(Operations.arcHCosine(Utils::acosh))
                .addOperator(Operations.arcHTangent(Utils::atanh))
                .addOperator(Operations.round(p -> (double) Math.round(p)))
                .addOperator(Operations.floor(Math::floor))
                .addOperator(Operations.ceiling(Math::ceil))
                .addOperator(Operations.naturalLogarithm(Math::log))
                .addOperator(Operations.base10Logarithm(Math::log10))
                .addOperator(Operations.squareRoot(Math::sqrt))
                .addOperator(Operations.cubeRoot(Math::cbrt))
                .addFunction(Operations.degrees(Math::toDegrees))
                .addFunction(Operations.radians(Math::toRadians))
                .addFunction(Operations.logarithm((b, v) -> Math.log(v) / Math.log(b)))
                .addFunction(Operations.exponential(Math::exp))
                .addFunction(Operations.maximum(p -> p.isEmpty() ? 0.0 : Collections.max(p.results())))
                .addFunction(Operations.minimum(p -> p.isEmpty() ? 0.0 : Collections.min(p.results())))
                .addFunction(Operations.mean(p -> p.isEmpty() ? 0.0 : Utils.average(p.results())))
                .addFunction(Operations.average(p -> p.isEmpty() ? 0.0 : Utils.average(p.results())))
                .addFunction(Operations.random(p -> Math.random()))
                .addConstant("pi", Math.PI).addConstant("e", Math.E);
    }

    @UtilityClass
    class Utils {

        double asinh(double x) {
            return Math.log(x + Math.sqrt(x * x + 1));
        }

        double acosh(double x) {
            return Math.log(x + Math.sqrt(x * x - 1));
        }

        double atanh(double x) {
            return 0.5 * Math.log((1 + x) / (1 - x));
        }

        Double average(List<Double> list) {
            return list.stream().mapToDouble(d -> d).average().orElse(0.0);
        }

        double factorial(double x) {
            if (x < 0 || x != (int) x)
                throw new Expr4jException("Cannot calculate factorial of " + x);

            double factorial = 1.0;
            for (int i = 2; i <= x; i++) factorial *= i;

            return factorial;
        }
    }
}
