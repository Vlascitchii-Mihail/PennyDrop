package com.bignerdranch.android.beatbox

private const val WAV = ".wav"

//contains sound information
class Sound(val assetPath: String, var soundId: Int? = null) {

    //returns full name of the sound
    //removeSuffix() - deletes suffix .waw if exist and returns without it
    val name = assetPath.split("/").last().removeSuffix(WAV)
}