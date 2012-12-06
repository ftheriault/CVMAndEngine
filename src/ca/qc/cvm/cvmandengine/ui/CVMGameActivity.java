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

public class CVMGameActivity extends SimpleBaseGameActivity {
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
 
    private Camera mCamera;
    
    private List<CVMAbstractScene> sceneList;
    private int currentSceneId;
    
    private CVMSoundManager soundManager;
    private CVMTextureManager textureManager;
	
	public void setSceneList(List<CVMAbstractScene> sceneList) {
		this.sceneList = sceneList;
		currentSceneId = sceneList.get(0).getId();;
	}
	
	public void setTextureManager(CVMTextureManager textureManager) {
		this.textureManager = textureManager;
	}
	
	public void setSoundManager(CVMSoundManager soundManager) {
		this.soundManager = soundManager;
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
			for (CVMAbstractScene sceneTmp : sceneList) {
				if (sceneTmp.getId() == currentSceneId) {
					sceneTmp.stop();
					break;
				}
			}
			
			if (isChildScene) {
				this.mEngine.getScene().setChildScene(scene);
			}
			else if (this.mEngine.getScene().getParent() != null){
				((Scene)this.mEngine.getScene().getParent()).setChildScene(scene);
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
			
			soundManager.load(this.mEngine.getSoundManager(), this);

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
        Scene scene = this.mEngine.getScene();
        
        if(scene != null && scene.hasChildScene()){
        	((CVMAbstractScene)scene.getChildScene()).stop();
        	
        	currentSceneId = ((CVMAbstractScene)this.mEngine.getScene()).getId(); 
        	
        	((CVMAbstractScene)this.mEngine.getScene()).start();
            scene.back();
        }
        else{
            this.finish();
        }
    }
    
    @Override
    public void onPause() {
    	super.onPause();

    	Scene scene = this.mEngine.getScene();
        if(scene != null && scene.hasChildScene()){
        	((CVMAbstractScene)scene.getChildScene()).stop();
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();

    	Scene scene = this.mEngine.getScene();
        if(scene != null && scene.hasChildScene()){
        	((CVMAbstractScene)scene.getChildScene()).start();
        }
    }

}
