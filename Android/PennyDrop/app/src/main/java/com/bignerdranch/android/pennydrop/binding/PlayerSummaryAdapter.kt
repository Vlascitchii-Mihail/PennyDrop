package com.bignerdranch.android.pennydrop.binding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.pennydrop.R
import com.bignerdranch.android.pennydrop.databinding.PlayerSummaryListItemBinding
import com.bignerdranch.android.pennydrop.types.PlayerSummary

/**
 * displays the PlayerSummary objects in the RecyclerView
 */
class PlayerSummaryAdapter : ListAdapter<PlayerSummary, PlayerSummaryAdapter.PlayerSummaryViewHolder>(
    PlayerSummaryDiffCallback()
) {

    //represents a new ViewHolder's object
    override fun onCreateViewHolder(parent: ViewGroup, viewTipe: Int) : PlayerSummaryViewHolder =

        //create a new ViewHolder's object
        PlayerSummaryViewHolder(

            //Utility class to create ViewDataBinding from layouts
            DataBindingUtil.inflate(

                //LayoutInflater - Создает экземпляр XML-файла макета в соответствующих объектах View.
                //from(parent.context) - Obtains the LayoutInflater from the given context.
                LayoutInflater.from(parent.context), R.layout.player_list_item, parent, false
            )
        )


    //bind the PlayerSummaryViewHolder
    override fun onBindViewHolder(viewHolder: PlayerSummaryViewHolder, position: Int) {

        //getItem(position) - receive the PlayerSummary's object from ListAdapter, depends where we are in the list
        viewHolder.bind(getItem(position))
    }

    /**
     * binds the PlayerSummary objects to the binding object in the player_summary_list_item.xml
     */
    inner class PlayerSummaryViewHolder(
        private val binding: PlayerSummaryListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

         fun bind(item: PlayerSummary) {
             binding.apply {

                 //playerSummary object in the player_summary_list_item.xml
                 playerSummary = item

                 //executePendingBindings() -  используется, чтобы биндинг не откладывался,
                 // а выполнился как можно быстрее. Это критично в случае с RecyclerView.
                 executePendingBindings()
             }
         }
    }

    //DiffUtil is a utility class that calculates the difference between two lists and
    // outputs a list of update operations that converts the first list into the second one.
    private class PlayerSummaryDiffCallback : DiffUtil.ItemCallback<PlayerSummary>() {

        //Called to check whether two objects represent the same item.
        override fun areItemsTheSame(oldItem: PlayerSummary, newItem: PlayerSummary) : Boolean =
            oldItem.id == newItem.id

        //Called to check whether two items have the same data.
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PlayerSummary, newItem: PlayerSummary) : Boolean =
            oldItem == newItem
    }
}