package ca.qc.cvm.cvmandengine.util;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.particle.ParticleSystem;

import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;

public class CVMParticleSystemUtils {

	public static void setParticleSystemDuration(final CVMAbstractScene scene, final ParticleSystem particleSystem, float stopSpawnSec, float removeSec) {
		 scene.registerUpdateHandler(new TimerHandler(stopSpawnSec, new ITimerCallback() {
		        @Override
		        public void onTimePassed(final TimerHandler pTimerHandler){
		        	particleSystem.setParticlesSpawnEnabled(false);
		        	scene.unregisterUpdateHandler(pTimerHandler);
		        }
		               
		}));
	    
		scene.registerUpdateHandler(new TimerHandler(removeSec, new ITimerCallback() {
		        @Override
		        public void onTimePassed(final TimerHandler pTimerHandler){
		                scene.detachChild(particleSystem);
		                scene.unregisterUpdateHandler(pTimerHandler);
		        }              
		}));
	}
}
