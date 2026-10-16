package org.openprovenance.candies.java.util.concurrent.atomic;

/**
 * JSweet candy implementation of {@code java.util.concurrent.atomic.AtomicInteger}.
 *
 * JavaScript is single-threaded, so a plain field provides the required
 * semantics; only the api surface of the JDK class is reproduced (the subset
 * commonly used by generated template code, plus the usual accessors).
 */
public class AtomicInteger {

    private int value;

    public AtomicInteger() {
        this.value = 0;
    }

    public AtomicInteger(int initialValue) {
        this.value = initialValue;
    }

    public int get() {
        return value;
    }

    public void set(int newValue) {
        this.value = newValue;
    }

    public int getAndSet(int newValue) {
        int previous = value;
        this.value = newValue;
        return previous;
    }

    public int getAndIncrement() {
        return value++;
    }

    public int getAndDecrement() {
        return value--;
    }

    public int incrementAndGet() {
        return ++value;
    }

    public int decrementAndGet() {
        return --value;
    }

    public int getAndAdd(int delta) {
        int previous = value;
        this.value = value + delta;
        return previous;
    }

    public int addAndGet(int delta) {
        this.value = value + delta;
        return value;
    }

    public boolean compareAndSet(int expectedValue, int newValue) {
        if (value == expectedValue) {
            this.value = newValue;
            return true;
        }
        return false;
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return value;
    }

    public float floatValue() {
        return value;
    }

    public double doubleValue() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
