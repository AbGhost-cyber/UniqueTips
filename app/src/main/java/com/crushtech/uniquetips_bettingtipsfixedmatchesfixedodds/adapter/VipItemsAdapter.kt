package com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.R
import com.crushtech.uniquetips_bettingtipsfixedmatchesfixedodds.ui.VipItems
import kotlinx.android.synthetic.main.vip_items_layout.view.*

class VipItemsAdapter :
    RecyclerView.Adapter<VipItemsAdapter.VipItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VipItemsViewHolder {
        return VipItemsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vip_items_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VipItemsViewHolder, position: Int) {
        val vipItems = differ.currentList[position]

        holder.itemView.apply {

            Glide.with(context).load(vipItems.image).into(vipImg)
            vipTipName.text = vipItems.name
            setOnClickListener {
                onItemClickListener?.let {
                    it(vipItems)
                }
            }
        }

    }


    private var onItemClickListener: ((VipItems) -> Unit)? = null

    fun setOnItemClickListener(listener: (VipItems) -> Unit) {
        onItemClickListener = listener
    }


    private val differCallBack = object : DiffUtil.ItemCallback<VipItems>() {
        override fun areItemsTheSame(oldItem: VipItems, newItem: VipItems): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: VipItems, newItem: VipItems): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    inner class VipItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

