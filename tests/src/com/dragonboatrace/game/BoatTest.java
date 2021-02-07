package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.*;
import com.dragonboatrace.game.screens.BoatChoice;
import com.dragonboatrace.game.screens.midRoundScreen;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

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
    public void checkCollisionTestTrue() {
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

    @Test
    public void checkCollisionTestFalse() {
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
    public void checkFinishedTestFalse() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
        int finishLine = 1000;
        Assert.assertFalse(boat.checkFinished(finishLine, 0));
    }

    @Test
    public void checkFinishedTestTrue() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 0f));
        int finishLine = 1000;
        boat.setDistanceTravelled((float) finishLine);
        Assert.assertTrue(boat.checkFinished(finishLine, 0));
    }

    @Test
    public void playerInFirstTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 0f));

        long playerFinish = 100;
        long nextToAdd = 50;

        boat.setTotalTime(playerFinish);
        boat.setFinishTime(playerFinish);

        Lane[] lanes = new Lane[3];
        Lane pLane = new Lane(boat, boat);
        lanes[0] = pLane;
        for(int i = 1; i < lanes.length; i ++){
            CPUBoat cpuBoat = new CPUBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
            cpuBoat.setTotalTime(playerFinish + (long)i*nextToAdd);
            cpuBoat.setFinishTime(playerFinish + (long)i*nextToAdd);
            Lane lane = new Lane(cpuBoat, boat);
            lanes[i] = lane;
        }

        int[] positions = midRoundScreen.getPlayerPositions(lanes, boat);

        Assert.assertEquals(1, positions[0]);
        Assert.assertEquals(1, positions[1]);
    }

    @Test
    public void playerNotInFirstTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 0f));

        long playerFinish = 500;
        long nextToAdd = 25;

        boat.setTotalTime(playerFinish);
        boat.setFinishTime(playerFinish);

        Lane[] lanes = new Lane[3];
        Lane pLane = new Lane(boat, boat);
        lanes[0] = pLane;
        for(int i = 1; i < lanes.length; i ++){
            CPUBoat cpuBoat = new CPUBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
            cpuBoat.setTotalTime(playerFinish - (long)i*nextToAdd);
            cpuBoat.setFinishTime(playerFinish - (long)i*nextToAdd);
            Lane lane = new Lane(cpuBoat, boat);
            lanes[i] = lane;
        }

        int[] positions = midRoundScreen.getPlayerPositions(lanes, boat);

        Assert.assertEquals(lanes.length, positions[0]);
        Assert.assertEquals(lanes.length, positions[1]);
    }

    @Test
    public void checkValidCPUBoatTypes(){
        for(BoatType type : BoatType.values()){
            ArrayList<BoatType> availableTypes = BoatChoice.availableBoats(type);
            Assert.assertFalse(availableTypes.contains(type));
        }
        // Testing should not be an available choice of boat for the cpu
        ArrayList<BoatType> availableTypes = BoatChoice.availableBoats(null);
        Assert.assertFalse(availableTypes.contains(BoatType.TESTING));
    }
}
