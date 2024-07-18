package com.example.applemarket

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import com.google.android.material.snackbar.Snackbar
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applemarket.databinding.ActivityDetailBinding
import kotlin.math.max

class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    companion object {
        const val ITEM = "item"
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

        // DataList -> MainActivity 에서 받아옴
        val item = intent.getParcelableExtra<Item>(ITEM)

        val format = DecimalFormat("#,###")
        val price = item?.dPrice

        // 그 받아온 DataList 에서 값을 꺼내서 각각의 위젯에 적용
        binding.ivDetailImage.setImageResource(item?.dImage ?: 0)
        binding.tvDetailName.text = item?.dName
        binding.tvDetailAddress.text = item?.dAddress
        binding.tvDetailTitle.text = item?.dItemText
        binding.tvDetailMessage.text = item?.dMessage
        binding.tvDetailPrice.text = "${format.format(price)}원"

        // Main에서 좋아요가 빈 하트인 상태로(false) 넘어오면 기본 하트 상태는 빈 하트
        // 빨간 하트 상태로(true) 넘어오면 기본 하트 상태도 빨간 하트
        if (!item!!.dHeartCheck) {
            binding.ivBottomHeart.setImageResource(R.drawable.heart)
        } else {
            binding.ivBottomHeart.setImageResource(R.drawable.heart_full)
        }

        // 좋아요 버튼을 눌렀을 때 좋아요 버튼 이미지, 좋아요 수를 결정 하는 부분
        binding.ivBottomHeart.setOnClickListener { view ->
            if (!item.dHeartCheck) {                                                                        // 처음 상태 -> 이미지 누르면 빨간 하트 이미지로 변환, 좋아요 수 + 1
                binding.ivBottomHeart.setImageResource(R.drawable.heart_full)
                item.dHeartCheck = true
                item.dHeart++
                Snackbar.make(view, "관심 목록에 추가\n좋아요 개수 : ${item.dHeart}", Snackbar.LENGTH_SHORT).show()
            } else {                                                                                         // 빨간 하트 상태 -> 이미지 누르면 빈 하트 이미지로 변환, 좋아요 수 -1
                binding.ivBottomHeart.setImageResource(R.drawable.heart)
                item.dHeartCheck = false
                item.dHeart--
                Snackbar.make(view, "관심 목록에서 제거\n좋아요 개수 : ${item.dHeart}", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 이거는 보너스. Detail Page의 tvDetailMessage가 3줄 이상일 때 더보기 버튼 보이고, 3줄 미만이면 숨기기
        // 왜 그런 건지는 잘 모르겠지만 이 코드를 써 주어야 더보기/접기 버튼이 잘 동작한다.
        // 근데 이거 3줄 미만일 때는 더보기 버튼 숨겨져야 하는데 실행을 시키면 더보기 버튼 보여진다..? 분명 기본 설정 GONE인데..? 참 이상하다.
        // 일정 줄 수 미만인 경우는 더보기 버튼이 아예 안 보여야 하는데... 참 어렵다..
        binding.tvDetailMessage.post {
            if (binding.tvDetailMessage.maxLines >= 3) binding.tvShowDetailButton.visibility = View.VISIBLE
        }

        // 더보기 버튼 눌렀는지 확인하기 위한 변수
        var checkDetail = false

        // 더보기 버튼 눌렀을 때 설정
        binding.tvShowDetailButton.setOnClickListener {
            checkDetail = !checkDetail                                       // 이거 안 써주면 더보기 버튼은 보이지만 아예 안 눌리던데 왜 그런지는 모르겠다.. ㅋ
            if (checkDetail) {                                               // 더보기 버튼 누른 상태
                binding.tvDetailMessage.maxLines = Int.MAX_VALUE
                binding.tvShowDetailButton.text = "접기"
            } else {                                                         // 접기 버튼 누른 상태
                binding.tvDetailMessage.maxLines = 3
                binding.tvShowDetailButton.text = "더보기"
            }
        }

        // 뒤로 가기 버튼 누르면 바뀐 DataList(= item) 들고 MainPage로 이동 ("MainActivity로 돌아간다"는 표현이 더 적절할 듯)
        // StartActivity를 안 써주는 이유는 이미 Main이 실행되고 거기서 이 Detail Page로 넘어왔기 때문에... Main을 한 번 더 실행 시킬 필요가 없어서 그렇다.
        // 그냥 뒤(= MainActivity)로 가기만 하면 된다. 그래서 RegisterForActivityResult가 있는 것이다.
        binding.ivBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(ITEM, item)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}