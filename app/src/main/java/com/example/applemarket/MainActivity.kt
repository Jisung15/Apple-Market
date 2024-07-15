package com.example.applemarket

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dataList by lazy {mutableListOf<Item>()}
    private val adapter by lazy { MyAdapter(dataList) }

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
                getString(R.string.sample_7_message),
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
                getString(R.string.sample_9_message),
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

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        val resultValue =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val item = result.data?.getParcelableExtra<Item>(ITEM)
                    updateItem(dataList, item!!)
                    adapter.notifyDataSetChanged()
                }
            }

        adapter.click = object : MyAdapter.OnClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(ITEM, dataList[position])
                resultValue.launch(intent)
            }
        }

        adapter.click2 = object : MyAdapter.LongClick {
            override fun onLongClick(view: View, position: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("삭제")
                builder.setMessage("정말로 이 상품을 삭제 하시겠습니까?")
                builder.setIcon(R.mipmap.ic_launcher)

                val listener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            Toast.makeText(
                                this@MainActivity,
                                "${dataList[position].dItemText} 삭제 완료",
                                Toast.LENGTH_SHORT
                            ).show()
                            dataList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyDataSetChanged()
                            if (dataList.isEmpty()) {
                                binding.recyclerView.visibility = View.GONE
                                binding.tvEmpty.visibility = View.VISIBLE
                            } else {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.tvEmpty.visibility = View.GONE
                            }
                        }

                        DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()
                    }
                }

                builder.setPositiveButton("예", listener)
                builder.setNegativeButton("아니오", listener)

                builder.show()
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.ivMainTitleAlarm.setOnClickListener {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val builder: NotificationCompat.Builder
            val channelId = "channel"
            val channelName = "My Channel"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channelId)

            builder.setSmallIcon(R.mipmap.ic_launcher)
            builder.setWhen(System.currentTimeMillis())
            builder.setContentTitle("키워드 알림")
            builder.setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")

            manager.notify(11, builder.build())
        }

        val floatingButton = binding.floatingButton
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!binding.recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatingButton.startAnimation(fadeOut)
                    floatingButton.visibility = View.GONE
                    isTop = true
                } else {
                    if (isTop) {
                        floatingButton.visibility = View.VISIBLE
                        floatingButton.startAnimation(fadeIn)
                        floatingButton.setOnClickListener {
                            binding.recyclerView.smoothScrollToPosition(0)
                        }
                        isTop = false
                    }
                }
            }
        })
    }

    private fun updateItem(dataList: MutableList<Item>, newItem: Item) {
        for (i in dataList.indices) {
            if (dataList[i].dItemText == newItem.dItemText) {
                dataList[i].dHeart = newItem.dHeart
            }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("종료")
        builder.setMessage("정말 Apple Market을 종료하시겠습니까?")
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> finish()
                DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()
            }
        }

        builder.setPositiveButton("예", listener)
        builder.setNegativeButton("아니오", listener)
        builder.setOnCancelListener {}

        builder.show()

    }
}
