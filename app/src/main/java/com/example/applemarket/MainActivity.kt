package com.example.applemarket

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    companion object {
        const val ITEM = "item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataList = mutableListOf<Item>()
        dataList.add(
            Item(
                R.drawable.sample1,
                getString(R.string.sample1_title),
                getString(R.string.sample1_address),
                getString(R.string.sample_1_price),
                25,
                13,
                getString(R.string.sample_1_name),
                getString(R.string.sample_1_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample2,
                getString(R.string.sample2_title),
                getString(R.string.sample2_address),
                getString(R.string.sample_2_price),
                28,
                8,
                getString(R.string.sample_2_name),
                getString(R.string.sample_2_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample3,
                getString(R.string.sample3_title),
                getString(R.string.sample3_address),
                getString(R.string.sample_3_price),
                5,
                23,
                getString(R.string.sample_3_name),
                getString(R.string.sample_3_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample4,
                getString(R.string.sample4_title),
                getString(R.string.sample4_address),
                getString(R.string.sample_4_price),
                17,
                14,
                getString(R.string.sample_4_name),
                getString(R.string.sample_4_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample5,
                getString(R.string.sample5_title),
                getString(R.string.sample5_address),
                getString(R.string.sample_5_price),
                9,
                22,
                getString(R.string.sample_5_name),
                getString(R.string.sample_5_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample6,
                getString(R.string.sample6_title),
                getString(R.string.sample6_address),
                getString(R.string.sample_6_price),
                16,
                25,
                getString(R.string.sample_6_name),
                getString(R.string.sample_6_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample7,
                getString(R.string.sample7_title),
                getString(R.string.sample7_address),
                getString(R.string.sample_7_price),
                54,
                142,
                getString(R.string.sample_7_name),
                getString(R.string.sample_7_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample8,
                getString(R.string.sample8_title),
                getString(R.string.sample8_address),
                getString(R.string.sample_8_price),
                7,
                31,
                getString(R.string.sample_8_name),
                getString(R.string.sample_8_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample9,
                getString(R.string.sample9_title),
                getString(R.string.sample9_address),
                getString(R.string.sample_9_price),
                28,
                7,
                getString(R.string.sample_9_name),
                getString(R.string.sample_9_message)
            )
        )
        dataList.add(
            Item(
                R.drawable.sample10,
                getString(R.string.sample10_title),
                getString(R.string.sample10_address),
                getString(R.string.sample_10_price),
                6,
                40,
                getString(R.string.sample_10_name),
                getString(R.string.sample_10_message)
            )
        )

        val adapter = MyAdapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))


        adapter.click = object : MyAdapter.OnClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(ITEM, dataList[position])
                startActivity(intent)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("종료")
        builder.setMessage("정말 종료하시겠습니까?")
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    finish()
                }
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog?.dismiss()
                }
            }
        }

        builder.setPositiveButton("예", listener)
        builder.setNegativeButton("아니오", null)
        builder.setOnCancelListener {}

        builder.show()

    }
}