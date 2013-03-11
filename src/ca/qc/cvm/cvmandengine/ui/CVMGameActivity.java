package ca.qc.cvm.cvmandengine.ui;

import java.io.IOException;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.Log;

import ca.qc.cvm.cvmandengine.CVMSoundManager;
import ca.qc.cvm.cvmandengine.CVMTextureManager;
import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;
import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene.State;

public abstract class CVMGameActivity extends SimpleBaseGameActivity {
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
 
    private Camera mCamera;
    
	private Music music;
	private String musicPath;
    
    private List<CVMAbstractScene> sceneList;
    private int currentSceneId;
    
    private CVMSoundManager soundManager;
    private CVMTextureManager textureManager;
    
    public CVMGameActivity(CVMTextureManager textureManager) {
    	this.textureManager = textureManager;
    }
	
	public void setSceneList(List<CVMAbstractScene> sceneList) {
		this.sceneList = sceneList;
		currentSceneId = sceneList.get(0).getId();
	}
	
	public List<CVMAbstractScene> getSceneList(){
		return this.sceneList;
	}
	
	public void setSoundManager(CVMSoundManager soundManager) {
		this.soundManager = soundManager;
	}
	
	public int getCurrentSceneId() {
		return currentSceneId;
	}

	public void changeScene(int id) {
		CVMAbstractScene scene = null;
		
		for (CVMAbstractScene sceneTmp : sceneList) {
			if (sceneTmp.getId() == id) {
				scene = sceneTmp;
				break;
			}
		}
		
		if (scene != null) {
			for (CVMAbstractScene sceneTmp : sceneList) {
				if (sceneTmp.getId() == currentSceneId) {
					((CVMAbstractScene)sceneTmp).stop();
					break;
				}
			}
		}
        
		currentSceneId = id;
		
		Log.i("CVMAndEngine", "Change Scene to " + id);
		this.mEngine.setScene(scene);
		
        ((CVMAbstractScene)scene).start();
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
 
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
        							new FillResolutionPolicy(), this.mCamera);
        
        options.getAudioOptions().setNeedsSound(true);
        options.getAudioOptions().setNeedsMusic(true);

        return options;
	}

	@Override
	protected void onCreateResources() {
		try {
	    	
	    	if(MultiTouch.isSupported(this)) {
	    		mEngine.setTouchController(new MultiTouchController());
	    	}
	    	
			textureManager.load(this.mEngine.getTextureManager(), this);
			
			for (CVMAbstractScene scene : sceneList) {
				scene.prepare(getVertexBufferObjectManager(), this);
				scene.load(this.mEngine.getTextureManager(), this, this.mEngine, textureManager);
			}
			
			if (soundManager != null) {
				soundManager.load(this.mEngine.getSoundManager(), this);
			}

		} catch (Exception e) {
			Log.e("CVMAndEngine", "CVMAbstractScene", e);
		}
	}

	@Override
	protected Scene onCreateScene() {
		CVMAbstractScene scene = null;
		
		for (CVMAbstractScene tmp : sceneList) {
			if (tmp.getId() == currentSceneId) {
				scene = tmp;
				break;
			}
		}
		
		scene.prepare(getVertexBufferObjectManager(), this);
		scene.start();
        this.mEngine.setScene(scene);
        
        return this.mEngine.getScene();
	}
	
	public abstract void backPressed();
	;
	@Override
	public void onBackPressed(){
		backPressed();
	}

    @Override
    public void onPause() {
    	super.onPause();
    	
    	CVMAbstractScene scene = null;
		
		for (CVMAbstractScene tmp : sceneList) {
			if (tmp.getId() == currentSceneId) {
				scene = tmp;
				break;
			}
		}
		
		if(music != null){
    		music.pause();
    	}
		
		scene.stopMusic();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
		if(music != null){
    		music.play();
    	}
    	
    	CVMAbstractScene scene = null;
		
		for (CVMAbstractScene tmp : sceneList) {
			if (tmp.getId() == currentSceneId) {
				scene = tmp;
				break;
			}
		}
		
		if (scene != null && scene.getState() == State.Started) {
			scene.playMusic();
		}
    }

    public void setMusic(String path){
    	if(musicPath == null || !musicPath.equals(path)){
	    	if(music != null){
	    		music.stop();
	    	}
    	
	    	this.musicPath = path;
	    	
	    	if (musicPath != null) {
		    	try {
					music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, musicPath);
					music.setLooping(true);
					music.play();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
    	}
    }
    
    public void pauseMusic() {
		if (music != null) {
			music.pause();
		}
	}
	
	public void stopMusic() {
		if (music != null) {
			music.stop();
			musicPath = null;
		}
	}
	
	public void playMusic() {
		if (music != null) {
			music.play();
		}
	}
	
	public Camera getCamera(){
		return this.mCamera;
	}
    
}















