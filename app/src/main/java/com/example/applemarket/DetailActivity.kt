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

        val item = intent.getParcelableExtra<Item>(ITEM)

        binding.ivDetailImage.setImageResource(item?.dImage ?: 0)
        binding.tvDetailName.text = item?.dName
        binding.tvDetailAddress.text = item?.dAddress
        binding.tvDetailTitle.text = item?.dItemText
        binding.tvDetailMessage.text = item?.dMessage
        binding.tvDetailPrice.text = item?.dPrice

        var changeImage = true
        var count: Int = item?.dHeart ?: 0

        binding.ivBottomHeart.setOnClickListener { view ->
            if (changeImage) {
                binding.ivBottomHeart.setImageResource(R.drawable.heart_full)
                changeImage = false
                count++
                item?.dHeart = item?.dHeart!! + 1
                Snackbar.make(view, "관심 목록에 추가되었습니다.\n좋아요 개수 : ${count}", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                binding.ivBottomHeart.setImageResource(R.drawable.heart)
                changeImage = true
                count--
                Snackbar.make(view, "관심 목록에서 제거되었습니다.\n좋아요 개수 : ${count}", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        binding.ivBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(ITEM, item)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}