package me.croabeast.expr4j.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class Operand<T> implements Token {

    @NotNull
    private final T value;

    @NotNull
    public String getLabel() {
        return value.toString();
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
