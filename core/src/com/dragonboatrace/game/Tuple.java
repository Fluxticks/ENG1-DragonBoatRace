package com.dragonboatrace.game;

/**
 * A generic type tuple used to hold a pair of values.
 *
 * @param <A> Generic type that defines the type of the first value.
 * @param <B> Generic type that defines the type of the second value.
 * @author Jacob Turner
 */
public class Tuple<A, B> {
    /**
     * The first value in the tuple. Of type A
     */
    public A a;
    /**
     * The second value in the tuple. Of type B
     */
    public B b;

    /**
     * Creates a tuple with values a and b, of types A and B respectively.
     *
     * @param a The first value in the tuple.
     * @param b The second value in the tuple.
     */
    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    // THIS IS NEW
    /**
     * Assert if a given Object is identical to a given tuple.
     *
     * @param obj The object being compared to the tuple.
     * @return A boolean of if the given object is identical to the tuple.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Tuple.class) {
            Tuple objT = (Tuple) obj;
            return this.a.equals(objT.a) && this.b.equals(objT.b);
        } else {
            return false;
        }
    }

    /**
     * Convert the tuple to a string.
     *
     * @return A string representing the tuple.
     */
    public String toString() {
        return String.format("Tuple<%s, %s>", this.a.toString(), this.b.toString());
    }
}