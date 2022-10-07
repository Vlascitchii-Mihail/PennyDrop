package com.bignerdranch.android.pennydrop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pennydrop.R
import com.bignerdranch.android.pennydrop.binding.PlayerSummaryAdapter

/**
 * contains Recycler View
 */

class RankingsFragment : Fragment() {

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

        return view
    }

}