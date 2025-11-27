package me.croabeast.expr4j.token;

import me.croabeast.expr4j.expression.Parameters;

/**
 * Specialization of {@link Operation} that consumes a single operand.
 * Implementations typically wrap math functions such as sine, absolute value
 * or factorial and can be plugged directly into the dictionary.
 *
 * @param <T> domain type used during expression evaluation
 */
public interface UnaryOperation<T> extends Operation<T> {

    /**
     * Executes the unary logic on a single operand instance.
     *
     * @param operand value passed from the expression tree
     * @return result of applying the operation to the operand
     */
    T evaluate(T operand);

    /**
     * Adapts the parameter container provided by the evaluator into a simple
     * unary invocation. This keeps custom implementations focused on their math
     * logic instead of handling parser plumbing.
     */
    @Override
    default T evaluate(Parameters<T> parameters) {
        return evaluate(parameters.result());
    }
}
