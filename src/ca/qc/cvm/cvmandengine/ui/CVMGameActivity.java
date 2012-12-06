package ca.qc.cvm.cvmandengine.ui;

import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.Log;

import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;

public class CVMGameActivity extends SimpleBaseGameActivity {
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
 
    private Camera mCamera;
    
    private List<CVMAbstractScene> sceneList;
	
	public void setSceneList(List<CVMAbstractScene> sceneList) {
		this.sceneList = sceneList;
	}
	
	public void changeScene(int id) {
		((CVMAbstractScene)this.mEngine.getScene()).stop();
		
		CVMAbstractScene scene = null;
		
		for (CVMAbstractScene sceneTmp : sceneList) {
			if (sceneTmp.getId() == id) {
				scene = sceneTmp;
				break;
			}
		}
		
		if (scene != null) {
			scene.populate(getVertexBufferObjectManager(), this);
			
			this.mEngine.getScene().setChildScene(scene);
			
	        scene.start();
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
		for (CVMAbstractScene scene : sceneList) {
			scene.load(this.mEngine.getTextureManager(), this, this.mEngine);
		}
	}

	@Override
	protected Scene onCreateScene() {
        sceneList.get(0).populate(getVertexBufferObjectManager(), this);
        this.mEngine.setScene(sceneList.get(0));
        sceneList.get(0).start();
        
        return this.mEngine.getScene();
	}
	
    @Override
    public void onBackPressed() {
        Scene scene = this.mEngine.getScene();
        
        if(scene != null && scene.hasChildScene()){
        	((CVMAbstractScene)scene.getChildScene()).stop();
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
