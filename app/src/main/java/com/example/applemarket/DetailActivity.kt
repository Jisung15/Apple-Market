package com.example.applemarket

import android.content.Intent
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
        const val ITEM = "item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DataList를 MainActivity에서 받아옴
        val item = intent.getParcelableExtra<Item>(ITEM)

        // 그 받아온 DataList에서 값을 꺼내서 각각의 위젯에 적용
        binding.ivDetailImage.setImageResource(item?.dImage ?: 0)
        binding.tvDetailName.text = item?.dName
        binding.tvDetailAddress.text = item?.dAddress
        binding.tvDetailTitle.text = item?.dItemText
        binding.tvDetailMessage.text = item?.dMessage
        binding.tvDetailPrice.text = item?.dPrice

        // 좋아요 버튼의 변경을 확인하기 위한 변수
        var changeImage = true
        var count: Int = item?.dHeart ?: 0          // 이건 좋아요 개수인데 Detail Page에는 표시하지 않으니 여기로 따로 빼놓음

        // 좋아요 버튼을 눌렀을 때 좋아요 버튼 이미지와 좋아요 수를 결정하는 부분
        binding.ivBottomHeart.setOnClickListener { view ->
            if (changeImage) {                                                          // 처음 상태에서 누르면 빨간 하트 이미지로 바뀌고, 좋아요 수 + 1
                binding.ivBottomHeart.setImageResource(R.drawable.heart_full)
                changeImage = false
                count++
                item?.dHeart = item?.dHeart!! + 1
                Snackbar.make(view, "관심 목록에 추가되었습니다.\n좋아요 개수 : ${count}", Snackbar.LENGTH_SHORT)
                    .show()
            } else {                                                                      // 빨간 하트 상태에서 누르면 빈 하트 이미지로 바뀌고, 좋아요 수 -1
                binding.ivBottomHeart.setImageResource(R.drawable.heart)
                changeImage = true
                count--
                Snackbar.make(view, "관심 목록에서 제거되었습니다.\n좋아요 개수 : ${count}", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        // 뒤로 가기 버튼 누르면 바뀐 DataList를 들고 MainActivity로 이동
        binding.ivBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(ITEM, item)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}