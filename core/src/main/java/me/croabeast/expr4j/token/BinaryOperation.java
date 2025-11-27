package me.croabeast.expr4j.token;

import me.croabeast.expr4j.expression.Parameters;

/**
 * {@link Operation} that works with two operands. Binary implementations are
 * used for common arithmetic operators such as addition, multiplication or
 * exponentiation.
 *
 * @param <T> evaluation result type
 */
public interface BinaryOperation<T> extends Operation<T> {

    /**
     * Applies the operation to the given left and right operands.
     *
     * @param left  first operand
     * @param right second operand
     * @return operation result
     */
    T evaluate(T left, T right);

    /**
     * Convenience bridge that adapts the positional parameters supplied by the
     * parser into the explicit {@code left} and {@code right} values expected by
     * the operation. Implementations can override this if they need lazy
     * evaluation semantics, but the eager default fits most arithmetic
     * scenarios.
     */
    @Override
    default T evaluate(Parameters<T> parameters) {
        return evaluate(parameters.result(), parameters.result(1));
    }
}
