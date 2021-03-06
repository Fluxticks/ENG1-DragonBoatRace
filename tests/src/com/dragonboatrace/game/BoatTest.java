package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.BoatType;
import com.dragonboatrace.game.entities.Obstacle;
import com.dragonboatrace.game.entities.ObstacleType;
import com.dragonboatrace.game.entities.PlayerBoat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class BoatTest {

    @Test
    public void boatInstantiationTest() {
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        // Boat Type Test
        Assert.assertEquals(testingBoat.getType(), BoatType.TESTING);
        // Pos Test
        Assert.assertEquals(testingBoat.getPos(), new Vector2(0, 0));
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
    public void getFinishTimeStringTest() {
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        testingBoat.setFinishTime(100000);
        Assert.assertEquals(testingBoat.getFinishTimeString(), "1 Minutes and 40 Seconds");
    }

    @Test
    public void getTotalTimeStringTest() {
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        testingBoat.setTotalTime(100000);
        Assert.assertEquals(testingBoat.getTotalTimeString(), "1 Minutes and 40 Seconds");
    }

    @Test
    public void checkCollisionTest(){
        //Check the fields when the collision happens
        checkCollisionTestTrue();
        //Check the fields when there is no collision
        checkCollisionTestFalse();
    }

    private void checkCollisionTestTrue() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
        Obstacle obstacle = new Obstacle(ObstacleType.ROCK, new Vector2(), new Vector2());

        float healthBefore = boat.getCurrentHealth();
        float staminaBefore = boat.getCurrentStamina();
        float velYBefore = boat.getMaxSpeed();

        boat.checkForCollision(obstacle);

        float healthAfter = boat.getCurrentHealth();
        float staminaAfter = boat.getCurrentStamina();
        float velYAfter = boat.getMaxSpeed();

        Assert.assertTrue(boat.checkCollision(obstacle));
        Assert.assertTrue(healthAfter < healthBefore);
        Assert.assertTrue(staminaAfter < staminaBefore);
        Assert.assertTrue(velYAfter < velYBefore);
    }

    private void checkCollisionTestFalse() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(100, 100), new Tuple<Float, Float>(0f, 0f));
        Obstacle obstacle = new Obstacle(ObstacleType.TESTING, new Vector2(10, 10), new Vector2());

        float healthBefore = boat.getCurrentHealth();
        float staminaBefore = boat.getCurrentStamina();
        float velYBefore = boat.getMaxSpeed();

        boat.checkForCollision(obstacle);

        float healthAfter = boat.getCurrentHealth();
        float staminaAfter = boat.getCurrentStamina();
        float velYAfter = boat.getMaxSpeed();

        Assert.assertFalse(boat.checkCollision(obstacle));
        Assert.assertEquals(healthAfter, healthBefore, 0.0);
        Assert.assertEquals(staminaAfter, staminaBefore, 0.0);
        Assert.assertEquals(velYAfter, velYBefore, 0.0);
    }

    @Test
    public void checkFinishedTest(){
        // Check when the boat is finished
        checkFinishedTestTrue();
        // Check when the boat is not finsihed
        checkFinishedTestFalse();
    }

    private void checkFinishedTestFalse() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
        int finishLine = 1000;
        Assert.assertFalse(boat.checkFinished(finishLine, 0));
    }

    private void checkFinishedTestTrue() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 0f));
        int finishLine = 1000;
        boat.setDistanceTravelled((float) finishLine);
        Assert.assertTrue(boat.checkFinished(finishLine, 0));
    }
}
