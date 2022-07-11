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

class BeatBox(private val assets: AssetManager) {
    val sounds: List<Sound>
    private var streamId: Int? = null
    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(MAX_SOUNDS).build()

    init {
        sounds = loadSounds()
    }
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {
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
            val assetPath = "$SOUND_FOLDER/$filename"
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            } catch(ioe: IOException) {
                Log.d(TAG, "Could not load sound $filename, ioe")
            }
        }
        return sounds
    }

    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    fun play(sound: Sound) {
        sound.soundId?.let {
            streamId = soundPool.play(it, 1.0f, 1.0f, 1, 6, Beet.speed)
        }
    }

    fun playSpeed(progress: Int) {
        streamId?.let { soundPool.setRate(it, progress * 0.1f) }
    }

    fun release() {
        soundPool.release()
    }
}

object Beet {
    var speed = 1.0f
}