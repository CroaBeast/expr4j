package me.croabeast.expr4j.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.croabeast.expr4j.exception.Expr4jException;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of tokens that separate other tokens in an expression, such as
 * brackets and commas. These separators guide how the parser constructs the
 * abstract syntax tree.
 */
@RequiredArgsConstructor
@Getter
public enum Separator implements Token {
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    COMMA(",");

    /**
     * Raw label used to identify the separator during tokenization and to
     * reconstruct the expression string.
     */
    private final String label;

    @Override
    public String toString() {
        return "Separator{label='" + label + "'}";
    }

    /**
     * Resolves a raw label into the corresponding {@link Separator} constant.
     *
     * @param label literal representation such as "(" or ","
     * @return matching separator instance
     * @throws Expr4jException if the label does not match a supported separator
     */
    @NotNull
    public static Separator getSeparator(String label) {
        switch (label) {
            case "(": return OPEN_BRACKET;
            case ")": return CLOSE_BRACKET;
            case ",": return COMMA;
            default: throw new Expr4jException("Invalid separator");
        }
    }
}
