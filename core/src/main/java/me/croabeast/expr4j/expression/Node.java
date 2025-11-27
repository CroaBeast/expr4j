package me.croabeast.expr4j.expression;

import lombok.Getter;
import me.croabeast.expr4j.token.Function;
import me.croabeast.expr4j.token.Operator;
import me.croabeast.expr4j.token.Token;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Node {

    @Nullable
    private final List<Node> children;
    private final Token token;

    public Node(Token token) {
        this.token = token;
        this.children = token instanceof Function || token instanceof Operator ? new ArrayList<>() : null;
    }
}
