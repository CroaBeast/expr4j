/**
 * Token types that represent the building blocks of an expression. Operators,
 * functions, operands, variables, and separators all implement the
 * {@link me.croabeast.expr4j.token.Token} interface so they can be arranged into
 * trees and evaluated uniformly.
 *
 * <p>Custom tokens generally implement {@link me.croabeast.expr4j.token.Operation}
 * to supply evaluation logic. The provided implementations cover most common
 * arithmetic scenarios, but you can extend them to support domain-specific
 * operators or functions.</p>
 */
package me.croabeast.expr4j.token;
