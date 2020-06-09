package fr.iut.tetris.controllers;

import fr.iut.tetris.Config;
import fr.iut.tetris.Log;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioController {
	float soundEffetLineVolumeControl=0;
	float musicLineVolumeControl=0;

	Thread bgMusicThread;
	ExecutorService sfxThreadPool = Executors.newFixedThreadPool(10);

	/**
	 * Read the configured audio level in the config
	 */
	public AudioController() {
		this.setGain_SFX(Config.getInstance().getInt("VOLUME_SFX"));
		this.setGain_MUSIC(Config.getInstance().getInt("VOLUME_MUSIC"));
	}

	/**
	 * Play an audio effect in the background
	 * @param file path of the audio file
	 */
	public void playSFX(URL file) {
		sfxThreadPool.submit(new Runnable() {
			public void run() {
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					Log.info(this,"Started sfx: "+file.getFile());

					Clip clip = null;
					try {
						clip = AudioSystem.getClip();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
					clip.open(audioInputStream);

					FloatControl ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
					ctrl.setValue(soundEffetLineVolumeControl);

					clip.start();
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Change the curent backgorund music. If there is no music the music will start
	 * @param file path of the audio file
	 */
	public void setMusicTrack(URL file) {
		if(bgMusicThread != null) {
			bgMusicThread.interrupt();
		}
		Runnable r = new Runnable() {
			public void run() {
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
					Log.info(this,"Started bg music playing: "+file.getFile());

					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);

					FloatControl ctrl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);

					clip.loop(Clip.LOOP_CONTINUOUSLY);
					clip.start();
					while (! Thread.currentThread().isInterrupted()) {
						ctrl.setValue(musicLineVolumeControl);
					}
					clip.stop();

					Log.info(this,"Stop playing bg music");
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		};
		bgMusicThread = new Thread(r);
		bgMusicThread.start();
	}

	/**
	 * The the audio volume of the sfx track
	 * @param gain the gain min:-80 max6
	 */
	public void setGain_SFX(float gain) {
		if(gain < -80) gain = -80;
		if(gain > 6) gain = 6;
		soundEffetLineVolumeControl = gain;
	}

	/**
	 * The the audio volume of the music track
	 * @param gain the gain min:-80 max6
	 */
	public void setGain_MUSIC(float gain) {
		if(gain < -80) gain = -80;
		if(gain > 6) gain = 6;
		musicLineVolumeControl = gain;
	}
}
