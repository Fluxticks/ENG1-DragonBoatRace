package com.dragonboatrace.game;

import org.junit.Assert;
import org.junit.Test;

public class TupleTest {

    @Test
    public void toStringTest() {
        Tuple<Integer, Integer> testingTuple = new Tuple<Integer, Integer>(10, 20);
        Assert.assertEquals(testingTuple.toString(), "Tuple<10, 20>");
    }
}
