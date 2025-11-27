package me.croabeast.expr4j.expression;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Translates between string representations and typed operands. Codec
 * implementations define how literals are parsed from text and how values are
 * converted back into strings for display.
 *
 * @param <T> operand type the codec works with
 */
public interface Codec<T> {

    /**
     * Parses a literal fragment into an operand value.
     *
     * @param string textual representation of a value
     * @return parsed operand
     */
    @NotNull
    T toOperand(String string);

    /**
     * Converts an operand into a printable string.
     *
     * @param operand operand to display
     * @return stringified operand
     */
    @NotNull
    String toString(T operand);

    /**
     * Supplies regex patterns that help the tokenizer detect literal tokens.
     *
     * @return list of regex patterns ordered by priority
     */
    @NotNull
    default List<String> getPatterns() {
        List<String> list = new ArrayList<>();
        list.add("(-?\\d+)(\\.\\d+)?(e-|e\\+|e|\\d+)\\d+");
        list.add("\\d*\\.?\\d+");
        return list;
    }
}
