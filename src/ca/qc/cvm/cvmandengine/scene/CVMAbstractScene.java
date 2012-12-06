package ca.qc.cvm.cvmandengine.scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import ca.qc.cvm.cvmandengine.CVMTextureManager;
import ca.qc.cvm.cvmandengine.entity.CVMSprite;
import ca.qc.cvm.cvmandengine.entity.CVMText;
import ca.qc.cvm.cvmandengine.entity.ManagedUpdateListener;
import ca.qc.cvm.cvmandengine.entity.TouchAreaListener;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

import android.content.Context;
import android.graphics.Typeface;
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
    
    private List<CVMSprite> spriteList;
    private List<CVMText> textList;
	
	public CVMAbstractScene(String backgroundPath, int id) {
		this.backgroundPath = backgroundPath;
		this.id = id;
		
		spriteList = new ArrayList<CVMSprite>();
		textList = new ArrayList<CVMText>();
	}
	
	public void addSprite(CVMSprite sprite) {
		spriteList.add(sprite);
	}
	
	public void addText(CVMText sprite) {
		textList.add(sprite);
	}

	public void load(TextureManager manager, Context context, Engine engine, CVMTextureManager cvmTextureManager) throws IllegalStateException, IOException {
		// Load background texture
		BitmapTextureAtlas mBackgroundTexture = new BitmapTextureAtlas(manager, CVMGameActivity.CAMERA_WIDTH, CVMGameActivity.CAMERA_HEIGHT, TextureOptions.DEFAULT);
		backgroundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, context, backgroundPath, 0, 0);

	    manager.loadTexture(mBackgroundTexture);

		// Define music
	    if (musicPath != null) {
	    	music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, musicPath);
	    	music.setLooping(musicLoop);
	    }

	    // Load textures for each sprite in scene
	    for (CVMSprite sprite : spriteList) {		    
	    	sprite.setTextureRegion(cvmTextureManager.getTextureById(sprite.getTextureId()));
	    }
	    
	    for (CVMText text : textList) {
	    	//Font font = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), text.getSize());
	    	BitmapTextureAtlas mFontTexture = new BitmapTextureAtlas(engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
	    	Font font = new Font(engine.getFontManager(), mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.WHITE);
	    	font.load();
	    	text.setFont(font);
	    }
	}
	
	public void populate(VertexBufferObjectManager vertexBufferObjectManager, final CVMGameActivity cvmGameActivity) {
		this.detachChildren();

        SpriteBackground bg = new SpriteBackground(new Sprite(0, 0, backgroundTexture, vertexBufferObjectManager));
        this.setBackground(bg);
        
        // Add sprites to scene and register touch events
	    for (final CVMSprite sprite : spriteList) {
			Sprite tmp = new Sprite(sprite.getPosX(), sprite.getPosY(), sprite.getTextureRegion(), vertexBufferObjectManager) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (sprite instanceof TouchAreaListener) {
						((TouchAreaListener)sprite).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY, cvmGameActivity, CVMAbstractScene.this);
					}
					return true;
				};
				
				@Override
			    protected void onManagedUpdate(float pSecondsElapsed) {
					if (sprite instanceof ManagedUpdateListener) {
						((ManagedUpdateListener)sprite).onManagedUpdate(pSecondsElapsed);
					}
					
					super.onManagedUpdate(pSecondsElapsed);
			    }
			};
			
			sprite.setSprite(tmp);
			this.attachChild(tmp);
			
			if (sprite instanceof TouchAreaListener) {
				this.registerTouchArea(tmp);
			}
	    }

	    for (CVMText text : textList) {
	    	Text txt = new Text(text.getPosX(), text.getPosY(), text.getFont(), text.getDisplayText(), new TextOptions(HorizontalAlign.LEFT), cvmGameActivity.getVertexBufferObjectManager());
	    	txt.setColor(text.getColor());
	    	text.setText(txt);
	    	
	    	this.attachChild(txt);
	    }
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
