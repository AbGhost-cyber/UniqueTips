package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.R
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.models.VipMatchesItem
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.getTimeAgo
import kotlinx.android.synthetic.main.vip_matches_items.view.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class VipMatchesItemAdapter : RecyclerView.Adapter<VipMatchesItemAdapter.VipMatchesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VipMatchesViewHolder {
        return VipMatchesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.vip_matches_items, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(
        holder: VipMatchesViewHolder,
        position: Int
    ) {
        val items = differ.currentList[position]
        holder.itemView.apply {
            try {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                val tipTime = items.date
                val date = dateFormat.parse(tipTime)
                date?.let {
                    val agoTime = getTimeAgo(it)
                    items.agoTime = agoTime
                }

            } catch (e: Exception) {

            }
            league_Name.text = items.leagueName
            teams.text = "${items.teamOne}  vs  ${items.teamTwo}"

            if (!items.isMatchPlayed) {
                matchState.setImageResource(R.drawable.match_not_played_icon)
                datePlusVipNamePlusOddPlusWon.text =
                    "${items.date} ${items.vipName} VIP"
                HT_FT_scores.text = null
                HT_FT_scores.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.no_scores_icon,
                    0
                )
            } else {
                when {
                    items.matchWon -> {
                        matchState.setImageResource(R.drawable.match_won)
                        datePlusVipNamePlusOddPlusWon.text =
                            "${items.date} ${items.vipName} VIP\n ${items.odds} Odds WON"
                    }
                    !items.matchWon -> {
                        matchState.setImageResource(R.drawable.match_lost_icon)
                        datePlusVipNamePlusOddPlusWon.text =
                            "${items.date} ${items.vipName} VIP\n ${items.odds} Odds LOST"
                    }
                }
                HT_FT_scores.text = "${items.HalfTimeScore}\n${items.FullTimeScore}"
                HT_FT_scores.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
            }

            if (items.vipName.toLowerCase(Locale.ROOT).contains("ht")
                || items.vipName.toLowerCase(Locale.ROOT).contains("ft")
            ) {
                fixtures_inOdds.text = "HT/FT ${items.HalfAndFullTimeScoresInOdds}"
            }
            if (items.isCorrectScore) {
                fixtures_inOdds.text = "Correct Score: ${items.correctScore}"
            } else {
                fixtures_inOdds.text = items.HalfAndFullTimeScoresInOdds
            }
            odds.text = items.odds
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<VipMatchesItem>() {
        override fun areItemsTheSame(oldItem: VipMatchesItem, newItem: VipMatchesItem): Boolean {
            return oldItem.teamOne == newItem.teamOne
        }

        override fun areContentsTheSame(oldItem: VipMatchesItem, newItem: VipMatchesItem): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)

    inner class VipMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}