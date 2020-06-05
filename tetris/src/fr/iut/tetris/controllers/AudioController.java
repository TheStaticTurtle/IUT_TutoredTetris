package fr.iut.tetris.controllers;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioController {
	float soundEffetLineVolumeControl=0;
	float musicLineVolumeControl=0;

	Thread bgMusicThread;

	public AudioController() {
	}

	public void playSFX(URL file) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					System.out.println("Started sfx: "+file.getFile());

					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);

					FloatControl ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
					ctrl.setValue(soundEffetLineVolumeControl);

					clip.start();
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		};
		Thread sfx = new Thread(r);
		sfx.start();
	}
	public void setMusicTrack(URL file) {
		if(bgMusicThread != null) {
			bgMusicThread.interrupt();
		}
		Runnable r = new Runnable() {
			public void run() {
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					System.out.println("Started bg music playing: "+file.getFile());

					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);

					FloatControl ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);

					clip.loop(Clip.LOOP_CONTINUOUSLY);
					clip.start();
					while (! Thread.currentThread().isInterrupted() ) {
						ctrl.setValue(musicLineVolumeControl);
					}
					clip.stop();

					System.out.println("Stop playing bg music");

				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		};
		bgMusicThread = new Thread(r);
		bgMusicThread.start();
	}

	public void setGain_SFX(float gain) {
		if(gain < -80) gain = -80;
		if(gain > 6) gain = 6;
		soundEffetLineVolumeControl = gain;
	}

	public void setGain_MUSIC(float gain) {
		if(gain < -80) gain = -80;
		if(gain > 6) gain = 6;
		musicLineVolumeControl = gain;
	}
}
