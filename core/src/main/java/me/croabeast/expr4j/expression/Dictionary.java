package me.croabeast.expr4j.expression;

import lombok.Getter;
import me.croabeast.expr4j.token.Function;
import me.croabeast.expr4j.token.Operation;
import me.croabeast.expr4j.token.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Registry of available operators, functions and constants used during
 * tokenization and evaluation. The dictionary allows builders to register or
 * remove executable elements dynamically.
 *
 * <p>Dictionaries are intentionally mutable so integrations can tailor the
 * parsing surface to their domain. For example, a physics plugin might register
 * vector-specific operators while keeping the rest of the API unchanged.</p>
 *
 * @param <T> numeric or domain type handled by the expressions
 */
public class Dictionary<T> {

    /**
     * Operators that precede their operand (e.g., unary minus). Stored by label
     * for quick lookup during tokenization.
     */
    private final Map<String, Operation<T>> prefixes = new TreeMap<>(),
            postfixes = new TreeMap<>(),
            infixes = new TreeMap<>(),
            functions = new TreeMap<>();

    /**
     * Fixed values that can be referenced directly in expressions. Keeping this
     * map visible through Lombok helps tooling display available constants in
     * documentation or IDE hints.
     */
    @Getter
    private final Map<String, T> constants = new TreeMap<>();

    /**
     * Registers an operator so it can be parsed in expressions.
     *
     * @param operator operator instance to register
     * @return this dictionary for chaining
     */
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

    /**
     * Removes an operator by its label and optionally its type. When the type
     * is {@code null} all matching operators are removed.
     *
     * @param label operator symbol
     * @param type  operator type or {@code null} to remove all
     * @return this dictionary for chaining
     */
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

    /**
     * Removes operators with the given label regardless of type.
     *
     * @param label operator symbol
     * @return this dictionary for chaining
     */
    public Dictionary<T> removeOperator(String label) {
        return removeOperator(label, null);
    }

    /**
     * Retrieves an operator by label and type.
     *
     * @param label operator symbol
     * @param type  operator type or {@code null} for infix lookup
     * @return matching operator or {@code null}
     */
    @Nullable
    public Operation<T> getOperator(String label, Operator.Type type) {
        if (type == null) return infixes.get(label);

        switch (type) {
            case PREFIX: return prefixes.get(label);
            case POSTFIX: return postfixes.get(label);
            default: return infixes.get(label);
        }
    }

    /**
     * Indicates whether an operator with the given label and type exists.
     *
     * @param label operator symbol
     * @param type  operator type
     * @return {@code true} if found, {@code false} otherwise
     */
    public boolean hasOperator(String label, Operator.Type type) {
        try {
            return getOperator(label, type) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Registers a function.
     *
     * @param function function to register
     * @return this dictionary for chaining
     */
    public Dictionary<T> addFunction(Function<T> function) {
        Objects.requireNonNull(function);

        functions.put(function.getLabel(), function);
        return this;
    }

    /**
     * Unregisters a function by name.
     *
     * @param label function identifier
     * @return this dictionary for chaining
     */
    public Dictionary<T> removeFunction(String label) {
        functions.remove(label);
        return this;
    }

    /**
     * Retrieves a function by its label.
     *
     * @param label function identifier
     * @return matching function or {@code null}
     */
    public Operation<T> getFunction(String label) {
        return functions.get(label);
    }

    /**
     * Checks whether a function has been registered.
     *
     * @param label function identifier
     * @return {@code true} if it exists
     */
    public boolean hasFunction(String label) {
        return getFunction(label) != null;
    }

    /**
     * Adds a named constant that can be referenced directly in expressions.
     *
     * @param label    constant name
     * @param constant value assigned to the constant
     * @return this dictionary for chaining
     */
    public Dictionary<T> addConstant(String label, T constant) {
        Objects.requireNonNull(constant);
        constants.put(label, constant);
        return this;
    }

    /**
     * Removes a constant from the registry.
     *
     * @param label constant name
     * @return this dictionary for chaining
     */
    public Dictionary<T> removeConstant(String label) {
        constants.remove(label);
        return this;
    }

    /**
     * Retrieves a constant value by its label.
     *
     * @param label constant name
     * @return stored value or {@code null}
     */
    @Nullable
    public T getConstant(String label) {
        return constants.get(label);
    }

    /**
     * Checks if a constant with the given name exists.
     *
     * @param label constant name
     * @return {@code true} if present
     */
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
