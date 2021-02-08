package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.entities.*;
import com.dragonboatrace.game.screens.GameScreen;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(GdxTestRunner.class)
public class SaveLoadTest {

    @Test
    public void _0_saveBoatTest() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        String output = boat.save();
        String knownCorrect = "{type:TESTING, health:300.000000, stamina:1000.000000, distance:0.000000, totalTime:0, " +
                "laneBounds:{x:0.000000, y:500.000000}, inGamePos:{x:0.000000, y:0.000000}, " +
                "pos:{x:0.000000, y:0.000000}, vel:{x:0.000000, y:0.000000}, " +
                "startPos:{x:0.000000, y:0.000000}}";
        Assert.assertEquals(knownCorrect, output);
        Assert.assertTrue(GameScreen.saveJSONString(output, Gdx.files.local("TestingSaves/boatSaveTest.json")));
        System.out.println("Done Save Boat Test");
    }

    @Test
    public void _0_saveObstacleTest() {
        Obstacle obstacle = new Obstacle(ObstacleType.TESTING, new Vector2(), new Vector2());
        String output = obstacle.save();
        String knownCorrect = "{type:TESTING, pos:{x:0.000000, y:0.000000}, vel:{x:0.000000, y:0.000000}, " +
                "constantVel:{x:0.000000, y:0.000000}}";
        Assert.assertEquals(knownCorrect, output);
        Assert.assertTrue(GameScreen.saveJSONString(output, Gdx.files.local("TestingSaves/obstacleSaveTest.json")));
        System.out.println("Done Save Obstacle Test");
    }

    @Test
    public void _0_savePowerUpTest() {
        PowerUp power = new PowerUp(PowerUpType.TESTING, new Vector2(), new Vector2());
        String output = power.save();
        String knownCorrect = "{type:TESTING, pos:{x:0.000000, y:0.000000}, vel:{x:0.000000, y:0.000000}, " +
                "constantVel:{x:0.000000, y:0.000000}}";
        Assert.assertEquals(knownCorrect, output);
        Assert.assertTrue(GameScreen.saveJSONString(output, Gdx.files.local("TestingSaves/powerUpSaveTest.json")));
        System.out.println("Done Save Power Up Test");
    }

    @Test
    public void _0_saveLaneTest() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        Lane lane = new Lane(boat, boat);
        lane.updateRound(1, 0.5f);
        String output = lane.save();
        String knownCorrect = "{boat:{type:TESTING, health:300.000000, stamina:1000.000000, distance:0.000000, totalTime:0, " +
                "laneBounds:{x:0.000000, y:500.000000}, inGamePos:{x:0.000000, y:0.000000}, " +
                "pos:{x:0.000000, y:0.000000}, vel:{x:0.000000, y:0.000000}, " +
                "startPos:{x:0.000000, y:0.000000}}, obstacles:[], powerup:null, isPlayer:1}";
        Assert.assertEquals(knownCorrect, output);
        Assert.assertTrue(GameScreen.saveJSONString(output, Gdx.files.local("TestingSaves/laneSaveTest.json")));
        System.out.println("Done Save Lane Test");
    }

    @Test
    public void _1_loadBoatTest() {
        PlayerBoat boatKnown = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        FileHandle file = Gdx.files.local("TestingSaves/boatSaveTest.json");
        JsonValue jsonString = new JsonReader().parse(file);
        PlayerBoat boatOutput = new PlayerBoat(jsonString);
        Assert.assertEquals(boatKnown, boatOutput);
    }

    @Test
    public void _1_loadObstacleTest() {
        Obstacle obstacleKnown = new Obstacle(ObstacleType.TESTING, new Vector2(), new Vector2());
        FileHandle file = Gdx.files.local("TestingSaves/obstacleSaveTest.json");
        JsonValue jsonString = new JsonReader().parse(file);
        Obstacle obstacleOutput = new Obstacle(jsonString);
        Assert.assertEquals(obstacleKnown, obstacleOutput);
    }

    @Test
    public void _1_loadPowerUpTest() {
        PowerUp powerKnown = new PowerUp(PowerUpType.TESTING, new Vector2(), new Vector2());
        FileHandle file = Gdx.files.local("TestingSaves/powerUpSaveTest.json");
        JsonValue jsonString = new JsonReader().parse(file);
        PowerUp powerOutput = new PowerUp(jsonString);
        Assert.assertEquals(powerKnown, powerOutput);
    }

    @Test
    public void _1_loadLaneTest() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        Lane laneKnown = new Lane(boat, boat);
        laneKnown.updateRound(1, 0.5f);
        FileHandle file = Gdx.files.local("TestingSaves/laneSaveTest.json");
        JsonValue jsonString = new JsonReader().parse(file);
        Lane laneOutput = new Lane(jsonString);
        laneOutput.setPb(boat);
        Assert.assertEquals(laneKnown, laneOutput);
    }

}
