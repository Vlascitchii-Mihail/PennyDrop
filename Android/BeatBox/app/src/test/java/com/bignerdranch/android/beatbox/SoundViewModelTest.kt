package com.bignerdranch.android.beatbox

import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertSame
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SoundViewModelTest {

    private lateinit var beatBox: BeatBox
    private lateinit var sound: Sound
    private lateinit var subject: SoundViewModel


    //@Before - code running once before executing tests
    //tests preparing
    @Before
    fun setUp() {

        //mock() = mockito function, creates BeatBox object simulation
        beatBox = mock(BeatBox::class.java)

        //simple object
        sound = Sound("assetPath")

        //SoundViewModel object creating
        //subject - object under test
        subject = SoundViewModel(beatBox)
        subject.sound = sound
    }

    //first test
    @Test fun exposesSoundNameAsTitle() {

        //assets that subject.title == sound.name
//        assertThat(subject.title, `is`(sound.name))

        //assets that subject.title == sound.name
        //Asserts that two objects refer to the same object.
        assertSame(subject.title, sound.name)
    }

    //click listener test
    @Test fun callBeatBoxPlayOnButtonClicked() {
        subject.onButtonClicked()

        //Подтверждает, что определенное поведение произошло однажды.
        //checking that for object beatBox was called function play(sound) with parameter Sound
        verify(beatBox).play(sound)
    }
}