package me.croabeast.expr4j.token;

import me.croabeast.expr4j.expression.Parameters;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Operation<T> extends Token {

    @NotNull
    default String getLabel() {
        return "";
    }

    T evaluate(Parameters<T> parameters);
}
