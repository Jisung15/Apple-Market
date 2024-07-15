package com.example.applemarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerViewBinding

class MyAdapter(private val item: MutableList<Item>) : RecyclerView.Adapter<MyAdapter.Holder>() {
    inner class Holder(private val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val mainText = binding.tvItemText
        val address = binding.tvAddress
        val price = binding.tvPrice
        val chat = binding.tvChatCount
        val heartCount = binding.tvHeartCount
//        val heart = binding.ivHeart
    }

    interface OnClick {
        fun onClick(view: View, position: Int)
    }

    interface LongClick {
        fun onLongClick(view: View, position: Int)
    }

    var click: OnClick? = null
    var click2: LongClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.itemView.setOnClickListener {
            click?.onClick(it, position)
        }

        holder.itemView.setOnLongClickListener {
            click2?.onLongClick(it, position); true
        }

        holder.image.setImageResource(item[position].dImage)
        holder.mainText.text = item[position].dItemText
        holder.address.text = item[position].dAddress
        holder.price.text = item[position].dPrice
        holder.chat.text = item[position].dChat.toString()
        holder.heartCount.text = item[position].dHeart.toString()

    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}