package me.croabeast.expr4j.expression;

import lombok.Getter;
import me.croabeast.expr4j.token.Function;
import me.croabeast.expr4j.token.Operator;
import me.croabeast.expr4j.token.Token;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Node within the abstract syntax tree built by the parser. Each node keeps a
 * reference to its token and optional child nodes depending on whether it is a
 * function or operator. Nodes are lightweight containers that make it easy to
 * traverse or visualize expression trees.
 */
@Getter
public class Node {

    /**
     * Ordered children belonging to this node. Functions and operators allocate
     * this list during construction so that parameters can be attached as the
     * tree is built.
     */
    @Nullable
    private final List<Node> children;

    /**
     * Token represented by this node. It can be a literal operand, a variable
     * placeholder, or an executable such as a function or operator.
     */
    private final Token token;

    /**
     * Creates a node around the provided token and initializes its child list
     * if the token supports operands.
     *
     * @param token token to wrap
     */
    public Node(Token token) {
        this.token = token;
        this.children = token instanceof Function || token instanceof Operator ? new ArrayList<>() : null;
    }
}
