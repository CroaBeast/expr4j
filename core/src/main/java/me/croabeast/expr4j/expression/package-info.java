/**
 * Tokenization and parsing utilities that transform raw text into executable
 * expression trees. Builders, dictionaries, tokenizers, and AST nodes live here,
 * making it the main extension point for users who want to customize how
 * expressions are read and evaluated.
 *
 * <p>Typical usage:</p>
 * <pre>{@code
 * Builder<Double> builder = new DoubleBuilder();
 * Expression<Double> expr = builder.build("3 * (a + b)");
 * Double result = expr.evaluate(Map.of("a", 2d, "b", 4d));
 * }</pre>
 *
 * The package favors immutability at the expression level while keeping
 * dictionaries mutable so plugins can register domain-specific constructs at
 * runtime.
 */
package me.croabeast.expr4j.expression;
