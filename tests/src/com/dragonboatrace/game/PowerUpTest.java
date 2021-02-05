package com.dragonboatrace.game;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.BoatType;
import com.dragonboatrace.game.entities.PlayerBoat;
import com.dragonboatrace.game.entities.PowerUp;
import com.dragonboatrace.game.entities.PowerUpType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import com.dragonboatrace.game.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class PowerUpTest {

    @Test
    public void powerUpInitialisationTest() {
        PowerUp testingPowerup = new PowerUp(PowerUpType.TESTING, new Vector2(0, 0), new Vector2(0, 0));

        //Type Test
        Assert.assertEquals(testingPowerup.getType(), PowerUpType.TESTING);

        // Velocity test
        Assert.assertEquals(testingPowerup.getVel(), new Vector2(0, 0));

        // Constant velocity test event after vel changed
        Assert.assertEquals(testingPowerup.getConstantVel(), new Vector2(0, 0));
        testingPowerup.setVel(new Vector2(10, 10));
        Assert.assertEquals(testingPowerup.getConstantVel(), new Vector2(0, 0));

    }

    @Test
    public void applySpeedTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp speedPower = new PowerUp(PowerUpType.SPEED, new Vector2(), new Vector2());
        float expectedDelta = speedPower.getType().getEffect();
        float outputDelta = speedPower.applySpeed(boat);
        Assert.assertEquals(expectedDelta, outputDelta, 0.0);
    }

    @Test
    public void applyHealthTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp healthPower = new PowerUp(PowerUpType.HEALTH, new Vector2(), new Vector2());
        float expectedDelta = healthPower.getType().getEffect();
        float outputDelta = healthPower.applySpeed(boat);
        Assert.assertEquals(expectedDelta, outputDelta, 0.0);
    }

    @Test
    public void applyAgilityTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp agilityPower = new PowerUp(PowerUpType.AGILITY, new Vector2(), new Vector2());
        float expectedDelta = agilityPower.getType().getEffect();
        float outputDelta = agilityPower.applySpeed(boat);
        Assert.assertEquals(expectedDelta, outputDelta, 0.0);
    }

    @Test
    public void applyStaminaTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp staminaPower = new PowerUp(PowerUpType.STAMINA, new Vector2(), new Vector2());
        float expectedDelta = staminaPower.getType().getEffect();
        float outputDelta = staminaPower.applySpeed(boat);
        Assert.assertEquals(expectedDelta, outputDelta, 0.0);
    }

    @Test
    public void applyTimerTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp timerPower = new PowerUp(PowerUpType.TIMER, new Vector2(), new Vector2());
        float expectedDelta = timerPower.getType().getEffect();
        float outputDelta = timerPower.applySpeed(boat);
        Assert.assertEquals(expectedDelta, outputDelta, 0.0);
    }

    @Test
    public void applyNoCollideTest(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp noCollidePower = new PowerUp(PowerUpType.NOCOLLIDE, new Vector2(), new Vector2());
        float expectedDelta = noCollidePower.getType().getEffect();
        float outputDelta = noCollidePower.applySpeed(boat);
        Assert.assertEquals(expectedDelta, outputDelta, 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void invalidPowerType(){
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 500f));
        PowerUp invalidPower = new PowerUp(PowerUpType.TESTING, new Vector2(), new Vector2());
        invalidPower.applyEffect(boat);
    }
}
