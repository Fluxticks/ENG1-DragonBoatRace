package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Entity;

/**
 * Represents the background of the game.
 *
 * @author Jacob Turner
 */
public class Background extends Entity {

    /**
     * An array of all the textures to use as the backgrounds.
     */
    protected Texture[] allTextures;
    /**
     * The current texture being used as the background.
     */
    protected Texture img;

    /**
     * Creates a new background at a position.
     *
     * @param pos The position of the background.
     */
    public Background(Vector2 pos) {
        super(pos, new Vector2(0, 0), 0);
        loadTexture();
        updateTexture();
    }

    /**
     * Add the background to the given Spritebatch and render it.
     *
     * @param batch  The Spritebatch to be added to.
     * @param relPos The position to be rendered relative to.
     */
    public void render(SpriteBatch batch, Vector2 relPos) {

        //if the background tile has gone off the screen move it to the top and give it a new texture
        if (relPos.y - this.pos.y > this.img.getHeight()) {
            this.pos.y += Gdx.graphics.getHeight() + this.img.getHeight();
            updateTexture();
        }

        batch.begin();
        batch.draw(
                this.img,
                (0),
                (this.pos.y - relPos.y),
                Gdx.graphics.getWidth(),
                this.size.y
        );
        batch.end();
    }

    /**
     * Change the background texture to another random one.
     */
    private void updateTexture() {
        this.img = this.allTextures[(int) (Math.random() * allTextures.length)];
        this.size = new Vector2(this.img.getWidth(), this.img.getHeight());
    }

    /**
     * Load all the textures.
     */
    public void loadTexture() {
        // Load a list of file names from a text file
        FileHandle backgroundsDirectory = Gdx.files.local("Backgrounds/backgrounds.txt");
        String[] files = backgroundsDirectory.readString().split(",");
        this.allTextures = new Texture[files.length];
        // Load those file names as files
        for (int i = 0; i < files.length; i++) {
            FileHandle file = Gdx.files.internal("Backgrounds/" + files[i] + ".jpg");
            this.allTextures[i] = new Texture(file);
        }
    }

    /**
     * Dispose all the textures of the backgrounds.
     */
    public void dispose() {
        for (Texture t : this.allTextures) {
            t.dispose();
        }
    }

    /**
     * Move the background. Not used. Empty body.
     *
     * @param deltaTime The time since the previous frame.
     */
    public void move(float deltaTime) {
    }

}