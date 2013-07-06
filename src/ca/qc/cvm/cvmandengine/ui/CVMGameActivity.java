package ca.qc.cvm.cvmandengine.ui;

import java.io.IOException;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import ca.qc.cvm.cvmandengine.CVMSoundManager;
import ca.qc.cvm.cvmandengine.CVMTextureManager;
import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;
import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene.State;

public abstract class CVMGameActivity extends BaseGameActivity {
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
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception  {
		try {
	    	
	    	if(MultiTouch.isSupported(this)) {
	    		mEngine.setTouchController(new MultiTouchController());
	    	}
	    	
			textureManager.load(this.mEngine.getTextureManager(), this);
			
			CVMAbstractScene scene = sceneList.get(0);
			scene.prepare(getVertexBufferObjectManager(), this);
			scene.load(this.mEngine.getTextureManager(), this, this.mEngine, textureManager);
			scene.start();
			this.mEngine.setScene(scene);
			Log.e("Scene ID IS"," " + scene.getId());

		} catch (Exception e) {
			Log.e("CVMAndEngine", "CVMAbstractScene", e);
		}
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
        pOnCreateSceneCallback.onCreateSceneFinished(sceneList.get(0));
	}
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
	
		mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				try{
					mEngine.unregisterUpdateHandler(pTimerHandler);
					for (CVMAbstractScene scene : sceneList) {
						if(scene.getId() != currentSceneId){
							scene.prepare(getVertexBufferObjectManager(), CVMGameActivity.this);
							scene.load(CVMGameActivity.this.mEngine.getTextureManager(), CVMGameActivity.this, CVMGameActivity.this.mEngine, textureManager);
						}
					}
					
					if (soundManager != null) {
						soundManager.load(CVMGameActivity.this.mEngine.getSoundManager(), CVMGameActivity.this);
					}
					
					CVMAbstractScene scene = sceneList.get(1);
					currentSceneId = scene.getId();
					
					scene.prepare(getVertexBufferObjectManager(), CVMGameActivity.this);
					scene.start();
					Log.e("Scene ID IS"," " + scene.getId());
					CVMGameActivity.this.mEngine.setScene(scene);
				}
				catch(IOException e){
					Log.e("Populate","CVMGameActivity",e);
				}
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}
	
	public abstract void backPressed();
	
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
	    		music.release();
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















