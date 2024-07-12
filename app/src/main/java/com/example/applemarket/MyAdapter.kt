package com.example.applemarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerViewBinding

class MyAdapter(val item: MutableList<Item>) : RecyclerView.Adapter<MyAdapter.Holder>() {
    inner class Holder(val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val mainText = binding.tvMainText
        val address = binding.tvAddress
        val price = binding.tvPrice
        val chat = binding.tvChatCount
        val heart = binding.tvHeartCount
    }

    interface OnClick {
        fun onClick(view: View, position: Int)
    }

    var click: OnClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            click?.onClick(it, position)
        }

        holder.image.setImageResource(item[position].dImage)
        holder.mainText.text = item[position].dTitle
        holder.address.text = item[position].dAddress
        holder.price.text = item[position].dPrice
        holder.chat.text = item[position].dChat.toString()
        holder.heart.text = item[position].dHeart.toString()
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}