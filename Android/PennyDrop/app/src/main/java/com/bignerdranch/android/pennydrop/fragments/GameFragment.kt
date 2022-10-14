package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.bignerdranch.android.pennydrop.R
import com.bignerdranch.android.pennydrop.data.GameState
import com.bignerdranch.android.pennydrop.databinding.FragmentGameBinding
import com.bignerdranch.android.pennydrop.viewmodels.GameViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //add ViewModel's object
    private val gameViewModel by activityViewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_game, container, false)

        //inflating the view
        val binding = FragmentGameBinding.inflate(inflater, container, false).apply {

            //connecting GameViewModel with fragment_game.xml vm variable
            this.vm = gameViewModel

            //add a scrolling opportunity
            textCurrentTurnInfo.movementMethod = ScrollingMovementMethod()

            //tells the LiveData in GameViewModel to follow the same lifecycle as the entered life-cycle owner
//            lifecycleOwner - Sets the LifecycleOwner that should be used for observing changes of LiveData in this binding.
//            viewLifecycleOwner - Get a LifecycleOwner that represents the Fragment's View lifecycle
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //listen the game state
        gameViewModel.currentGame.observe(viewLifecycleOwner) { gameState->
            if (gameState.game.gameState == GameState.Finished)
                val dialog = activity?.let { AlertDialog.Builder(it).setTitle("New Game?")
                        .setIcon(R.drawable.dice_6).setMessage("Same players or new players")
                        .setPositiveButton("Same Players") { _, _ ->
                            gameViewModel.startWithSamePlayers()
                        }.setNegativeButton("New Players") { _, _ ->
                            goToPickPlayers()
                        }.setNeutralButton("Cancel") { _, _ ->

                            //here dialog is closing
                        }.create()
                }

            //display the dialog
            dialog?.show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}