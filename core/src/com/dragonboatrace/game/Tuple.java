package com.dragonboatrace.game;

public class Tuple<A, B>{
    public A a;
    public B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == Tuple.class){
            Tuple objT = (Tuple)obj;
            return this.a.equals(objT.a) && this.b.equals(objT.b);
        }else{
            return false;
        }
    }

    public String toString() {
        return String.format("Tuple<%s, %s>", this.a.toString(), this.b.toString());
    }
}