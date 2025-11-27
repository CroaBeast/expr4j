package me.croabeast.expr4j.token;

import lombok.Getter;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.expression.Parameters;
import org.jetbrains.annotations.NotNull;

/**
 * Token that models an operator inside an expression. Operators carry
 * precedence and associativity information and delegate their computation to a
 * backing {@link Operation} implementation. Use this class when registering
 * new symbols such as power, modulus, or domain-specific shortcuts that should
 * participate in the parser's precedence ladder.
 *
 * <p>Each operator instance declares both its display label and its associativity
 * type, allowing the parser to decide when implicit multiplication is
 * necessary and how to group tokens into an AST. The {@link Operation}
 * delegate can contain anything from primitive arithmetic to more exotic
 * domain logic.</p>
 *
 * @param <T> result type produced by this operator
 */
@Getter
public class Operator<T> implements Operation<T>, Comparable<Operator<T>> {

    /**
     * Symbol that appears in the textual expression. This is also used to
     * retrieve the operator from the {@link me.croabeast.expr4j.expression.Dictionary}.
     */
    private final String label;

    /**
     * Associativity descriptor indicating whether the operator appears before,
     * after, or between its operands.
     */
    private final Type type;

    /**
     * Precedence level controlling how tightly this operator binds when mixed
     * with others. Higher values are evaluated earlier during parsing.
     */
    private final int precedence;

    /**
     * Delegated computation that performs the actual calculation when the
     * operator executes. Keeping this as a field ensures Lombok surfaces the
     * accessor for plugin authors who want to introspect available operations.
     */
    private final Operation<T> operation;

    /**
     * Creates a new operator definition.
     *
     * @param label       symbol used in the expression source
     * @param type        operator position relative to its operands
     * @param precedence  precedence level where larger values bind tighter
     * @param operation   logic executed when the operator is evaluated
     * @throws Expr4jException if the precedence is lower than one
     */
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

    /**
     * Compares operators by precedence and associativity to decide evaluation
     * order during parsing.
     *
     * @param o other operator being compared
     * @return comparison result where positive values prefer the current
     * instance
     */
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

    /**
     * Describes how an operator should be positioned relative to its operands.
     */
    public enum Type {
        PREFIX,
        POSTFIX,
        INFIX,
        INFIX_RTL
    }
}
