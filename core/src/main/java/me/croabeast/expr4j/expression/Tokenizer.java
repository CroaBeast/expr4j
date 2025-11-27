package me.croabeast.expr4j.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.croabeast.expr4j.exception.Expr4jException;
import me.croabeast.expr4j.token.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class Tokenizer<T> {

    private final Dictionary<T> dictionary;
    private final Codec<T> codec;

    @SuppressWarnings("unchecked")
    private boolean postOrInFixOperatorAllowed(Token token) {
        if (token == null) return false;

        if (token instanceof Separator)
            return token == Separator.CLOSE_BRACKET;

        if (token instanceof Operator)
            return (((Operator<T>) token).getType() == Operator.Type.POSTFIX);

        return token instanceof Operand || token instanceof Variable;
    }

    private void addImplicitMultiplication(List<Token> list, Token last) {
        if (postOrInFixOperatorAllowed(last))
            list.add(dictionary.getOperator("*", null));
    }

    @NotNull
    public List<Token> tokenize(String expression) {
        if (expression == null || expression.isEmpty() || expression.chars().allMatch(Character::isWhitespace))
            throw new Expr4jException("Expression is blank, empty or null");

        List<Token> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(dictionary.getExecutables()
                .stream().map(Pattern::quote)
                .sorted((e1, e2) -> (e2.length() - e1.length()))
                .collect(Collectors.joining("|")));

        Pattern unary = Pattern.compile("[+-]");
        Pattern sep = Pattern.compile("[(),]");
        Pattern var = Pattern.compile("[a-zA-Z]+[0-9]*[a-zA-Z]*");
        Pattern whitespace = Pattern.compile("\\s+");

        List<Pattern> operands = new ArrayList<>();
        for (String string : codec.getPatterns()) operands.add(Pattern.compile(string));

        boolean probableUnary = true;
        int index = 0;
        Token lastToken = null;

        outer: while (index < expression.length()) {
            Matcher matcher;

            matcher = sep.matcher(expression.substring(index));
            if (matcher.lookingAt()) {
                String match = matcher.group();
                index += match.length();

                Separator separator = Separator.getSeparator(match);
                if (separator == Separator.OPEN_BRACKET) {
                    addImplicitMultiplication(list, lastToken);
                    probableUnary = true;
                }
                else probableUnary = separator != Separator.CLOSE_BRACKET;

                list.add(separator);
                lastToken = separator;
                continue;
            }

            matcher = unary.matcher(expression.substring(index));
            if (probableUnary && matcher.lookingAt()) {
                String match = matcher.group();
                index += match.length();

                Operation<T> operator = dictionary.getOperator(match, Operator.Type.PREFIX);
                list.add(operator);

                probableUnary = false;
                lastToken = operator;
                continue;
            }

            matcher = pattern.matcher(expression.substring(index));
            if (matcher.lookingAt()) {
                String match = matcher.group();
                index += match.length();

                if (dictionary.hasFunction(match)) {
                    Operation<T> function = dictionary.getFunction(match);

                    addImplicitMultiplication(list, lastToken);
                    list.add(function);

                    probableUnary = false;
                    lastToken = function;
                    continue;
                }

                Operation<T> operator = null;
                if (postOrInFixOperatorAllowed(lastToken)) {
                    operator = dictionary.getOperator(match, Operator.Type.POSTFIX);
                    if (operator == null) operator = dictionary.getOperator(match, null);
                }

                if (operator == null)
                    operator = dictionary.getOperator(match, Operator.Type.PREFIX);

                Operator<T> op = (Operator<T>) operator;
                if (op == null)
                    throw new Expr4jException("Undefined symbol: " + match);

                if (op.getType() == Operator.Type.PREFIX) addImplicitMultiplication(list, lastToken);
                list.add(operator);

                probableUnary = op.getType() != Operator.Type.POSTFIX;
                lastToken = operator;
                continue;
            }

            for (Pattern number : operands) {
                matcher = number.matcher(expression.substring(index));
                if (!matcher.lookingAt()) continue;

                String match = matcher.group();
                index += match.length();

                addImplicitMultiplication(list, lastToken);

                Operand<T> operand = new Operand<>(codec.toOperand(match));
                list.add(operand);

                probableUnary = false;
                lastToken = operand;
                continue outer;
            }

            matcher = var.matcher(expression.substring(index));
            if (matcher.lookingAt()) {
                String match = matcher.group();
                index += match.length();

                addImplicitMultiplication(list, lastToken);

                Variable variable = new Variable(match);
                list.add(variable);

                probableUnary = false;
                lastToken = variable;

                continue;
            }

            matcher = whitespace.matcher(expression.substring(index));
            if (matcher.lookingAt()) {
                index += matcher.group().length();
                continue;
            }

            throw new Expr4jException("Invalid expression at '" + matcher.group() + "'");
        }

        return list;
    }
}
