package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Entity;

/**
 * Represents a lane marker that sits between each lane.
 *
 * @author Jacob Turner
 */
public class LaneMarker extends Entity {
    /**
     * The texture of the lane marker.
     */
    protected Texture img;

    /**
     * Creates a lane marker at a given position.
     *
     * @param pos The position of the lane marker.
     */
    public LaneMarker(Vector2 pos) {
        this(pos, new Texture("Obstacles/line.png"));
    }

    /**
     * Creates a lane marker at a position with a specific texture.
     *
     * @param pos The position of the lane marker.
     * @param img The texture to set the lane marker to.
     */
    public LaneMarker(Vector2 pos, Texture img) {
        super(pos, new Vector2(img.getWidth(), img.getHeight()), 0);
        this.img = img;
    }

    /**
     * Load the texture. Not used. Empty body.
     */
    @Override
    public void loadTexture() {

    }

    /**
     * Add the lane marker to a spritebatch and render it.
     *
     * @param batch  The spritebatch to be added to.
     * @param relPos The position to be rendered relative to.
     */
    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        for (int i = 0; i < ((3 * Gdx.graphics.getHeight()) / this.img.getHeight()); i++) {
            batch.draw(this.img,
                    (this.pos.x - this.size.x / 2),
                    (this.pos.y - relPos.y) % this.img.getHeight() + (i * this.img.getHeight()));
        }
        batch.end();
    }

    /**
     * Dispose of the texture for the lane marker.
     */
    public void dispose() {
        this.img.dispose();
    }

    /**
     * Move the lane marker. Not used. Empty body.
     *
     * @param deltaTime The time since the previous frame.
     */
    public void move(float deltaTime) {
    }

}