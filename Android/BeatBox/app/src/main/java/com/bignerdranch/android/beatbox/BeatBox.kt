package com.bignerdranch.android.beatbox

import android.content.res.AssetManager
import android.util.Log
import android.media.SoundPool
import android.content.res.AssetFileDescriptor
import android.widget.SeekBar
import java.io.IOException

private const val TAG = "BeatBox"
private const val SOUND_FOLDER = "sample_sounds"
private const val MAX_SOUNDS = 5

//search, tracking and music playback
/**
 * @param assets: AssetManager - using assets
 */
class BeatBox(private val assets: AssetManager) {
    val sounds: List<Sound>
    private var streamId: Int? = null

    //playing sounds object for uploading in memory some sounds
    //setMaxStreams() - quantity of sounds which can be playing in the same time
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(MAX_SOUNDS).build()

    init {

        //creating list of sounds
        sounds = loadSounds()
    }

    //access to assets
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {

            //returns the list of file's names from the following folder
            /**
             * @param SOUND_FOLDER - Path to the assets
             */
            soundNames = assets.list(SOUND_FOLDER)!!
//            Log.d(TAG, "Found ${soundNames.size} sounds")
//            Log.d(TAG, "Found ${soundNames.asList()} sounds")
//            soundNames.asList()
        } catch(e: Exception) {
            Log.d(TAG, "Could not list assets")
            return emptyList()
        }

        val sounds = mutableListOf<Sound>()
//        Log.d(TAG, "Found ${soundNames.asList()} sounds")
        soundNames.forEach { filename ->

            //creating sound's path
            val assetPath = "$SOUND_FOLDER/$filename"

            //creating sound's object
            val sound = Sound(assetPath)
            try {

                //sound uploading
                load(sound)

                //adding in  mutableListOf<Sound>()
                sounds.add(sound)
            } catch(ioe: IOException) {
                Log.d(TAG, "Could not load sound $filename, ioe")
            }
        }
        return sounds
    }

    //uploading sounds in SoundPool
    private fun load(sound: Sound) {

        //FileDescriptor creation
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)

        //file uploading and returning some ID
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    //playing sounds
    fun play(sound: Sound) {
        sound.soundId?.let {

            //playing setting up
            /**
             * @param it - sound.soundId
             * @param 1.0f - volume on the right part
             * @param 1.0f - volume on the left part
             * @param 1 - priority (ignored)
             * @param 6 - Признак циклического воспроизведения, -1 - infinitive playing
             * @param Beet.speed - playing speed
             */
            streamId = soundPool.play(it, 1.0f, 1.0f, 1, 6, Beet.speedSaveState)
        }
    }

    fun playSpeed(progress: Int) {

        //Change playback rate. The playback rate allows the application to vary the
        // playback rate (pitch) of the sound. A value of 1.0 means playback at the
        // original frequency. A value of 2.0 means playback twice as fast,
        // and a value of 0.5 means playback at half speed.
        streamId?.let { soundPool.setRate(it, progress * 0.1f) }
    }

    //release of resources
    fun release() {
        soundPool.release()
    }
}

object Beet {

    //playing speed
    var speedSaveState = 1.0f
}