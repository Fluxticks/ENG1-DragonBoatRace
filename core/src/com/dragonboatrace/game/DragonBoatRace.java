package com.dragonboatrace.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dragonboatrace.game.screens.TitleScreen;

import java.util.ArrayList;

public class DragonBoatRace extends Game {

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public ArrayList<ScreenAdapter> toDispose;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.toDispose = new ArrayList<>();
        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
        for (ScreenAdapter s : this.toDispose) {
            s.dispose();
        }
    }

}
