package com.example.applemarket

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import com.google.android.material.snackbar.Snackbar
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applemarket.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    companion object {
        const val EXTRA_ITEM = "item"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DataList를 MainActivity 에서 받아옴
        val item = intent.getParcelableExtra<Item>(EXTRA_ITEM) ?: return

        // 천 단위로 콤마(,) 넣기
        val format = DecimalFormat("#,###")
        val price = item.dPrice

        // 그 받아온 DataList 에서 값을 꺼내서 각각의 위젯에 적용
        with(binding) {
            ivDetailImage.setImageResource(item.dImage ?: 0)
            tvDetailName.text = item.dName
            tvDetailAddress.text = item.dAddress
            tvDetailTitle.text = item.dItemText
            tvDetailPrice.text = "${format.format(price)}원"
            tvDetailMessage.text = item.dMessage
        }
//        binding.ivDetailImage.setImageResource(item?.dImage ?: 0)
//        binding.tvDetailName.text = item?.dName
//        binding.tvDetailAddress.text = item?.dAddress
//        binding.tvDetailTitle.text = item?.dItemText
//        binding.tvDetailMessage.text = item?.dMessage
//        binding.tvDetailPrice.text = "${format.format(price)}원"

        // Main에서 좋아요가 빈 하트인 상태로(false) 넘어오면 기본 하트 상태는 빈 하트
        // 빨간 하트 상태로(true) 넘어오면 기본 하트 상태도 빨간 하트
        binding.ivBottomHeart.setImageResource(
            if (item.dHeartCheck) R.drawable.heart_full else R.drawable.heart
        )
//        if (!item!!.dHeartCheck) {
//            binding.ivBottomHeart.setImageResource(R.drawable.heart)
//        } else {
//            binding.ivBottomHeart.setImageResource(R.drawable.heart_full)
//        }

        // 좋아요 버튼을 눌렀을 때 좋아요 버튼 이미지, 좋아요 수를 결정 하는 부분
        // 왜 if문 안에 !를 조건 앞에 붙여주었냐면.. Main에서 넘어온 하트 이미지를 안 누른 빈 하트 상태가 false 상태인데 거기서 이미지를 눌러서 true로 만들고, 빨간 하트로 바꾸기 때문이다.
        binding.ivBottomHeart.setOnClickListener { view ->
            if (!item.dHeartCheck) {                                                                        // 처음 상태 -> 이미지 누르면 빨간 하트 이미지로 변환, 좋아요 수 + 1
                binding.ivBottomHeart.setImageResource(R.drawable.heart_full)
                item.dHeartCheck = true
                item.dHeart++
                Snackbar.make(view, "관심 목록에 추가\n좋아요 개수 : ${item.dHeart}", Snackbar.LENGTH_SHORT).show()
            } else {                                                                                         // 빨간 하트 상태 -> 이미지 누르면 빈 하트 이미지로 변환, 좋아요 수 - 1
                binding.ivBottomHeart.setImageResource(R.drawable.heart)
                item.dHeartCheck = false
                item.dHeart--
                Snackbar.make(view, "관심 목록에서 제거\n좋아요 개수 : ${item.dHeart}", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 뒤로 가기 버튼 누르면 바뀐 DataList(= item) 들고 MainPage로 이동 ("MainActivity로 돌아간다"는 표현이 더 적절할 듯)
        // StartActivity를 안 써주는 이유는 이미 Main이 실행되고 거기서 이 Detail Page로 넘어왔기 때문에... Main을 한 번 더 실행 시킬 필요가 없어서 그렇다.
        // 그냥 뒤(= MainActivity)로 가기만 하면 된다. 그래서 RegisterForActivityResult가 있는 것이다.
        binding.ivBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_ITEM, item)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}