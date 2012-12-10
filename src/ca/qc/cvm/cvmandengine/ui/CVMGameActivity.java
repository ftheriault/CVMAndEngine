package ca.qc.cvm.cvmandengine.ui;

import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.Log;

import ca.qc.cvm.cvmandengine.CVMSoundManager;
import ca.qc.cvm.cvmandengine.CVMTextureManager;
import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;
import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene.State;

public class CVMGameActivity extends SimpleBaseGameActivity {
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
 
    private Camera mCamera;
    
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
	
	public void setSoundManager(CVMSoundManager soundManager) {
		this.soundManager = soundManager;
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
					sceneTmp.stop();
					sceneTmp.clearChildScene();
					break;
				}
			}
		}

		scene.clearChildScene();
        scene.start();
        
		currentSceneId = id;
		
		Log.i("CVMAndEngine", "Set main scene" + id);
		this.mEngine.setScene(scene);
	}
	public void changeScene(int id, boolean isChildScene) {
		CVMAbstractScene scene = null;
		
		for (CVMAbstractScene sceneTmp : sceneList) {
			if (sceneTmp.getId() == id) {
				scene = sceneTmp;
				break;
			}
		}
		
		if (scene != null) {
			CVMAbstractScene currentScene = null;
			
			for (CVMAbstractScene sceneTmp : sceneList) {
				if (sceneTmp.getId() == currentSceneId) {
					sceneTmp.stop();
					currentScene = sceneTmp;
					break;
				}
			}
			
			if (isChildScene) {
				Log.i("CVMAndEngine", "Added child scene :" + currentSceneId +" -> " + id);
				currentScene.setChildScene(scene);
			}
			else if (currentScene!= null && currentScene.getParentScene() != null){
				Log.i("CVMAndEngine", "Added scene :" + currentSceneId +" -> " + id);
				((Scene)currentScene.getParentScene()).setChildScene(scene);
			}
			else {
				Log.i("CVMAndEngine", "Set main scene" + id);
				this.mEngine.setScene(scene);
			}
			
	        scene.start();
	        
			currentSceneId = id;
		}
		else {
			Log.w("CVMGameEngine", "Scene not found");
		}
	}	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
 
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
        							new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
        
        options.getAudioOptions().setNeedsSound(true);
        options.getAudioOptions().setNeedsMusic(true);

        return options;
	}

	@Override
	protected void onCreateResources() {
		try {
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
	
    @Override
    public void onBackPressed() {
		Scene scene = null;
		
		for (CVMAbstractScene tmp : sceneList) {
			if (tmp.getId() == currentSceneId) {
				scene = tmp;
				break;
			}
		}
		
		if (scene == null) {
			Log.i("CVMAndEngine", "Scene was not found, quitting");
		}
		        
        if(scene != null && scene.getParentScene() != null){
        	Log.i("CVMAndEngine", "Back pressed, going to parent");
        	((CVMAbstractScene)scene).stop();
        	
        	currentSceneId = ((CVMAbstractScene)scene.getParentScene()).getId(); 
        	scene.getParentScene().clearChildScene();
        	((CVMAbstractScene)scene.getParentScene()).start();
        }
        else{
            this.finish();
        }
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
		
		scene.stopMusic();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	CVMAbstractScene scene = null;
		
		for (CVMAbstractScene tmp : sceneList) {
			if (tmp.getId() == currentSceneId) {
				scene = tmp;
				break;
			}
		}
		
		if (scene.getState() == State.Started) {
			scene.playMusic();
		}
    }

}
