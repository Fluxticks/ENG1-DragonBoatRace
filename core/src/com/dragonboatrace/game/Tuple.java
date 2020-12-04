package com.dragonboatrace.game;

public class Tuple<A, B> {
    public A a;
    public B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public String toString() {
        return String.format("Tuple<%s, %s>", this.a.toString(), this.b.toString());
    }
}