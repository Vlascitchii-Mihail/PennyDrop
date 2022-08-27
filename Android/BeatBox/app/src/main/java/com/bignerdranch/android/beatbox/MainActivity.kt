package com.bignerdranch.android.beatbox
import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bignerdranch.android.beatbox.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.beatbox.databinding.ListItemSoundBinding
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider

open class MainActivity : AppCompatActivity() {

    private lateinit var beatBox: BeatBox

    //variable's type =  ActivityMainBinding, which was generated in activity_main.xml
    lateinit var binding: ActivityMainBinding
    private val viewModelSound: ViewModelSound by lazy {
        ViewModelProvider(this).get(ViewModelSound::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        beatBox = viewModelSound.beatBox
        /**
         * @param assets - AssetManager object, references to all the files from sample_sounds folder
         */
        beatBox = BeatBox(assets)

        //filling in and binding the class ActivityMainBinding, which was generated in activity_main.xml
        //DataBindingUtil - Utility class to create ViewDataBinding from layouts.
        //setContentView() - Задайте представление содержимого Activity для данного макета и верните связанную привязку.
        // Данный ресурс макета не должен быть макетом слияния
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //setting up the Recycler View using ActivityMainBinding class
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)

            //connecting binding.recyclerView.adapter with sounds from asset
            adapter = SoundAdapter(beatBox.sounds)
        }

        //setting SeekBar
        //SeekBar listener
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            //overriding interface's functions

            //when the progress value was changed by user
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                //changing playing speed ob BeatBox
                beatBox.playSpeed(progress)
                Beet.speedSaveState = progress * 0.1f
            }

            //calls when the user touches the SeekBar
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            //calls when the user stops touching the SeekBar
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    /**
     * @param binding - ListItemSoundBinding object, from list_item_sound
     * @param binding.root - root element in list_item_sound
     */
    private inner class SoundHolder(private val binding: ListItemSoundBinding):
        RecyclerView.ViewHolder(binding.root) {

            init {

                //connecting binding to SoundViewModel
                binding.viewModel = SoundViewModel(beatBox)
            }

        fun bind(sound: Sound) {
            binding.apply {

                //receiving sound from SoundAdapter.BindViewHolder()
                viewModel?.sound = sound

                //order to layout to update immediately
                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>): RecyclerView.Adapter<SoundHolder>() {

        //creating SoundHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {

            //DataBindingUtil - Utility class to create ViewDataBinding from layouts.
            //inflate<>() - Inflates a binding layout and returns the newly-created binding for that layout.
            /**
             * @param layoutInflater - used to inflate the binding layout.
             */
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(layoutInflater, R.layout.list_item_sound, parent, false)
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]

            //initialization buttons in Recycler View
            holder.bind(sound)
        }

        //returns the size of the sound's list
        override fun getItemCount(): Int = beatBox.sounds.size
    }

    override fun onPause() {
        super.onPause()

        //release of resources
        beatBox.release()
    }
}

