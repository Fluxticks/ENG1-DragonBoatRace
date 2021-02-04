package com.dragonboatrace.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import com.dragonboatrace.game.GdxTestRunner;

// @RunWith(GdxTestRunner.class)   Only add if Gdx needed!
public class TupleTest {

    @Test
    public void toStringTest() {
        Tuple<Integer, Integer> testingTuple = new Tuple<Integer, Integer>(10,20);
        Assert.assertEquals(testingTuple.toString(), "Tuple<10, 20>");
    }
}
