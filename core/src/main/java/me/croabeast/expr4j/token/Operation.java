package me.croabeast.expr4j.token;

import me.croabeast.expr4j.expression.Parameters;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an action that can be performed during expression evaluation.
 * Operations may be functions or operators and therefore extend {@link Token}
 * while also exposing the ability to compute a value from supplied
 * {@link Parameters}. Builders typically supply concrete implementations that
 * map to numeric operations, but the generic type allows custom domains too.
 *
 * @param <T> result type handled by the builder and evaluation pipeline
 */
@FunctionalInterface
public interface Operation<T> extends Token {

    /**
     * Default label used by operations that are not meant to appear directly
     * in the tokenized expression (for instance, inline lambdas backing
     * higher-level operators).
     *
     * @return an empty string representing an anonymous operation label
     */
    @NotNull
    default String getLabel() {
        return "";
    }

    /**
     * Executes the operation using the provided arguments.
     *
     * @param parameters ordered set of lazily evaluated arguments
     * @return the resulting value after applying the operation logic
     */
    T evaluate(Parameters<T> parameters);
}
