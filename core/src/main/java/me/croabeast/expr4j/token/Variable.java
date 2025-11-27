package me.croabeast.expr4j.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Token representing a variable placeholder inside an expression. Variables
 * are resolved at evaluation time using the provided map of assignments.
 */
@RequiredArgsConstructor
@Getter
public class Variable implements Token {

    /**
     * Symbolic name of the variable as it appears in the expression source.
     */
    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
