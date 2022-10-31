package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pennydrop.R
import com.bignerdranch.android.pennydrop.binding.PlayerSummaryAdapter
import com.bignerdranch.android.pennydrop.viewmodels.RankingsViewModel

/**
 * contains Recycler View
 */

class RankingsFragment : Fragment() {

    //create a new RankingsViewModel exemplar
    private val viewModel by activityViewModels<RankingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = inflater.inflate(R.layout.fragment_rankings, container, false)

        val playerSummaryAdapter = PlayerSummaryAdapter()
//smart cast
        if (view is RecyclerView) {
            with(view) {
                adapter = playerSummaryAdapter

                //add decoration to RecyclerView
                addItemDecoration(

                    //Creates a divider RecyclerView.ItemDecoration that can be used with a LinearLayoutManager.
                    DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                )
            }
        }

        //LiveData.observe - listener's registration
//         viewLifecycleOwner - Get a LifecycleOwner that represents the Fragment's View lifecycle.
//         fragment's view lifecycle. Снимает слушателя после уничтожения фрагмента

//         Observer - reaction to a new data (lambda) Adds the given observer to the observers list within
//         the lifespan (срока жизни) of the given owner. The events are dispatched on the main thread.
        viewModel.playerSummaries.observe(viewLifecycleOwner) { summaries ->

            //submitList() - Submits (Отправляет) a new list to be diffed (для сравнения), and displayed.
            playerSummaryAdapter.submitList(summaries)
        }

        return view
    }

}