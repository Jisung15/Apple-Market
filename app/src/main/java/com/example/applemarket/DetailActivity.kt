package com.example.applemarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.applemarket.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    companion object { const val ITEM = "item" }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent.getParcelableExtra<Item>(ITEM)

        binding.ivDetailImage.setImageResource(intent?.dImage ?: 0)
        binding.tvDetailName.text = intent?.dName
        binding.tvDetailAddress.text = intent?.dAddress
        binding.tvDetailTitle.text = intent?.dTitle
        binding.tvDetailMessage.text = intent?.dMessage
        binding.tvPrice.text = intent?.dPrice

        binding.ivBackButton.setOnClickListener {
            finish()
        }
    }
}