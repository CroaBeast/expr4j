package me.croabeast.expr4j.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.croabeast.expr4j.exception.Expr4jException;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public enum Separator implements Token {
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    COMMA(",");

    private final String label;

    @Override
    public String toString() {
        return "Separator{label='" + label + "'}";
    }

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
