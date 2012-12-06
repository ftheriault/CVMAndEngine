package ca.qc.cvm.cvmandengine.entity;

import org.andengine.input.touch.TouchEvent;

import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

public interface TouchAreaListener {
	public abstract void onAreaTouched(TouchEvent touchEvent, float touchAreaX, float touchAreaY, CVMGameActivity activity, CVMAbstractScene scene);
}
