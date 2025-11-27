package me.croabeast.expr4j.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Variable implements Token {

    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
