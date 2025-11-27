package me.croabeast.expr4j.expression;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Represents a single argument in an expression tree. Parameters defer
 * evaluation until the value is explicitly requested, allowing operators and
 * functions to control when child nodes are computed.
 *
 * @param <T> evaluation type
 */
@RequiredArgsConstructor
public class Parameter<T> {

    private final Expression<T> expression;
    private final Node node;
    private final Map<String, T> variables;

    /**
     * Evaluates the associated node within its expression context.
     *
     * @return computed parameter value
     */
    public T result() {
        return expression.evaluate(node, variables).getValue();
    }
}
