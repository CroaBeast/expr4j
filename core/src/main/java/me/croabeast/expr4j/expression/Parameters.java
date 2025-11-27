package me.croabeast.expr4j.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Holds lazily evaluated parameters for functions and operators. Each
 * parameter is a wrapper capable of resolving its value on demand.
 *
 * @param <T> type of values returned by the parameters
 */
public class Parameters<T> {

    private final List<Parameter<T>> parameters = new ArrayList<>();

    /**
     * Adds a parameter to the list in encounter order.
     *
     * @param parameter parameter to include
     */
    public void add(Parameter<T> parameter) {
        this.parameters.add(parameter);
    }

    /**
     * Indicates whether any parameters are present.
     *
     * @return {@code true} when no parameters are registered
     */
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    /**
     * Evaluates and returns the parameter at the specified index.
     *
     * @param index zero-based index of the parameter to evaluate
     * @return computed value
     */
    public T result(int index) {
        return parameters.get(index).result();
    }

    /**
     * Evaluates and returns the first parameter.
     *
     * @return computed value of the first parameter
     */
    public T result() {
        return result(0);
    }

    /**
     * Evaluates all parameters using the provided mapper.
     *
     * @param mapper function used to extract values from each parameter
     * @param <K>    target mapped type
     * @return list of mapped results
     */
    public <K> List<K> results(Function<Parameter<T>, K> mapper) {
        return parameters.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * Evaluates all parameters and returns their raw results.
     *
     * @return list of computed parameter values
     */
    public List<T> results() {
        return results(Parameter::result);
    }
}
