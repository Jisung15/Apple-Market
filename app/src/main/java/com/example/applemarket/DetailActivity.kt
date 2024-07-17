package com.example.applemarket

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

        var format = DecimalFormat("#,###")
        var price = item?.dPrice

        // 그 받아온 DataList 에서 값을 꺼내서 각각의 위젯에 적용
        binding.ivDetailImage.setImageResource(item?.dImage ?: 0)
        binding.tvDetailName.text = item?.dName
        binding.tvDetailAddress.text = item?.dAddress
        binding.tvDetailTitle.text = item?.dItemText
        binding.tvDetailMessage.text = item?.dMessage
        binding.tvDetailPrice.text = "${format.format(price)}원"

        // 좋아요 개수 변경을 확인 하기 위한 변수
        var count: Int = item?.dHeart ?: 0          // 이건 좋아요 개수 인데 DetailPage에 표시하는 건 아니지만, 나중에 필요하니 여기로 따로 빼놓음

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
                count++
                item.dHeart = count                                                                          // 이거 때문에 많이 헤맸다.. 반드시 +1 하고 dHeart에 대입을 다시 해주어야 한다..? 그냥 count를 메인에 써주면 안 되나... 라고 생각 했는데... 이 count는 Detail Page의 변수라는 걸 까먹었다... ㅋㅋㅋ
                item.dHeartCheck = true
                Snackbar.make(view, "관심 목록에 추가\n좋아요 개수 : $count", Snackbar.LENGTH_SHORT).show()
            } else {                                                                                         // 빨간 하트 상태 -> 이미지 누르면 빈 하트 이미지로 변환, 좋아요 수 -1
                binding.ivBottomHeart.setImageResource(R.drawable.heart)
                count--
                item.dHeart = count
                item.dHeartCheck = false
                Snackbar.make(view, "관심 목록에서 제거\n좋아요 개수 : $count", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 이거는 보너스. Detail Page의 tvDetailMessage가 3줄 이상일 때 더보기 버튼 보이고, 3줄 미만이면 숨기기
        // 왜 그런 건지는 잘 모르겠지만 이 코드를 써 주어야 더보기/접기 버튼이 잘 동작한다.
        // 더보기 버튼의 숨김/보이기 설정은 Detail Page의 tvDetailMessage 텍스트 줄 수에 따라 결정된다.
        // 근데 이거 3줄 미만일 때는 더보기 버튼 숨겨져야 하는데 실행을 시키면 더보기 버튼 보여진다..? 분명 기본 설정 GONE인데..? 참 이상하다.
        // 이거 if문에 3줄 초과로 하면 긴 텍스트 같은 거 볼 때 더보기 버튼 아예 안 보인다.. ㅠㅠ
        // 일정 줄 수 미만인 경우는 더보기 버튼이 아예 안 보여야 하니... 참 어렵다..
        binding.tvDetailMessage.post {
            if (binding.tvDetailMessage.maxLines >= 3) binding.tvShowDetailButton.visibility = View.VISIBLE
        }

        // 더보기 버튼 눌렀는지 확인하기 위한 변수
        var checkDetail = false

        // 더보기 버튼 눌렀을 때 설정
        binding.tvShowDetailButton.setOnClickListener {
            checkDetail = !checkDetail                                       // 이거  안 써주면 에러 나던데 왜 그런지는 모르겠다.. ㅋ
            if (checkDetail) {
                binding.tvDetailMessage.maxLines = Int.MAX_VALUE
                binding.tvShowDetailButton.text = "접기"
            } else {
                binding.tvDetailMessage.maxLines = 3
                binding.tvShowDetailButton.text = "더보기"
            }
        }


        // 뒤로 가기 버튼 누르면 바뀐 DataList(= item) 들고 MainPage로 이동
        binding.ivBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(ITEM, item)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}