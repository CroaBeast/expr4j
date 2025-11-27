# expr4j (CroaBeast fork)

This project is a heavily tuned fork of the original [expr4j/expr4j](https://github.com/expr4j/expr4j) expression engine. The upstream repository went offline, so this fork keeps the project accessible while adding better packaging, docs, and ready-to-use shaded artifacts. It keeps the generic, token-based parser but streamlines module publishing for plugin ecosystems and JVM apps alike.

## Modules & packaging

All published artifacts live under the `me.croabeast.expr4j` group. The repository provides both regular and shaded JARs:

| Module | Coordinates | Notes |
| --- | --- | --- |
| Core | `me.croabeast.expr4j:core` | Required by every implementation. |
| Double | `me.croabeast.expr4j:double` | Depends on `core`. |
| Big Decimal | `me.croabeast.expr4j:big-decimal` | Depends on `core` and `ch.obermuhlner:big-math:2.3.2`. |
| Complex | `me.croabeast.expr4j:complex` | Depends on `core` and `org.apache.commons:commons-numbers-complex:1.2`. |
| Shaded variants | `me.croabeast.expr4j:<module>-shaded` | Bundle `core` and the module’s own dependencies for drop-in use. |

Big-decimal and complex implementations require their external libraries when using the non-shaded artifacts; you can declare them as dependencies or add them as `libraries` in your `plugin.yml`. The shaded artifacts already include everything, so you can reference them directly.

### Non-shaded dependency examples

Add the repository and declare the dependencies you need. Non-shaded `big-decimal` and `complex` require their math libraries:

```kotlin
repositories {
    maven("https://croabeast.github.io/repo")
}

dependencies {
    implementation("me.croabeast.expr4j:core:<version>")
    implementation("me.croabeast.expr4j:double:<version>")
    implementation("me.croabeast.expr4j:big-decimal:<version>")
    implementation("me.croabeast.expr4j:complex:<version>")

    // External requirements for non-shaded modules
    implementation("ch.obermuhlner:big-math:2.3.2")
    implementation("org.apache.commons:commons-numbers-complex:1.2")
}
```

If you only need one implementation, keep `core` plus your chosen module. Shaded variants (`<module>-shaded`) already bundle `core` and the external math libraries.

## Repository

Artifacts are published to [https://croabeast.github.io/repo/](https://croabeast.github.io/repo/) with the path `me/croabeast/expr4j`. Add the repository and pick the modules you need.

### Gradle (Kotlin DSL)
```kotlin
repositories {
    maven("https://croabeast.github.io/repo")
}

dependencies {
    implementation("me.croabeast.expr4j:core:<version>")
    implementation("me.croabeast.expr4j:double:<version>")
    // Optional extras:
    // implementation("me.croabeast.expr4j:big-decimal:<version>")
    // implementation("me.croabeast.expr4j:complex:<version>")
}
```

### Maven
```xml
<repositories>
  <repository>
    <id>croabeast-repo</id>
    <url>https://croabeast.github.io/repo</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>me.croabeast.expr4j</groupId>
    <artifactId>core</artifactId>
    <version><!-- your version --></version>
  </dependency>
  <dependency>
    <groupId>me.croabeast.expr4j</groupId>
    <artifactId>double</artifactId>
    <version><!-- your version --></version>
  </dependency>
</dependencies>
```

If you prefer shaded binaries, replace `double` (or `big-decimal`, `complex`) with `<module>-shaded` and omit the extra dependencies.

## Quick start

Create a builder for your numeric type, register variables, and evaluate:

```java
DoubleBuilder builder = new DoubleBuilder();
Expression<Double> expression = builder.build("2 * cos(x) + y/4");

Map<String, Double> variables = new HashMap<>();
variables.put("x", Math.PI / 2);
variables.put("y", 8.0);

Double result = expression.evaluate(variables); // -> 3.0
```

Complex and BigDecimal builders expose the same API, so swapping types is effortless when you need more precision or imaginary components.

## Implementation notes

* Parsing relies on Dijkstra’s shunting-yard algorithm to produce an AST and supports implicit multiplication (e.g., `2x` or `(a+b)(a-b)`).
* Operators and functions are lazy-evaluated, making it easy to plug in custom logic without recomputing unchanged branches.
* Builders are generic: extend `Builder<T>` with a custom `Codec` to support new numeric domains.

## Shaded usage in plugins

For Bukkit/Spigot or similar platforms, include the shaded artifact in your `plugin.yml` `libraries` section or bundle it directly. The shaded JAR already contains `core` and any third-party math libraries, so no extra packaging is required.

```yaml
name: Expr4jDemo
main: me.example.Expr4jDemo
version: 1.0.0
api-version: "1.20"
libraries:
  - me.croabeast.expr4j:complex-shaded:<version>
```

If you want to load non-shaded modules instead, add both `core` and the implementation, plus the external libraries in the same `libraries` block.

## Why this fork?

* Upstream hosting disappeared, so the fork keeps expr4j available with stable coordinates.
* Unified publishing for every module under a single repository path (`me/croabeast/expr4j`).
* Shaded variants tailored for plugin ecosystems.
* Expanded documentation, richer Javadoc, and examples that match the maintained repository.

