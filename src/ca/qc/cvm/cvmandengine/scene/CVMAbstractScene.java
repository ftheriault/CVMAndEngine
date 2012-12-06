package ca.qc.cvm.cvmandengine.scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
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
	
	private CVMGameActivity gameActivity;
	private CVMTextureManager cvmTextureManager;
	protected VertexBufferObjectManager vertexBufferObjectManager; 

	private String backgroundPath;
    public static TextureRegion backgroundTexture;
    private RunnableHandler runnableRemoveHandler;
    
    private List<CVMSprite> spriteList;
    private List<CVMText> textList;
    
    private List<IEntity> entitiesToRemove;
	
	public CVMAbstractScene(String backgroundPath, int id) {
		this.backgroundPath = backgroundPath;
		this.id = id;
		this.state = State.Stopped;
		
		spriteList = new ArrayList<CVMSprite>();
		textList = new ArrayList<CVMText>();
		
		entitiesToRemove = new ArrayList<IEntity>();
		
		runnableRemoveHandler = new RunnableHandler();
		
        registerUpdateHandler(runnableRemoveHandler);

	}
	
	public void addSprite(CVMSprite sprite) {
		if (state == State.Stopped) {
			spriteList.add(sprite);
		}
		else if (state == State.Started) {
			this.addSpriteToScene(sprite);
		}
	}
	
	public void removeSprite(CVMSprite sprite) {
		if (state == State.Stopped) {
			spriteList.remove(sprite);
		}
		else if (state == State.Started) {
			this.removeSpriteFromScene(sprite.getSprite());
		}
	}
	
	public void addText(CVMText sprite) {
		textList.add(sprite);
	}
	
	public abstract void sceneTouched(TouchEvent pSceneTouchEvent);
	public abstract void managedUpdate(float pSceneTouchEvent);

	@Override
	public void onManagedUpdate(float secondsElapsed) {
		if (state == State.Started) {
			managedUpdate(secondsElapsed);
		}
		
		super.onManagedUpdate(secondsElapsed);
	}
	
	@Override
	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		boolean result = super.onSceneTouchEvent(pSceneTouchEvent);
		
		if (state == State.Started) {
			sceneTouched(pSceneTouchEvent);
		}
		
		return result;
	}
	
	public void load(TextureManager manager, Context context, Engine engine, CVMTextureManager cvmTextureManager) throws IllegalStateException, IOException {
		this.cvmTextureManager = cvmTextureManager;
		
		// Load background texture
		BitmapTextureAtlas mBackgroundTexture = new BitmapTextureAtlas(manager, CVMGameActivity.CAMERA_WIDTH, CVMGameActivity.CAMERA_HEIGHT, TextureOptions.DEFAULT);
		backgroundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, context, backgroundPath, 0, 0);

	    manager.loadTexture(mBackgroundTexture);

		// Define music
	    if (musicPath != null) {
	    	music = MusicFactory.createMusicFromAsset(engine.getMusicManager(), context, musicPath);
	    	music.setLooping(musicLoop);
	    }
	    
	    for (CVMText text : textList) {
	    	//Font font = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), text.getSize());
	    	BitmapTextureAtlas mFontTexture = new BitmapTextureAtlas(engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
	    	Font font = new Font(engine.getFontManager(), mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.WHITE);
	    	font.load();
	    	text.setFont(font);
	    }
	}
	
	private void addSpriteToScene(final CVMSprite sprite) {
    	sprite.setTextureRegion(cvmTextureManager.getTextureById(sprite.getTextureId()));
    	
		Sprite tmp = null;
		
		if (cvmTextureManager.isTiled(sprite.getTextureId())) {
			tmp = new AnimatedSprite(sprite.getPosX(), sprite.getPosY(), sprite.getWidth(), sprite.getHeight(), ((TiledTextureRegion)sprite.getTextureRegion()).deepCopy(), vertexBufferObjectManager) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (sprite instanceof TouchAreaListener) {
						if (CVMAbstractScene.this.state == State.Started) {
							((TouchAreaListener)sprite).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY, gameActivity, CVMAbstractScene.this);
						}
					}
					return true;
				};
				
				@Override
			    protected void onManagedUpdate(float pSecondsElapsed) {
					if (sprite instanceof ManagedUpdateListener) {
						if (CVMAbstractScene.this.state == State.Started) {
							((ManagedUpdateListener)sprite).managedUpdate(pSecondsElapsed, CVMAbstractScene.this);
						}
					}
					
					super.onManagedUpdate(pSecondsElapsed);
			    }
			};
		}
		else {
			tmp = new Sprite(sprite.getPosX(), sprite.getPosY(), sprite.getWidth(), sprite.getHeight(), sprite.getTextureRegion(), vertexBufferObjectManager) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (sprite instanceof TouchAreaListener) {
						if (CVMAbstractScene.this.state == State.Started) {
							((TouchAreaListener)sprite).onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY, gameActivity, CVMAbstractScene.this);
						}
					}
					return true;
				};
				
				@Override
			    protected void onManagedUpdate(float pSecondsElapsed) {
					if (sprite instanceof ManagedUpdateListener) {
						if (CVMAbstractScene.this.state == State.Started) {
							((ManagedUpdateListener)sprite).managedUpdate(pSecondsElapsed, CVMAbstractScene.this);
						}
					}
					
					super.onManagedUpdate(pSecondsElapsed);
			    }
			};
		}
		
		sprite.setSprite(tmp);
		this.attachChild(tmp);
		
		Log.i("CVMBalls", "Scene #" + CVMAbstractScene.this.getId() + " added an entity");
		
		if (sprite instanceof TouchAreaListener) {
			this.registerTouchArea(tmp);
		}
	}
	
	public void prepare(VertexBufferObjectManager vertexBufferObjectManager, final CVMGameActivity cvmGameActivity) {
		this.vertexBufferObjectManager = vertexBufferObjectManager;
		this.gameActivity = cvmGameActivity;
	}
	
	private void removeSpriteFromScene(final Sprite sprite) {
		runnableRemoveHandler.postRunnable(new Runnable() {
            @Override
            public void run() {
				CVMAbstractScene.this.unregisterTouchArea(sprite);
				CVMAbstractScene.this.detachChild(sprite);
            }
		});
	}
	
	public void reset() {
		
		for (int i = 0; i < CVMAbstractScene.this.getChildCount(); i++) {
			IEntity s = (IEntity) CVMAbstractScene.this.getChildByIndex(i);
			entitiesToRemove.add(s);
		}
		
		runnableRemoveHandler.postRunnable(new Runnable() {
            @Override
            public void run() {

				for (int i = 0; i < entitiesToRemove.size(); i++) {
					IEntity s = entitiesToRemove.get(i);
					
					if (s instanceof Sprite) {
						CVMAbstractScene.this.unregisterTouchArea((Sprite)s);
					}
					
					Log.i("CVMBalls", "Scene #" + CVMAbstractScene.this.getId() + " removed an entity");
					
					CVMAbstractScene.this.detachChild(s);
				}
				
				entitiesToRemove.clear();
            }
		});
	}
	
	private void populate() {
        SpriteBackground bg = new SpriteBackground(new Sprite(0, 0, backgroundTexture, vertexBufferObjectManager));
        this.setBackground(bg);
        
		runnableRemoveHandler.postRunnable(new Runnable() {
            @Override
            public void run() {
		        // Add sprites to scene and register touch events
			    for (final CVMSprite sprite : spriteList) {
			    	addSpriteToScene(sprite);
			    }
		
			    for (CVMText text : textList) {
			    	Text txt = new Text(text.getPosX(), text.getPosY(), text.getFont(), text.getDisplayText(), new TextOptions(HorizontalAlign.LEFT), gameActivity.getVertexBufferObjectManager());
			    	txt.setColor(text.getColor());
			    	text.setText(txt);
			    	
			    	CVMAbstractScene.this.attachChild(txt);
			    }
            }
		});
	}
	
	public abstract void started();
	
	public void start() {
		populate(); 
		
		if (music != null) {
			music.play();
		}
		
		started();

		state = State.Started;
		Log.i("CVMAndEngine", "Scene " + id +" started");
	}
	
	public void stop() {
		reset();
		
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
