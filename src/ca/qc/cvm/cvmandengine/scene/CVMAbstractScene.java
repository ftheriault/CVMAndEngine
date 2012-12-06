package ca.qc.cvm.cvmandengine.scene;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

import android.content.Context;
import android.util.Log;

public abstract class CVMAbstractScene extends Scene {
	public enum State {Started, Stopped}
	protected State state;
	
	private int id;
	
	private Music music;
	private String musicPath;
	private boolean musicLoop;

	private String backgroundPath;
    public static TextureRegion backgroundTexture;
	
	public CVMAbstractScene(String backgroundPath, int id) {
		this.backgroundPath = backgroundPath;
		this.id = id;
	}

	public void load(TextureManager manager, Context context, Engine engine) {
		try {
			// Load background texture
			BitmapTextureAtlas mBackgroundTexture = new BitmapTextureAtlas(manager, CVMGameActivity.CAMERA_WIDTH, CVMGameActivity.CAMERA_HEIGHT, TextureOptions.DEFAULT);
			backgroundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, context, backgroundPath, 0, 0);

		    manager.loadTexture(mBackgroundTexture);

			// Define music
		    if (musicPath != null) {
		    	music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, musicPath);
		    	music.setLooping(musicLoop);
		    }
	    	
		} catch (Exception e) {
			Log.e("CVMAndEngine", "CVMAbstractScene", e);
		}
	}
	
	public void populate(VertexBufferObjectManager vertexBufferObjectManager, CVMGameActivity cvmGameActivity) {
		this.detachChildren();

        SpriteBackground bg = new SpriteBackground(new Sprite(0, 0, backgroundTexture, vertexBufferObjectManager));
        this.setBackground(bg);
	}
	
	public void start() {
		state = State.Started; 
		
		if (music != null) {
			music.play();
		}
		
		Log.i("CVMAndEngine", "Scene " + id +" started");
	}
	
	public void stop() {
		state = State.Stopped;
		
		if (music != null) {
			music.pause();
		}
		
		Log.i("CVMAndEngine", "Scene " + id + " stopped");
	}
	
	protected void setBackgroundPath(String backgroundPath) {
		this.backgroundPath = backgroundPath;
	}
	
	protected void setMusicPath(String path, boolean loop) {
		this.musicPath = path;
		this.musicLoop = loop;
	}
	
	public int getId() {
		return id;
	}
}
