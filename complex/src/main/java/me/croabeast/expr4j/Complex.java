package me.croabeast.expr4j;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Lightweight wrapper around {@link org.apache.commons.numbers.complex.Complex}
 * that exposes a friendly API tailored for expression evaluation. Instances are
 * immutable, making them safe to reuse across threads or share between
 * expressions without defensive copies. The class mirrors common mathematical
 * operations while favoring descriptive names and rich documentation so that
 * plugin authors can understand the semantics without consulting the upstream
 * Apache Commons implementation.
 */
@RequiredArgsConstructor
public final class Complex extends Number implements Comparable<Complex> {

    private static final long serialVersionUID = 20251127L;

    /**
     * Neutral element for addition that maps to {@code 0 + 0i}. Keeping a
     * shared instance avoids allocating new objects for comparisons or
     * zero-initialized states when building expressions.
     */
    public static final Complex ZERO = new Complex(0.0, 0.0);
    /**
     * Multiplicative identity {@code 1 + 0i}. Useful when generating constants
     * for scaling and when users need a well-documented starting value for
     * iterative methods.
     */
    public static final Complex ONE = new Complex(1.0, 0.0);
    /**
     * Imaginary unit {@code 0 + 1i}. Central for trigonometric conversions and
     * for building Euler-style expressions.
     */
    public static final Complex I = new Complex(0.0, 1.0);
    /**
     * Constant whose real part matches {@link Math#PI}. This mirrors the
     * notation used in many scientific calculators and keeps the expression API
     * self contained.
     */
    public static final Complex PI = new Complex(Math.PI, 0.0);
    /**
     * Constant whose real part matches {@link Math#E}. Having {@code e}
     * available as a {@code Complex} simplifies constructing logarithmic or
     * exponential expressions without manual wrapping.
     */
    public static final Complex E = new Complex(Math.E, 0.0);

    private final org.apache.commons.numbers.complex.Complex complex;

    /**
     * Builds a complex number from real and imaginary components while keeping
     * the behavior consistent with Apache Commons math parsing and formatting.
     */
    public Complex(double real, double imaginary) {
        this(org.apache.commons.numbers.complex.Complex.ofCartesian(real, imaginary));
    }

    /**
     * Parses a textual complex number using Apache Commons formatting rules.
     * The accepted syntax includes pairs such as {@code 3+4i} or {@code -2i}.
     */
    public Complex(String string) {
        this(org.apache.commons.numbers.complex.Complex.parse(string));
    }

    /**
     * Exposes the underlying Apache Commons representation so advanced callers
     * can rely on the full feature set of that library (for instance, advanced
     * roots or argument calculations) while still keeping the wrapper for
     * interoperability with the expression engine.
     *
     * @return wrapped {@link org.apache.commons.numbers.complex.Complex}
     */
    @NotNull
    public org.apache.commons.numbers.complex.Complex asApache() {
        return complex;
    }

    /**
     * Returns the real component of the number. No rounding is performed, so
     * the caller receives the exact double precision value stored in the
     * internal representation.
     */
    public double getReal() {
        return complex.getReal();
    }

    /**
     * Returns the imaginary component of the number. The value is always
     * expressed in cartesian form rather than polar coordinates.
     */
    public double getImaginary() {
        return complex.getImaginary();
    }

    /**
     * Produces the additive inverse of this complex number, effectively
     * multiplying both components by {@code -1}. Useful for symmetric
     * operations and for toggling signs in manually constructed expressions.
     */
    @NotNull
    public Complex negate() {
        return new Complex(complex.negate());
    }

    /**
     * Adds another complex number and returns a new immutable instance rather
     * than mutating the current one.
     *
     * @param other addend
     * @return sum of this and {@code other}
     */
    @NotNull
    public Complex add(@NotNull Complex other) {
        return new Complex(complex.add(other.complex));
    }

    /**
     * Subtracts another complex number while keeping the original operands
     * intact.
     *
     * @param other subtrahend
     * @return difference between this and {@code other}
     */
    @NotNull
    public Complex subtract(@NotNull Complex other) {
        return new Complex(complex.subtract(other.complex));
    }

    /**
     * Multiplies by another complex number using the standard cartesian
     * multiplication rule.
     *
     * @param other multiplicand
     * @return product of the two numbers
     */
    @NotNull
    public Complex multiply(@NotNull Complex other) {
        return new Complex(complex.multiply(other.complex));
    }

    /**
     * Divides by another complex number. Division by zero mirrors Apache
     * Commons' behavior, propagating infinities or {@code NaN} as appropriate.
     *
     * @param other divisor
     * @return quotient of the division
     */
    @NotNull
    public Complex divide(@NotNull Complex other) {
        return new Complex(complex.divide(other.complex));
    }

    /**
     * Raises this number to the given complex power. The result follows the
     * same branch selection strategy as Apache Commons, ensuring compatibility
     * with their trigonometric utilities.
     *
     * @param other exponent to apply
     * @return result of the exponentiation
     */
    @NotNull
    public Complex pow(@NotNull Complex other) {
        return new Complex(complex.pow(other.complex));
    }

    /**
     * Raises this number to a real-valued exponent. This is a convenience for
     * callers that primarily work with real numbers but need occasional complex
     * exponentiation.
     *
     * @param other exponent to apply
     * @return result of the exponentiation
     */
    @NotNull
    public Complex pow(double other) {
        return new Complex(complex.pow(other));
    }

    /**
     * Returns the magnitude (modulus) of the complex number calculated as
     * {@code sqrt(real^2 + imaginary^2)}.
     */
    public double abs() {
        return complex.abs();
    }

    /**
     * Computes the complex sine using {@code sin(a+bi) = sin(a)cosh(b) +
     * i cos(a)sinh(b)}.
     *
     * @return sine of this complex number
     */
    @NotNull
    public Complex sin() {
        return new Complex(complex.sin());
    }

    /**
     * Computes the complex cosine while preserving branch consistency with the
     * underlying Apache Commons implementation.
     *
     * @return cosine of this complex number
     */
    @NotNull
    public Complex cos() {
        return new Complex(complex.cos());
    }

    /**
     * Computes the complex tangent.
     *
     * @return tangent of this complex number
     */
    @NotNull
    public Complex tan() {
        return new Complex(complex.tan());
    }

    /**
     * Computes the inverse sine, returning the principal value in the complex
     * plane.
     *
     * @return arcsine of this complex number
     */
    @NotNull
    public Complex asin() {
        return new Complex(complex.asin());
    }

    /**
     * Computes the inverse cosine and returns the principal value.
     *
     * @return arccosine of this complex number
     */
    @NotNull
    public Complex acos() {
        return new Complex(complex.acos());
    }

    /**
     * Computes the inverse tangent using the same conventions as Apache
     * Commons.
     *
     * @return arctangent of this complex number
     */
    @NotNull
    public Complex atan() {
        return new Complex(complex.atan());
    }

    /**
     * Computes the hyperbolic sine in the complex plane.
     *
     * @return hyperbolic sine of this complex number
     */
    @NotNull
    public Complex sinh() {
        return new Complex(complex.sinh());
    }

    /**
     * Computes the hyperbolic cosine in the complex plane.
     *
     * @return hyperbolic cosine of this complex number
     */
    @NotNull
    public Complex cosh() {
        return new Complex(complex.cosh());
    }

    /**
     * Computes the hyperbolic tangent.
     *
     * @return hyperbolic tangent of this complex number
     */
    @NotNull
    public Complex tanh() {
        return new Complex(complex.tanh());
    }

    /**
     * Computes the inverse hyperbolic sine.
     *
     * @return inverse hyperbolic sine of this complex number
     */
    @NotNull
    public Complex asinh() {
        return new Complex(complex.asinh());
    }

    /**
     * Computes the inverse hyperbolic cosine using the principal branch.
     *
     * @return inverse hyperbolic cosine of this complex number
     */
    @NotNull
    public Complex acosh() {
        return new Complex(complex.acosh());
    }

    /**
     * Computes the inverse hyperbolic tangent.
     *
     * @return inverse hyperbolic tangent of this complex number
     */
    @NotNull
    public Complex atanh() {
        return new Complex(complex.atanh());
    }

    /**
     * Calculates the natural logarithm and returns the principal value.
     *
     * @return natural logarithm of this complex number
     */
    @NotNull
    public Complex log() {
        return new Complex(complex.log());
    }

    /**
     * Calculates the base-10 logarithm.
     *
     * @return base-10 logarithm of this complex number
     */
    @NotNull
    public Complex log10() {
        return new Complex(complex.log10());
    }

    /**
     * Produces the principal square root of this complex number.
     *
     * @return principal square root of this complex number
     */
    @NotNull
    public Complex sqrt() {
        return new Complex(complex.sqrt());
    }

    /**
     * Calculates Euler's exponential {@code e^(a+bi)}.
     *
     * @return Euler's exponential of this complex number
     */
    @NotNull
    public Complex exp() {
        return new Complex(complex.exp());
    }

    /**
     * Returns the real component truncated to an {@code int} to satisfy
     * {@link Number} contract.
     */
    @Override
    public int intValue() {
        return (int) complex.getReal();
    }

    /**
     * Returns the real component truncated to a {@code long}.
     */
    @Override
    public long longValue() {
        return (long) complex.getReal();
    }

    /**
     * Returns the real component cast to a {@code float}.
     */
    @Override
    public float floatValue() {
        return (float) complex.getReal();
    }

    /**
     * Returns the real component as a {@code double}.
     */
    @Override
    public double doubleValue() {
        return complex.getReal();
    }

    /**
     * Compares complex numbers by magnitude and then lexicographically by
     * components to provide a deterministic ordering. This ordering is stable
     * enough for use in sorted collections even though complex numbers do not
     * have a natural total order in mathematics.
     */
    @Override
    public int compareTo(@NotNull Complex o) {
        int cmpMag = Double.compare(complex.abs(), o.complex.abs());
        if (cmpMag != 0) return cmpMag;

        int cmpReal = Double.compare(complex.getReal(), o.complex.getReal());
        if (cmpReal != 0) return cmpReal;

        return Double.compare(complex.getImaginary(), o.complex.getImaginary());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complex)) return false;

        Complex other = (Complex) o;
        return complex.equals(other.complex);
    }

    @Override
    public int hashCode() {
        return complex.hashCode();
    }

    /**
     * Returns the canonical string representation produced by Apache Commons,
     * which is compatible with {@link #Complex(String)} for round-tripping.
     *
     * @return canonical string representation of the complex number
     */
    @Override
    public String toString() {
        return complex.toString();
    }

    /**
     * Generates a complex number with random real and imaginary parts in the
     * {@code [0,1)} range.
     *
     * @return random complex value
     */
    @NotNull
    public static Complex random() {
        return new Complex(Math.random(), Math.random());
    }
}
