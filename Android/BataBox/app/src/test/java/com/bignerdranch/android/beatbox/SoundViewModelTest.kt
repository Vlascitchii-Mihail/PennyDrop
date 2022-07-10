package com.bignerdranch.android.beatbox

import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SoundViewModelTest {

    private lateinit var beatBox: BeatBox
    private lateinit var sound: Sound
    private lateinit var subject: SoundViewModel

    @Before
    fun setUp() {
        beatBox = mock(BeatBox::class.java)
        sound = Sound("assetPath")
        subject = SoundViewModel(beatBox)
        subject.sound = sound
    }

    @Test fun exposesSoundNameAsTitle() {
//        assertThat(subject.title, `is`(sound.name))
        assertSame(subject.title, sound.name)
    }

    @Test fun callBeatBoxPlayOnButtonClicked() {
        subject.onButtonClicked()
        verify(beatBox).play(sound)
    }
}