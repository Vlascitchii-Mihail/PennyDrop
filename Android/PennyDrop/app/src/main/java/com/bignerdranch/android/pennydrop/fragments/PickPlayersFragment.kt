package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
//import androidx.core.view.isVisible
import com.bignerdranch.android.pennydrop.R
import com.bignerdranch.android.pennydrop.databinding.FragmentPickPlayersBinding
import com.bignerdranch.android.pennydrop.viewmodels.GameViewModel
import com.bignerdranch.android.pennydrop.viewmodels.PickPlayersViewModel
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PickPlayersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PickPlayersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //receiving PickPlayersViewModel exemplar from Activity
    //each fragment connected with this action will get the same ViewModels exemplar
    //activityViewModels<>() - Возвращает делегат свойства для доступа к ViewModel родительской активности
    private val pickPlayersViewModel by activityViewModels<PickPlayersViewModel>()

    //receiving GameViewModel exemplar from Activity
    //activityViewModels<GameViewModel>() - Возвращает делегат свойства для доступа к ViewModel родительской активности
    private val gameViewModel by activityViewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
//        val view =  inflater.inflate(R.layout.fragment_pick_players, container, false)
//
//        with(view.findViewById<View>(R.id.mainPlayer)) {
//            this?.findViewById<CheckBox>(R.id.checkbox_player_active)?.let {
//                it.isVisible = false
//                it.isChecked = true
//            }
//        }
//
//        return view

        //inflating the view with the FragmentPickPlayersBinding class
        val binding = FragmentPickPlayersBinding.inflate(inflater, container, false).apply {

            //initialize the variable vm from the fragment_pick_player.xml
            this.vm = pickPlayersViewModel

            this.buttonPlayGame.setOnClickListener {

                //calling the suspend function startGame using the coroutine block
                //viewLifecycleOwner - Get a LifecycleOwner that represents the Fragment's View lifecycle.
                viewLifecycleOwner.lifecycleScope.launch {

                    //calling the suspend function
                    gameViewModel.startGame(

                        //filter LiveData
                        pickPlayersViewModel.players.value?.filter{ newPlayer ->
                            newPlayer.isIncluded.get()
                        }?.map { newPlayer ->

                            //transform newPlayer's object to Player's object
                            newPlayer.toPlayer()

                            //if don't have any player
                        } ?: emptyList()
                    )

                    //go to the GameFragment
                    //navigate() - Navigate to a destination from the current navigation graph.
                    findNavController().navigate(R.id.gameFragment)
                }
            }
        }

        //root - Возвращает самое внешнее представление в файле макета, связанном с привязкой
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PickPlayersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PickPlayersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}