package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.BoatType;
import com.dragonboatrace.game.entities.CPUBoat;
import com.dragonboatrace.game.entities.PlayerBoat;
import com.dragonboatrace.game.screens.BoatChoice;
import com.dragonboatrace.game.screens.Finale;
import com.dragonboatrace.game.screens.GameScreen;
import com.dragonboatrace.game.screens.midRoundScreen;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(GdxTestRunner.class)
public class GameplayTest {

    @Test
    public void playerInFirstTest() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 0f));

        long playerFinish = 100;
        long nextToAdd = 50;

        boat.setTotalTime(playerFinish);
        boat.setFinishTime(playerFinish);

        Lane[] lanes = new Lane[3];
        Lane pLane = new Lane(boat, boat);
        lanes[0] = pLane;
        for (int i = 1; i < lanes.length; i++) {
            CPUBoat cpuBoat = new CPUBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
            cpuBoat.setTotalTime(playerFinish + (long) i * nextToAdd);
            cpuBoat.setFinishTime(playerFinish + (long) i * nextToAdd);
            Lane lane = new Lane(cpuBoat, boat);
            lanes[i] = lane;
        }

        int[] positions = midRoundScreen.getPlayerPositions(lanes, boat);

        Assert.assertEquals(1, positions[0]);
        Assert.assertEquals(1, positions[1]);
    }

    @Test
    public void playerNotInFirstTest() {
        PlayerBoat boat = new PlayerBoat(BoatType.TESTING, new Vector2(0, 0), new Tuple<Float, Float>(0f, 0f));

        long playerFinish = 500;
        long nextToAdd = 25;

        boat.setTotalTime(playerFinish);
        boat.setFinishTime(playerFinish);

        Lane[] lanes = new Lane[3];
        Lane pLane = new Lane(boat, boat);
        lanes[0] = pLane;
        for (int i = 1; i < lanes.length; i++) {
            CPUBoat cpuBoat = new CPUBoat(BoatType.TESTING, new Vector2(), new Tuple<Float, Float>(0f, 0f));
            cpuBoat.setTotalTime(playerFinish - (long) i * nextToAdd);
            cpuBoat.setFinishTime(playerFinish - (long) i * nextToAdd);
            Lane lane = new Lane(cpuBoat, boat);
            lanes[i] = lane;
        }

        int[] positions = midRoundScreen.getPlayerPositions(lanes, boat);

        Assert.assertEquals(lanes.length, positions[0]);
        Assert.assertEquals(lanes.length, positions[1]);
    }

    @Test
    public void checkValidCPUBoatTypes() {
        for (BoatType type : BoatType.values()) {
            ArrayList<BoatType> availableTypes = BoatChoice.availableBoats(type);
            Assert.assertFalse(availableTypes.contains(type));
        }
        // Testing should not be an available choice of boat for the cpu
        ArrayList<BoatType> availableTypes = BoatChoice.availableBoats(null);
        Assert.assertFalse(availableTypes.contains(BoatType.TESTING));
    }

    @Test
    public void playerReceivesCorrectMedal() {
        String[] medals = {"", "Gold", "Silver", "Bronze", ""};
        for (int i = 0; i < medals.length; i++) {
            Assert.assertEquals(medals[i], Finale.getPlayerMedal(i));
        }
    }

    @Test
    public void updateMaxObstaclesMultipleDifficultyTest(){
        int difficulty = 1;
        float expectedOutcome = 0.5f;
        float actualOutcome = GameScreen.getObstacleMultiplier(difficulty);
        Assert.assertEquals(expectedOutcome, actualOutcome, 0.0);
    }

    @Test
    public void updateMaxObstaclesSingleDifficultyTest(){
        float[] multipliers = {0f, 0.5f, 1f, 2f, 0f};
        for (int i = 0; i < multipliers.length; i++){
            float expectedOutcome = multipliers[i];
            float actualOutcome = GameScreen.getObstacleMultiplier(i);
            Assert.assertEquals(expectedOutcome, actualOutcome, 0.0);
        }
    }

}
