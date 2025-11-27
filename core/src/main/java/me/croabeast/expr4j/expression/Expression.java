package me.croabeast.expr4j.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.token.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Immutable representation of a parsed expression tree. Instances can be
 * evaluated multiple times with different variable bindings and are responsible
 * for orchestrating token execution.
 *
 * <p>The expression keeps a reference to its originating {@link Dictionary} and
 * {@link Codec} so that downstream code can inspect how values are formatted or
 * determine which constants were registered at build time. This design makes it
 * easy to serialize or log expressions with full contextual information.</p>
 *
 * @param <T> result type produced by the expression
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings("unchecked")
public class Expression<T> {

    @Setter
    /**
     * Root node of the parsed abstract syntax tree. Set during the build
     * process and later reused for evaluation and pretty-printing.
     */
    private Node root;

    /**
     * Registry of functions, operators, and constants used to evaluate the
     * expression. Exposed for tooling that wants to enumerate available
     * symbols or reconstruct documentation at runtime.
     */
    private final Dictionary<T> dictionary;

    /**
     * Codec that produced the operands. Builders can reuse it to present
     * results in a human-friendly way or to understand which literal patterns
     * were allowed when the expression was parsed.
     */
    private final Codec<T> codec;

    protected Operand<T> evaluate(Node node, Map<String, T> variables) {
        Token token = node.getToken();
        List<Node> children = node.getChildren();
        String label = token.getLabel();

        if (token instanceof Variable) {
            if (!variables.containsKey(label))
                throw new Expr4jException("Variable not found: " + label);

            return new Operand<>(variables.get(label));
        }

        if (token instanceof Function) {
            Function<T> function = (Function<T>) token;
            if (children == null || children.size() != function.getParameters())
                throw new Expr4jException("Invalid function: " + label);

            Parameters<T> parameters = new Parameters<>();
            for (Node n : children)
                parameters.add(new Parameter<>(this, n, variables));

            return new Operand<>(function.evaluate(parameters));
        }

        if (token instanceof Operator) {
            Operator<T> operator = (Operator<T>) token;
            Operator.Type type = operator.getType();

            int count = type == Operator.Type.INFIX || type == Operator.Type.INFIX_RTL ? 2 : 1;
            if (children == null || children.size() != count)
                throw new Expr4jException("Invalid operator: " + label);

            Parameters<T> parameters = new Parameters<>();
            for (Node n : children)
                parameters.add(new Parameter<>(this, n, variables));

            return new Operand<>(operator.evaluate(parameters));
        }

        try {
            return (Operand<T>) token;
        } catch (Exception e) {
            throw new Expr4jException("Invalid token: " + token.getClass());
        }
    }

    /**
     * Evaluates the expression using the provided variable assignments.
     *
     * @param variables mapping of variable names to values
     * @return computed result of the expression
     */
    @NotNull
    public T evaluate(Map<String, T> variables) {
        if (root == null)
            throw new Expr4jException("Root node not defined");

        Map<String, T> map = new HashMap<>(dictionary.getConstants());
        if (variables != null && !variables.isEmpty())
            map.putAll(variables);

        return evaluate(root, map).getValue();
    }

    /**
     * Evaluates the expression without external variables, relying solely on
     * constants embedded in the dictionary.
     *
     * @return computed result
     */
    @NotNull
    public T evaluate() {
        return evaluate(new HashMap<>());
    }

    @NotNull
    protected String toString(Node node) {
        if (node == null) return "";

        Token token = node.getToken();
        List<Node> children = node.getChildren();
        String label = token.getLabel();

        if (token instanceof Function) {
            Function<T> function = (Function<T>) token;

            if (children == null || children.size() != function.getParameters())
                throw new Expr4jException("Invalid function: " + label);

            return label + "(" + children.stream()
                    .map(this::toString)
                    .collect(Collectors.joining(", ")) + ")";
        }

        if (token instanceof Operator) {
            Operator<T> operator = (Operator<T>) token;
            Operator.Type type = operator.getType();

            int count = type == Operator.Type.INFIX || type == Operator.Type.INFIX_RTL ? 2 : 1;
            if (children == null || children.size() != count)
                throw new Expr4jException("Invalid operator: " + label);

            if (count == 2) {
                StringBuilder sb = new StringBuilder();

                Node left = children.get(0);
                Token leftToken = left.getToken();

                Node right = children.get(1);
                Token rightToken = right.getToken();

                if (leftToken instanceof Operator) {
                    Operator<T> leftOperator = (Operator<T>) leftToken;
                    Operator.Type leftType = leftOperator.getType();

                    if (!leftOperator.getLabel().equals("*") &&
                            (leftType == Operator.Type.INFIX || leftType == Operator.Type.INFIX_RTL) &&
                            operator.compareTo(leftOperator) < 0)
                        sb.append("(").append(toString(left)).append(")");
                    else sb.append(toString(left));
                }
                else sb.append(toString(left));

                if (rightToken instanceof Operator) {
                    Operator<T> rightOperator = (Operator<T>) rightToken;
                    Operator.Type rightType = rightOperator.getType();

                    if (!rightOperator.getLabel().equals("*") &&
                            (rightType == Operator.Type.INFIX || rightType == Operator.Type.INFIX_RTL) &&
                            operator.compareTo(rightOperator) < 0)
                        sb.append("(").append(toString(right)).append(")");
                    else sb.append(toString(right));
                }
                else sb.append(toString(right));

                return sb.toString();
            }

            boolean prefix = operator.getType() == Operator.Type.PREFIX;
            Node child = children.get(0);
            Token childToken = child.getToken();

            if (label.equals("+") || label.equals("-")) {
                if (childToken instanceof Operator) {
                    prefix = ((Operator<T>) childToken).getType() == Operator.Type.PREFIX;
                    return label + (prefix ? "" : "(") + toString(child) + (prefix ? "" : ")");
                }

                return label + toString(child);
            }

            if (childToken instanceof Operator || childToken instanceof Function)
                return (prefix ? label : "") + "("+ toString(child) + ")" + (prefix ? "" : (" " + label));

            return (prefix ? (label + " ") : "") + toString(child) + (prefix ? "" : (" " + label));
        }

        return token instanceof Operand ?
                codec.toString(((Operand<T>) token).getValue()) :
                label;
    }

    /**
     * Returns the expression rebuilt as an infix string.
     *
     * @return human-readable representation of the expression
     */
    @Override
    public String toString() {
        return toString(root);
    }
}
