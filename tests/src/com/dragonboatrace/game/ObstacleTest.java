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

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Obstacle;
import com.dragonboatrace.game.entities.ObstacleType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import com.dragonboatrace.game.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class ObstacleTest {

    Obstacle testingObstacle = new Obstacle(ObstacleType.TESTING, new Vector2(0, 0), new Vector2(0, 0));

    @Test
    public void obstacleInstantiationTest() {
        Obstacle testingObstacle = new Obstacle(ObstacleType.TESTING, new Vector2(0, 0), new Vector2(0, 0));

        // Obstacle Type Test
        Assert.assertEquals(testingObstacle.getType(), ObstacleType.TESTING);
        // Pos Test
        Assert.assertEquals(testingObstacle.getPos(), new Vector2(0, 0));
        // Vel Test
        Assert.assertEquals(testingObstacle.getVel(), new Vector2(0, 0));
        // Size Test
        Assert.assertEquals(testingObstacle.getSize(), testingObstacle.getType().getSize());
        // Movement Characteristics
        Assert.assertEquals(testingObstacle.getType().getMover(), MovementCharacteristics.STATIC);
    }

    @Test
    public void obstacleMovementCharacteristicTest(){
        for(ObstacleType type : ObstacleType.values()){
            Obstacle obstacle = new Obstacle(type, new Vector2(), new Vector2());
            MovementCharacteristics expectedMover = type.getMover();
            MovementCharacteristics actualMover = obstacle.getMover();
            Assert.assertEquals(expectedMover, actualMover);
        }
    }
}
