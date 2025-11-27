package me.croabeast.expr4j.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Leaf token that wraps a literal value. Operands are produced by codecs when
 * parsing textual expressions and are returned unchanged during evaluation.
 *
 * @param <T> literal type contained in the operand
 */
@RequiredArgsConstructor
@Getter
public class Operand<T> implements Token {

    /**
     * Concrete literal or computed value represented by the token. The
     * tokenizer fills this with parsed operands, and evaluators simply return it
     * unchanged.
     */
    @NotNull
    private final T value;

    /**
     * Returns the string representation of the wrapped value.
     *
     * @return value converted to text
     */
    @NotNull
    public String getLabel() {
        return value.toString();
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
