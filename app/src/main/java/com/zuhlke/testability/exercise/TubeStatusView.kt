package com.zuhlke.testability.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zuhlke.testability.R
import com.zuhlke.testability.common.TubeLine
import kotlinx.android.synthetic.main.status_list_item.view.*

class TubeStatusViewHolder(view: View) : RecyclerView.ViewHolder(view)

class TubeStatusAdapter(private val tubeLines: List<TubeLine>) : RecyclerView.Adapter<TubeStatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TubeStatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.status_list_item, parent, false)
        return TubeStatusViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tubeLines.size
    }

    override fun onBindViewHolder(holder: TubeStatusViewHolder, position: Int) {
        holder.itemView.line_title_tv.text = tubeLines[position].name
        holder.itemView.line_status_tv.text = tubeLines[position].statuses
            .sortedBy { it.severity }
            .joinToString(separator = ", ") { it.description }

        val emojiStatusIndicator = holder.itemView.emoji_status_indicator

        val mostSevereStatus = tubeLines[position].statuses.maxBy { it.severity }

        when (mostSevereStatus?.severity) {
            0 -> emojiStatusIndicator.setText(R.string.emoji_indicator_okay)
            20 -> emojiStatusIndicator.setText(R.string.emoji_indicator_problem)
            else -> emojiStatusIndicator.setText(R.string.emoji_indicator_unknown)
        }

    }

}