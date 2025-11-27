package me.croabeast.expr4j.expression;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface Codec<T> {
    
    @NotNull
    T toOperand(String string);

    @NotNull
    String toString(T operand);

    @NotNull
    default List<String> getPatterns() {
        List<String> list = new ArrayList<>();
        list.add("(-?\\d+)(\\.\\d+)?(e-|e\\+|e|\\d+)\\d+");
        list.add("\\d*\\.?\\d+");
        return list;
    }
}
