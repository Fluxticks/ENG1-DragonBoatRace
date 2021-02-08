package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Obstacle;
import com.dragonboatrace.game.entities.ObstacleType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class ObstacleTest {

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
    public void obstacleMovementCharacteristicTest() {
        for (ObstacleType type : ObstacleType.values()) {
            Obstacle obstacle = new Obstacle(type, new Vector2(), new Vector2());
            MovementCharacteristics expectedMover = type.getMover();
            MovementCharacteristics actualMover = obstacle.getMover();
            Assert.assertEquals(expectedMover, actualMover);
        }
    }
}
