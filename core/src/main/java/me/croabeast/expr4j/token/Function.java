package me.croabeast.expr4j.token;

import lombok.Getter;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.expression.Parameters;

/**
 * Token that encapsulates a named function. Functions delegate their
 * evaluation to a backing {@link Operation} while keeping track of the
 * expected parameter count to validate parsed expressions.
 *
 * @param <T> result type calculated by the function
 */
@Getter
public class Function<T> implements Operation<T> {

    /**
     * Human-friendly label used when the function is rendered in an expression
     * or surfaced in error messages.
     */
    private final String label;

    /**
     * Declared arity for the function. Values greater than or equal to zero
     * represent a fixed number of parameters, while {@code -1} denotes a
     * variadic function signature.
     */
    private final int parameters;

    /**
     * Implementation delegate that holds the actual logic to execute. Keeping
     * it as a field ensures Lombok generates descriptive accessor Javadoc for
     * plugin authors inspecting the API surface.
     */
    private final Operation<T> operation;

    /**
     * Creates a function with an explicit arity.
     *
     * @param label       name of the function as written in expressions
     * @param parameters  required number of parameters, or {@code -1} for
     *                    variable length functions
     * @param operation   operation executed when the function is evaluated
     * @throws Expr4jException if the number of parameters is invalid
     */
    public Function(String label, int parameters, Operation<T> operation) {
        this.label = label;
        this.parameters = parameters;
        this.operation = operation;

        if (this.parameters < -1)
            throw new Expr4jException("Invalid number of parameters: " + this.parameters);
    }

    /**
     * Creates a variadic function definition.
     *
     * @param label     function name
     * @param operation logic that will be executed during evaluation
     */
    public Function(String label, Operation<T> operation) {
        this(label, -1, operation);
    }

    /**
     * Invokes the wrapped operation after deferring parameter evaluation. This
     * indirection allows functions to short-circuit or reorder the values if a
     * custom implementation needs to inspect the {@link Parameters} object
     * directly.
     *
     * @param parameters lazily evaluated parameters supplied by the parser
     * @return result produced by the backing operation
     */
    public T evaluate(Parameters<T> parameters) {
        return this.operation.evaluate(parameters);
    }

    @Override
    public String toString() {
        return "Function{label='" + label + "', parameters=" + parameters + '}';
    }
}
