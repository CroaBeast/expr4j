package me.croabeast.expr4j.token;

import lombok.Getter;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.expression.Parameters;

@Getter
public class Function<T> implements Operation<T> {

    private final String label;
    private final int parameters;
    private final Operation<T> operation;

    public Function(String label, int parameters, Operation<T> operation) {
        this.label = label;
        this.parameters = parameters;
        this.operation = operation;

        if (this.parameters < -1)
            throw new Expr4jException("Invalid number of parameters: " + this.parameters);
    }

    public Function(String label, Operation<T> operation) {
        this(label, -1, operation);
    }

    public T evaluate(Parameters<T> parameters) {
        return this.operation.evaluate(parameters);
    }

    @Override
    public String toString() {
        return "Function{label='" + label + "', parameters=" + parameters + '}';
    }
}
