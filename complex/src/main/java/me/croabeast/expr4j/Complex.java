package me.croabeast.expr4j;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class Complex extends Number implements Comparable<Complex> {

    private static final long serialVersionUID = 20251127L;

    public static final Complex ZERO = new Complex(0.0, 0.0);
    public static final Complex ONE = new Complex(1.0, 0.0);
    public static final Complex I = new Complex(0.0, 1.0);
    public static final Complex PI = new Complex(Math.PI, 0.0);
    public static final Complex E = new Complex(Math.E, 0.0);

    private final org.apache.commons.numbers.complex.Complex complex;

    public Complex(double real, double imaginary) {
        this(org.apache.commons.numbers.complex.Complex.ofCartesian(real, imaginary));
    }

    public Complex(String string) {
        this(org.apache.commons.numbers.complex.Complex.parse(string));
    }

    @NotNull
    public org.apache.commons.numbers.complex.Complex asApache() {
        return complex;
    }

    public double getReal() {
        return complex.getReal();
    }

    public double getImaginary() {
        return complex.getImaginary();
    }

    @NotNull
    public Complex negate() {
        return new Complex(complex.negate());
    }

    @NotNull
    public Complex add(@NotNull Complex other) {
        return new Complex(complex.add(other.complex));
    }

    @NotNull
    public Complex subtract(@NotNull Complex other) {
        return new Complex(complex.subtract(other.complex));
    }

    @NotNull
    public Complex multiply(@NotNull Complex other) {
        return new Complex(complex.multiply(other.complex));
    }

    @NotNull
    public Complex divide(@NotNull Complex other) {
        return new Complex(complex.divide(other.complex));
    }

    @NotNull
    public Complex pow(@NotNull Complex other) {
        return new Complex(complex.pow(other.complex));
    }

    @NotNull
    public Complex pow(double other) {
        return new Complex(complex.pow(other));
    }

    public double abs() {
        return complex.abs();
    }

    @NotNull
    public Complex sin() {
        return new Complex(complex.sin());
    }

    @NotNull
    public Complex cos() {
        return new Complex(complex.cos());
    }

    @NotNull
    public Complex tan() {
        return new Complex(complex.tan());
    }

    @NotNull
    public Complex asin() {
        return new Complex(complex.asin());
    }

    @NotNull
    public Complex acos() {
        return new Complex(complex.acos());
    }

    @NotNull
    public Complex atan() {
        return new Complex(complex.atan());
    }

    @NotNull
    public Complex sinh() {
        return new Complex(complex.sinh());
    }

    @NotNull
    public Complex cosh() {
        return new Complex(complex.cosh());
    }

    @NotNull
    public Complex tanh() {
        return new Complex(complex.tanh());
    }

    @NotNull
    public Complex asinh() {
        return new Complex(complex.asinh());
    }

    @NotNull
    public Complex acosh() {
        return new Complex(complex.acosh());
    }

    @NotNull
    public Complex atanh() {
        return new Complex(complex.atanh());
    }

    @NotNull
    public Complex log() {
        return new Complex(complex.log());
    }

    @NotNull
    public Complex log10() {
        return new Complex(complex.log10());
    }

    @NotNull
    public Complex sqrt() {
        return new Complex(complex.sqrt());
    }

    @NotNull
    public Complex exp() {
        return new Complex(complex.exp());
    }

    @Override
    public int intValue() {
        return (int) complex.getReal();
    }

    @Override
    public long longValue() {
        return (long) complex.getReal();
    }

    @Override
    public float floatValue() {
        return (float) complex.getReal();
    }

    @Override
    public double doubleValue() {
        return complex.getReal();
    }

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

    @Override
    public String toString() {
        return complex.toString();
    }

    @NotNull
    public static Complex random() {
        return new Complex(Math.random(), Math.random());
    }
}
