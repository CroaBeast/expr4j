package me.croabeast.expr4j;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.experimental.UtilityClass;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.expression.Builder;
import me.croabeast.expr4j.expression.Codec;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BigDecimalBuilder extends Builder<BigDecimal> {

    public static final MathContext DEFAULT_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);

    private MathContext mathContext;

    public BigDecimalBuilder(MathContext mathContext) {
        super(new Codec<BigDecimal>() {
            @NotNull
            public BigDecimal toOperand(String string) {
                try {
                    return new BigDecimal(string);
                } catch (Exception e) {
                    return BigDecimal.ZERO;
                }
            }

            @NotNull
            public String toString(BigDecimal operand) {
                return operand.toString();
            }
        }, false);

        setMathContext(mathContext);
    }

    public BigDecimalBuilder(int precision, RoundingMode mode) {
        this(new MathContext(precision, mode));
    }

    public BigDecimalBuilder(int precision) {
        this(precision, RoundingMode.HALF_UP);
    }

    public BigDecimalBuilder() {
        this(DEFAULT_CONTEXT);
    }

    public BigDecimalBuilder setMathContext(MathContext context) {
        reset();
        this.mathContext = context;
        initialize();
        return this;
    }

    @Override
    protected void initialize() {
        getDictionary()
                .addOperator(Operations.positive()).addOperator(Operations.negative(BigDecimal::negate))
                .addOperator(Operations.add((a, b) -> a.add(b, mathContext)))
                .addOperator(Operations.subtract((a, b) -> a.subtract(b, mathContext)))
                .addOperator(Operations.multiply((a, b) -> a.multiply(b, mathContext)))
                .addOperator(Operations.divide((a, b) -> a.divide(b, mathContext)))
                .addOperator(Operations.percent((a, b) -> a.remainder(b, mathContext)))
                .addOperator(Operations.power((a, b) -> BigDecimalMath.pow(a, b, mathContext)))
                .addOperator(Operations.factorial(Utils::factorial))
                .addOperator(Operations.absolute(p -> p.abs(mathContext)))
                .addOperator(Operations.sine(p -> BigDecimalMath.sin(p, mathContext)))
                .addOperator(Operations.cosine(p -> BigDecimalMath.cos(p, mathContext)))
                .addOperator(Operations.tangent(p -> BigDecimalMath.tan(p, mathContext)))
                .addOperator(Operations.arcsine(p -> BigDecimalMath.asin(p, mathContext)))
                .addOperator(Operations.arccosine(p -> BigDecimalMath.acos(p, mathContext)))
                .addOperator(Operations.arctangent(p -> BigDecimalMath.atan(p, mathContext)))
                .addOperator(Operations.hSine(p -> BigDecimalMath.sinh(p, mathContext)))
                .addOperator(Operations.hCosine(p -> BigDecimalMath.cosh(p, mathContext)))
                .addOperator(Operations.hTangent(p -> BigDecimalMath.tanh(p, mathContext)))
                .addOperator(Operations.arcHSine(p -> BigDecimalMath.acosh(p, mathContext)))
                .addOperator(Operations.arcHCosine(p -> BigDecimalMath.acosh(p, mathContext)))
                .addOperator(Operations.arcHTangent(p -> BigDecimalMath.atanh(p, mathContext)))
                .addOperator(Operations.round(p -> p.setScale(0, RoundingMode.HALF_UP)))
                .addOperator(Operations.floor(p -> p.setScale(0, RoundingMode.FLOOR)))
                .addOperator(Operations.ceiling(p -> p.setScale(0, RoundingMode.CEILING)))
                .addOperator(Operations.naturalLogarithm(p -> BigDecimalMath.log(p, mathContext)))
                .addOperator(Operations.base10Logarithm(p -> BigDecimalMath.log10(p, mathContext)))
                .addOperator(Operations.squareRoot(p -> BigDecimalMath.sqrt(p, mathContext)))
                .addOperator(Operations.cubeRoot(p -> Utils.cbrt(p, mathContext)))
                .addFunction(Operations.degrees(p -> BigDecimalMath.toDegrees(p, mathContext)))
                .addFunction(Operations.radians(p -> BigDecimalMath.toRadians(p, mathContext)))
                .addFunction(Operations.logarithm((b, v) -> Utils.log(b, v, mathContext)))
                .addFunction(Operations.exponential(p -> BigDecimalMath.exp(p, mathContext)))
                .addFunction(Operations.maximum(p -> p.isEmpty() ? BigDecimal.ZERO : Collections.max(p.results())))
                .addFunction(Operations.minimum(p -> p.isEmpty() ? BigDecimal.ZERO : Collections.min(p.results())))
                .addFunction(Operations.mean(p -> Utils.average(p.results(), mathContext)))
                .addFunction(Operations.average(p -> Utils.average(p.results(), mathContext)))
                .addFunction(Operations.random(p -> new BigDecimal(Math.random())))
                .addConstant("pi", BigDecimalMath.pi(mathContext)).addConstant("e", BigDecimalMath.e(mathContext));
    }

    @UtilityClass
    class Utils {

        BigDecimal log(BigDecimal base, BigDecimal value, MathContext mathContext) {
            return BigDecimalMath.log(value, mathContext).divide(BigDecimalMath.log(base, mathContext), mathContext);
        }

        BigDecimal cbrt(BigDecimal x, MathContext mathContext) {
            return BigDecimalMath.pow(x,
                    BigDecimal.ONE.divide(new BigDecimal(3), mathContext),
                    mathContext);
        }

        BigDecimal average(List<BigDecimal> list, MathContext mathContext) {
            BigDecimal sum = list.stream()
                    .map(Objects::requireNonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(new BigDecimal(list.size()), mathContext);
        }

        BigDecimal factorial(BigDecimal x) {
            if (x == null || x.compareTo(BigDecimal.ZERO) < 0 || x.stripTrailingZeros().scale() > 0)
                throw new Expr4jException("Cannot calculate factorial of " + x);

            BigInteger factorial = BigInteger.ONE;

            BigInteger n = x.toBigInteger();
            while (n.compareTo(BigInteger.ONE) > 0) {
                factorial = factorial.multiply(n);
                n = n.subtract(BigInteger.ONE);
            }

            return new BigDecimal(factorial);
        }
    }
}
