/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.dragonboatrace.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import com.dragonboatrace.game.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class BoatTest {

    @Test
    public void boatInstantiationTest(){
        PlayerBoat testingBoat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 100f));
        // Boat Type Test
        Assert.assertEquals(testingBoat.getBoatType(), BoatType.TESTING);
        // Pos Test
        Assert.assertEquals(testingBoat.getPos(), new Vector2(0,0));
        // Lane Bounds Test
        Assert.assertEquals(testingBoat.getLaneBounds().toString(), new Tuple<Float, Float>(0f, 100f).toString());
        // Current Health Test
        Assert.assertEquals(testingBoat.getHealth(), BoatType.TESTING.getMaxHealth(), 0.01);
        // Current Stamina Test
        Assert.assertEquals(testingBoat.getStamina(), 1000, 0.01);
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
