package me.croabeast.expr4j.token;

import me.croabeast.expr4j.expression.Parameters;

public interface UnaryOperation<T> extends Operation<T> {

    T evaluate(T operand);

    @Override
    default T evaluate(Parameters<T> parameters) {
        return evaluate(parameters.result());
    }
}
