package me.croabeast.expr4j.expression;

import lombok.Getter;
import me.croabeast.expr4j.token.Function;
import me.croabeast.expr4j.token.Operation;
import me.croabeast.expr4j.token.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Dictionary<T> {

    private final Map<String, Operation<T>> prefixes = new TreeMap<>(),
            postfixes = new TreeMap<>(),
            infixes = new TreeMap<>(),
            functions = new TreeMap<>();

    @Getter
    private final Map<String, T> constants = new TreeMap<>();

    public Dictionary<T> addOperator(Operator<T> operator) {
        Objects.requireNonNull(operator);

        String label = operator.getLabel();
        switch (operator.getType()) {
            case PREFIX:
                prefixes.put(label, operator);
                break;

            case POSTFIX:
                postfixes.put(label, operator);
                break;

            default:
                infixes.put(label, operator);
                break;
        }

        return this;
    }

    public Dictionary<T> removeOperator(String label, Operator.Type type) {
        if (type == null) {
            prefixes.remove(label);
            postfixes.remove(label);
            infixes.remove(label);
            return this;
        }

        switch (type) {
            case PREFIX:
                prefixes.remove(label);
                break;

            case POSTFIX:
                postfixes.remove(label);
                break;

            default:
                infixes.remove(label);
                break;
        }

        return this;
    }

    public Dictionary<T> removeOperator(String label) {
        return removeOperator(label, null);
    }

    @Nullable
    public Operation<T> getOperator(String label, Operator.Type type) {
        if (type == null) return infixes.get(label);

        switch (type) {
            case PREFIX: return prefixes.get(label);
            case POSTFIX: return postfixes.get(label);
            default: return infixes.get(label);
        }
    }

    public boolean hasOperator(String label, Operator.Type type) {
        try {
            return getOperator(label, type) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public Dictionary<T> addFunction(Function<T> function) {
        Objects.requireNonNull(function);

        functions.put(function.getLabel(), function);
        return this;
    }

    public Dictionary<T> removeFunction(String label) {
        functions.remove(label);
        return this;
    }

    public Operation<T> getFunction(String label) {
        return functions.get(label);
    }

    public boolean hasFunction(String label) {
        return getFunction(label) != null;
    }

    public Dictionary<T> addConstant(String label, T constant) {
        Objects.requireNonNull(constant);
        constants.put(label, constant);
        return this;
    }

    public Dictionary<T> removeConstant(String label) {
        constants.remove(label);
        return this;
    }

    @Nullable
    public T getConstant(String label) {
        return constants.get(label);
    }

    public boolean hasConstant(String label) {
        return getConstant(label) != null;
    }

    @NotNull
    Set<String> getExecutables() {
        Set<String> executables = new TreeSet<>();
        executables.addAll(prefixes.keySet());
        executables.addAll(postfixes.keySet());
        executables.addAll(infixes.keySet());
        executables.addAll(functions.keySet());
        return executables;
    }
}
