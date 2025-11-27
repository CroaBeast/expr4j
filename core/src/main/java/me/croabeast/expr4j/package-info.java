/**
 * Core facade for the expr4j engine. This package contains shared types and
 * utilities that are reused by every numeric implementation module. The
 * {@code Operations} helpers offer pre-built arithmetic factories, while the
 * base {@code Builder} abstraction (in the {@code expression} package) wires up
 * dictionaries and codecs used by parsers.
 *
 * <p>Use these types when integrating expr4j into your own domain: register new
 * operators, functions, or constants in a dictionary and pass it to a builder to
 * produce reusable {@link me.croabeast.expr4j.expression.Expression}
 * instances.</p>
 */
package me.croabeast.expr4j;
