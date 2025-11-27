package me.croabeast.expr4j.expression;

import lombok.Getter;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.token.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Stack;

@SuppressWarnings("unchecked")
@Getter
public abstract class Builder<T> {

    private Dictionary<T> dictionary = new Dictionary<>();
    private final Codec<T> codec;
    private Expression<T> expression;

    public Builder(Codec<T> codec, boolean initialize) {
        this.codec = codec;
        if (initialize) initialize();
    }

    public Builder(Codec<T> codec) {
        this(codec, true);
    }

    public void reset() {
        dictionary = new Dictionary<>();
    }

    private boolean formTree(Node node, Token token) {
        Token last = node.getToken();

        List<Node> children = node.getChildren();
        if (children == null) return false;

        if (last instanceof Function) {
            Function<T> function = (Function<T>) last;

            if (!children.isEmpty() && formTree(children.get(0), token))
                return true;

            if (children.size() < function.getParameters()) {
                children.add(0, new Node(token));
                return true;
            }
        }

        if (last instanceof Operator) {
            Operator<T> operator = (Operator<T>) last;
            Operator.Type type = operator.getType();

            if (!children.isEmpty() && formTree(children.get(0), token))
                return true;

            if (children.size() < (type == Operator.Type.INFIX || type == Operator.Type.INFIX_RTL ? 2 : 1)) {
                children.add(0, new Node(token));
                return true;
            }
        }

        return false;
    }

    private void formTree(Stack<Token> postfix) {
        while (!postfix.isEmpty()) {
            final Token token = postfix.pop();

            if (expression.getRoot() == null) {
                expression.setRoot(new Node(token));
                continue;
            }

            if (!formTree(expression.getRoot(), token))
                throw new Expr4jException("Token tree not parsed correctly");
        }
    }

    protected abstract void initialize();

    @NotNull
    public Expression<T> build(String expression) throws Expr4jException {
        try {
            Tokenizer<T> tokenizer = new Tokenizer<>(dictionary, codec);
            List<Token> list = tokenizer.tokenize(expression);

            this.expression = new Expression<>(dictionary, codec);
            Parser<T> parser = new Parser<>();
            formTree(parser.parse(list));

            return this.expression;
        }
        finally {
            this.expression = null;
        }
    }

    static class Parser<T> {

        private Stack<Token> postfix, operatorStack;
        private Stack<Integer> functionStack;

        void resetStacks() {
            postfix = new Stack<>();
            operatorStack = new Stack<>();
            functionStack = new Stack<>();
        }

        Parser() {
            resetStacks();
        }

        void validate(boolean bool, String message) {
            if (bool) throw new Expr4jException(message);
        }

        void validateNotPostfix(Token token) {
            if (token instanceof Operator)
                validate(((Operator<T>) token).getType() != Operator.Type.POSTFIX, "Invalid not postfix operator");
        }

        void validateOpenSeparator(Token token) {
            if (!(token instanceof Separator)) return;

            Separator s = (Separator) token;
            validate(s == Separator.OPEN_BRACKET || s == Separator.COMMA, "Open bracket or comma");
        }

        Stack<Token> parse(List<Token> tokenList) {
            resetStacks();

            boolean probableZeroFunction = false;
            Token lastToken = null;

            for (int i = 0; i < tokenList.size(); i++) {
                Token token = tokenList.get(i);

                if (token instanceof Separator) {
                    Separator separator = (Separator) token;

                    switch (separator) {
                        case OPEN_BRACKET:
                            operatorStack.push(separator);
                            break;

                        case CLOSE_BRACKET: {
                            validateNotPostfix(lastToken);
                            validateOpenSeparator(lastToken);

                            if (probableZeroFunction) {
                                validate(functionStack.isEmpty(), "Invalid empty function");

                                functionStack.pop();
                                functionStack.push(0);
                            }

                            boolean flag = true;

                            while (!operatorStack.isEmpty()) {
                                Token opToken = operatorStack.peek();

                                if (opToken instanceof Function) {
                                    Function<T> function = (Function<T>) operatorStack.pop();
                                    validate(functionStack.isEmpty(), "Invalid empty function");

                                    int parameters = functionStack.pop();
                                    if (function.getParameters() == -1)
                                        function = new Function<>(function.getLabel(), parameters, function.getOperation());

                                    else if (function.getParameters() != parameters)
                                        throw new Expr4jException("Incorrect number of parameters for function: " + function.getLabel());

                                    postfix.push(function);
                                    flag = false;
                                    break;
                                }

                                if (opToken instanceof Separator) {
                                    operatorStack.pop();
                                    Token t;
                                    if (!operatorStack.isEmpty() &&
                                            (t = operatorStack.peek()) instanceof Operator &&
                                            ((Operator<T>) t).getType() == Operator.Type.PREFIX)
                                        postfix.push(operatorStack.pop());

                                    flag = false;
                                    break;
                                }

                                postfix.push(operatorStack.pop());
                            }

                            validate(flag, "Unmatched number of parenthesis");
                            break;
                        }

                        case COMMA: {
                            validate(lastToken instanceof Function, "Invalid function on comma");
                            validateNotPostfix(lastToken);
                            validateOpenSeparator(lastToken);

                            while (!operatorStack.isEmpty() && !(operatorStack.peek() instanceof Function))
                                postfix.push(operatorStack.pop());

                            validate(functionStack.isEmpty(), "Invalid empty function");
                            functionStack.push(functionStack.pop() + 1);
                            break;
                        }

                        default: break;
                    }

                    probableZeroFunction = false;
                }

                else if (token instanceof Function) {
                    Function<T> function = (Function<T>) token;

                    Token next = i != tokenList.size() - 1 ? tokenList.get(i + 1) : null;
                    validate(
                            next != Separator.OPEN_BRACKET,
                            "Missing open bracket for function: " + function.getLabel()
                    );

                    i++;

                    functionStack.push(function.getParameters() == 0 ? 0 : 1);
                    operatorStack.push(function);

                    if (function.getParameters() == -1) probableZeroFunction = true;
                }

                else if (token instanceof Operator) {
                    Operator<T> operator = (Operator<T>) token;

                    switch (operator.getType()) {
                        case PREFIX:
                            Token temp;
                            while (!operatorStack.isEmpty() &&
                                    (temp = operatorStack.peek()) instanceof Operator &&
                                    operator.compareTo((Operator<T>) temp) > 0)
                                postfix.push(operatorStack.pop());
                            break;

                        case POSTFIX:
                            validate(lastToken == null, "Last token is null");
                            validateNotPostfix(lastToken);
                            postfix.push(operator);
                            break;

                        case INFIX:
                        case INFIX_RTL:
                            validate(lastToken == null, "Last token is null");
                            validate(lastToken instanceof Function, "Token shouldn't be a function");
                            validateNotPostfix(lastToken);
                            validateOpenSeparator(lastToken);

                            operatorStack.push(operator);
                            break;

                        default: break;
                    }

                    probableZeroFunction = false;
                }

                else if (token instanceof Operand || token instanceof Variable) {
                    postfix.push(token);
                    probableZeroFunction = false;
                }

                else throw new Expr4jException("Invalid token implementation: " + token.getClass());

                lastToken = token;
            }

            while (!operatorStack.isEmpty()) {
                Token temp = operatorStack.peek();
                validate(temp instanceof Operator || temp instanceof Separator, "Unmatched number of parenthesis");
                postfix.push(operatorStack.pop());
            }

            return postfix;
        }
    }
}
