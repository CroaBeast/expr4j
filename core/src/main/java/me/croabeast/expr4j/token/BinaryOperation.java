package me.croabeast.expr4j.token;

import me.croabeast.expr4j.expression.Parameters;

public interface BinaryOperation<T> extends Operation<T> {

    T evaluate(T left, T right);

    @Override
    default T evaluate(Parameters<T> parameters) {
        return evaluate(parameters.result(), parameters.result(1));
    }
}
