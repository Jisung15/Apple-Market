package com.example.applemarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerViewBinding

class MyAdapter(private val item: MutableList<Item>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    // Holder에 변수 설정
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

    // 그냥 클릭 리스너 설정
    interface OnClick {
        fun onClick(view: View, position: Int)
    }

    // 길게 눌렀을 때 클릭 리스너 설정
    interface LongClick {
        fun onLongClick(view: View, position: Int)
    }

    var click: OnClick? = null
    var click2: LongClick? = null

    // 이건 Holder를 만드는 것 같다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 본격적으로 설정 시작
    override fun onBindViewHolder(holder: Holder, position: Int) {

        // 그냥 클릭 리스너
        holder.itemView.setOnClickListener {
            click?.onClick(it, position)
        }

        // 길게 클릭했을 때 클릭 리스너
        holder.itemView.setOnLongClickListener {
            click2?.onLongClick(it, position); true
        }

        // DataList에 있는 값을 어댑터에 넣어주기 시작
        // 이걸 RecyclerView로 쭈욱 넣을 것이다.
        holder.image.setImageResource(item[position].dImage)
        holder.mainText.text = item[position].dItemText
        holder.address.text = item[position].dAddress
        holder.price.text = item[position].dPrice
        holder.chat.text = item[position].dChat.toString()
        holder.heartCount.text = item[position].dHeart.toString()

    }

    // 여기부터는 크기와 위치 설정하는 곳. 지금은 별로 중요하지 않다.
    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}