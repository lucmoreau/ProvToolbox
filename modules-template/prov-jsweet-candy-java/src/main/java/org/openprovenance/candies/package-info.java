/**
 * Root package for candy classes that must map onto their original global
 * namespaces. JSweet erases the package prefix up to and including a package
 * annotated with {@code @jsweet.lang.Root} when generating TypeScript names:
 * a class in {@code org.openprovenance.candies.java.util.concurrent.atomic}
 * is emitted in the TypeScript namespace {@code java.util.concurrent.atomic},
 * where references from transpiled application code resolve to it.
 *
 * (Classes cannot be placed in a real {@code java.*} package: javac prohibits
 * it, and the candy's Java sources are also compiled normally.)
 */
@jsweet.lang.Root
package org.openprovenance.candies;
