package me.croabeast.expr4j.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Parameters<T> {

    private final List<Parameter<T>> parameters = new ArrayList<>();

    public void add(Parameter<T> parameter) {
        this.parameters.add(parameter);
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public T result(int index) {
        return parameters.get(index).result();
    }

    public T result() {
        return result(0);
    }

    public <K> List<K> results(Function<Parameter<T>, K> mapper) {
        return parameters.stream().map(mapper).collect(Collectors.toList());
    }

    public List<T> results() {
        return results(Parameter::result);
    }
}
