package me.croabeast.expr4j.expression;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class Parameter<T> {

    private final Expression<T> expression;
    private final Node node;
    private final Map<String, T> variables;

    public T result() {
        return expression.evaluate(node, variables).getValue();
    }
}
