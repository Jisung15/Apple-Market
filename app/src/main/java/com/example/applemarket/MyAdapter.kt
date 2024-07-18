package com.example.applemarket

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerViewBinding

class MyAdapter(private val item: MutableList<Item>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    // Holder에 변수 설정 (이미지, 텍스트, 주소, 가격, 채팅 개수, 좋아요 개수, 좋아요 이미지)
    inner class Holder(private val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivItemImage
        val mainText = binding.tvItemText
        val address = binding.tvAddress
        val price = binding.tvPrice
        val chat = binding.tvChatCount
        val heartCount = binding.tvHeartCount
        val heart = binding.ivHeart
    }

    // 클릭 리스너 설정 (아이템 클릭하면 Detail Page로 넘어가는 설정 위해서)
    interface OnClick {
        fun onClick(view: View, position: Int)
    }

    // 길게 눌렀을 때 클릭 리스너 설정 (아이템 길게 누르면 삭제 다이얼로그 띄우는 설정 위해서)
    interface LongClick {
        fun onLongClick(view: View, position: Int)
    }

    // 두 변수를 설정하는데 각각 클릭 리스너들의 초기 값은 null = 한 번도 클릭 안 한 초기 상태 (솔직히 잘 모르겠다.. ㅋㅋㅋ)
    var click: OnClick? = null
    var longClick: LongClick? = null

    // 이건 item_recycler_view.xml 내용을 바탕으로 Holder를 만드는 것 같다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 본격적으로 Holder 설정 시작
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        // 클릭 리스너
        holder.itemView.setOnClickListener {
            click?.onClick(it, position)
        }

        // 길게 클릭했을 때 클릭 리스너
        holder.itemView.setOnLongClickListener {
            longClick?.onLongClick(it, position); true                        // 여기 true랑 false의 차이는 뭔지 모르겠다.. ㅋㅋㅋ
        }

        // 여기도 천 단위마다 콤마(,) 넣기
        var format = DecimalFormat("#,###")
        var price = item[position].dPrice

        // DataList에 있는 값을 Adapter에 넣어주기 시작
        // 이것들을 묶어서 item_recycler_view.xml에 넣고, 그걸 RecyclerView로 MainActivity에 쭈욱 넣을 것이다.     (맞는 지는 모르겠다..)
        holder.image.setImageResource(item[position].dImage)
        holder.mainText.text = item[position].dItemText
        holder.address.text = item[position].dAddress
        holder.price.text = "${format.format(price)}원"
        holder.chat.text = item[position].dChat.toString()
        holder.heartCount.text = item[position].dHeart.toString()

        // 좋아요 이미지 설정
        // 좋아요 버튼을 누르지 않은 상태(false)이면 그냥 빈 하트...인데 왜 heart_full일까? 왜냐하면 Boolean은 true부터 시작 하기 때문이다. heart 이미지를 else에 안 쓰고 if에 쓰려면 if문 조건 앞에 !를 붙이면 된다.
        if (item[position].dHeartCheck) {
            holder.heart.setImageResource(R.drawable.heart_full)
        } else {
            holder.heart.setImageResource(R.drawable.heart)
        }

//        위에서 말한 코드가 바로 이것이다. 테스트 결과 정상적으로 동작함
//        if (!item[position].dHeartCheck) {
//            holder.heart.setImageResource(R.drawable.heart)
//        } else {
//            holder.heart.setImageResource(R.drawable.heart_full)
//        }

        // 좋아요 개수는 MainPage에서 변화시키지 않고, Detail Page에서 변화시키고 메인에 적용시키는 것이니 이렇게 작성
        holder.heartCount.text = item[position].dHeart.toString()
    }

    // 여기 부분은 Datalist 크기와 아이템 위치 설정. 지금은 별로 중요하지 않다.
    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}