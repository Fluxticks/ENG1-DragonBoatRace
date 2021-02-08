package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.BoatType;
import com.dragonboatrace.game.entities.CPUBoat;
import com.dragonboatrace.game.entities.PlayerBoat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class LaneTest {

    @Test
    public void updateMaxObstaclesTestSingle() {
        Lane lane = new Lane(null, null);
        int initialRound = 1;
        float multiplier = 0.5f;
        int expectedOutcome = (int) ((3 + initialRound) * multiplier);
        Assert.assertEquals(expectedOutcome, lane.updateRound(initialRound, multiplier));
    }

    @Test
    public void updateMaxObstacleTestMultiple() {
        Lane lane = new Lane(null, null);
        float multiplier = 0.5f;
        int offset = 3;
        for (int i = 1; i < 10; i++) {
            int expectedOutcome = (int) ((offset + i) * multiplier);
            Assert.assertEquals(expectedOutcome, lane.updateRound(i, multiplier));
        }
    }

    @Test
    public void isPlayerLaneTestFalse() {
        Lane lane = new Lane(new CPUBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f)), null);
        Assert.assertFalse(lane.isPlayerLane);
    }

    @Test
    public void isPlayerLaneTestTrue() {
        Lane lane = new Lane(new PlayerBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f)), null);
        Assert.assertTrue(lane.isPlayerLane);
    }
}
