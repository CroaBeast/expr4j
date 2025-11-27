package me.croabeast.expr4j.token;

import org.jetbrains.annotations.NotNull;

/**
 * Represents any syntactic element that can appear in an expression tree.
 * Tokens expose a human-readable label that is later used by the parser and
 * the pretty-printer to reconstruct the expression source.
 */
public interface Token {

    /**
     * Returns the symbolic label associated with the token (for example
     * an operator sign, variable name or function identifier).
     *
     * @return never {@code null} textual label that identifies the token
     */
    @NotNull
    String getLabel();
}
