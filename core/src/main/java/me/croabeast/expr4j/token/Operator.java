package me.croabeast.expr4j.token;

import lombok.Getter;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.expression.Parameters;
import org.jetbrains.annotations.NotNull;

@Getter
public class Operator<T> implements Operation<T>, Comparable<Operator<T>> {

    private final String label;
    private final Type type;
    private final int precedence;
    private final Operation<T> operation;

    public Operator(String label, Type type, int precedence, Operation<T> operation) {
        this.label = label;
        this.type = type;
        this.precedence = precedence;
        this.operation = operation;

        if (this.precedence < 1)
            throw new Expr4jException("Invalid precedence: " + this.precedence);
    }

    @Override
    public T evaluate(Parameters<T> parameters) {
        return this.operation.evaluate(parameters);
    }

    @SuppressWarnings("all")
    public int compareTo(@NotNull Operator<T> o) {
        try {
            return (precedence == o.precedence) ?
                    (type == Type.INFIX || type == Type.POSTFIX ? 1 : -1) :
                    o.precedence - precedence;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Operator{label='" + label + "', type=" + type + ", precedence=" + precedence + '}';
    }

    public enum Type {
        PREFIX,
        POSTFIX,
        INFIX,
        INFIX_RTL
    }
}
