package com.dragonboatrace.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class BoatTest {

    @Test
    public void boatInstantiationTest(){
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        // Boat Type Test
        Assert.assertEquals(testingBoat.getType(), BoatType.TESTING);
        // Pos Test
        Assert.assertEquals(testingBoat.getPos(), new Vector2(0,0));
        // Lane Bounds Test
        Assert.assertEquals(testingBoat.getLaneBounds().toString(), new Tuple<Float, Float>(0f, 100f).toString());
        // Current Health Test
        Assert.assertEquals(testingBoat.getCurrentHealth(), BoatType.TESTING.getMaxHealth(), 0.01);
        // Current Stamina Test
        Assert.assertEquals(testingBoat.getCurrentStamina(), 1000, 0.01);
        // Max Speed Test
        Assert.assertEquals(testingBoat.getMaxSpeed(), BoatType.TESTING.getSpeed(), 0.01);
    }

    @Test
    public void getFinishTimeStringTest(){
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        testingBoat.setFinishTime(100000);
        Assert.assertEquals(testingBoat.getFinishTimeString(), "1 Minutes and 40 Seconds");
    }

    @Test
    public void getTotalTimeStringTest(){
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        testingBoat.setTotalTime(100000);
        Assert.assertEquals(testingBoat.getTotalTimeString(), "1 Minutes and 40 Seconds");
    }

    @Test
    public void checkCollisionTestTrue(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f,0f));
        Obstacle obstacle = new Obstacle(ObstacleType.TESTING, new Vector2(), new Vector2());
        Assert.assertTrue(boat.checkCollision(obstacle));
    }

    @Test
    public void checkCollisionTestFalse(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(100,100), new Tuple<Float, Float>(0f,0f));
        Obstacle obstacle = new Obstacle(ObstacleType.TESTING, new Vector2(10,10), new Vector2());
        Assert.assertFalse(boat.checkCollision(obstacle));
        //TODO: Check health and stamina change.
    }

    @Test
    public void checkFinishedTestFalse(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f,0f));
        int finishLine = 1000;
        Assert.assertFalse(boat.checkFinished(finishLine, 0));
    }

    @Test
    public void checkFinishedTestTrue(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0,0), new Tuple<Float, Float>(0f,0f));
        int finishLine = 1000;
        boat.setDistanceTravelled((float)finishLine);
        Assert.assertTrue(boat.checkFinished(finishLine, 0));
    }
}
