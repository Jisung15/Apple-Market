package com.example.applemarket

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

    // DataList, RecyclerView Adapter에 대한 변수 선언
    private val dataList by lazy { mutableListOf<Item>() }
    private val adapter by lazy { MyAdapter(dataList) }

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

        // DataList에 초기 데이터 원본을 추가
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

        // Adapter, RecyclerView 연결
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // 이건 구분선 넣는 코드
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        // DetailPage 에서 바뀐 DataList를 받음. 이미 MainPage를 실행 해서 DetailPage를 불렀다. 그 상태에서 한 번 더 Main을 실행하는 건 낭비이다, 그래서 RegisterForActivityResult 사용
        val resultValue =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val item = result.data?.getParcelableExtra<Item>(ITEM)
                    updateItem(dataList, item!!)
                    adapter.notifyDataSetChanged()         // Adapter 모든 데이터 업데이트
                }
            }

        // 여기는 MainPage의 DataList를 DetailPage로 보내는 부분
        adapter.click = object : MyAdapter.OnClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(ITEM, dataList[position])
                resultValue.launch(intent)
            }
        }

        // 아이템 길게 눌러 삭제 하는 부분
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
                            adapter.notifyDataSetChanged()                              // 삭제 하고 나면 전체 데이터 업데이트
                            if (dataList.isEmpty()) {                                  // 모든 아이템 삭제 되면 "모든 항목이 삭제되었습니다"라고 써있는 TextView 보이게 설정
                                binding.recyclerView.visibility = View.GONE
                                binding.tvEmpty.visibility = View.VISIBLE
                            } else {
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.tvEmpty.visibility = View.GONE
                            }
                        }

                        // 아니오 버튼 누르면 그냥 다이얼 로그만 종료
                        DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()
                    }
                }

                builder.setPositiveButton("예", listener)
                builder.setNegativeButton("아니오", listener)

                builder.show()
            }
        }

        // 알림 설정 하는 부분.. 여기는 아직 더 공부가 필요하다.
        binding.ivMainTitleAlarm.setOnClickListener {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            val builder: NotificationCompat.Builder

            // 채널 생성
            val channelId = "channel"
            val channelName = "My Channel"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT                   // 여기는 중요도 설정.. HIGH로 해도 상관은 없지만 일단 DEFAULT로 설정
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)                                                                        // 배지 설정 (알림 하나씩 쌓일 때마다 아이콘 위에 숫자 뜨게)

                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)                // 알림 소리를 기본 소리로 설정

                // 그 기본 알림 소리의 속성을 설정
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)                                                             // 알림 소리를 audioAttributes 변수에 설정한 소리로 한다는 뜻
                enableVibration(true)                                                              //     알림 올 때 진동 발생 여부 -> true

            }
            manager.createNotificationChannel(channel)                                                             // 만든 채널 등록
            builder = NotificationCompat.Builder(this, channelId)                                           // builder 생성

            // builder 설정. 여기선 알림에 무슨 내용이 들어갈 지 정하는 것이다.
            builder.setSmallIcon(R.mipmap.ic_launcher)
            builder.setWhen(System.currentTimeMillis())                                            // 알림 시간 -> 현재 시간 설정
            builder.setContentTitle("키워드 알림")
            builder.setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")

            manager.notify(11, builder.build())                                                // ivMainTitleAlarm 버튼을 눌렀을 때 알림 실행
        }

        // floating button 설정 부분
        // 애니메이션도 각각 설정을 해 준다.
        val floatingButton = binding.floatingButton
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true

        // RecyclerView가 스크롤 되었을 때 설정이다.
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!binding.recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {    // 스크롤이 멈춰 있고, 위치가 최상단일 경우
                    floatingButton.startAnimation(fadeOut)
                    floatingButton.visibility = View.GONE
                    isTop = true
                } else {
                    if (isTop) {
                        floatingButton.visibility = View.VISIBLE
                        floatingButton.startAnimation(fadeIn)
                        floatingButton.setOnClickListener {                                              // floating button 누르면 최상단으로 이동
                            binding.recyclerView.smoothScrollToPosition(0)
                        }
                        isTop = false
                    }
                }
            }
        })
    }

    // 좋아요 개수 업데이트 하는 부분
    // 좋아요 이미지 업데이트 하는 코드는 아직...
    private fun updateItem(dataList: MutableList<Item>, newItem: Item) {
        for (i in dataList.indices) {
            if (dataList[i].dItemText == newItem.dItemText) {
                dataList[i].dHeart = newItem.dHeart
            }
        }
    }

    // 뒤로 가기 버튼 눌렀을 때 다이얼 로그 설정
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("종료")
        builder.setMessage("정말 Apple Market을 종료하시겠습니까?")
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> finish()                                // 예 버튼 누르면 그냥 앱을 종료
                DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()                       // 아니오 버튼 누르면 다이얼 로그 종료
            }
        }

        builder.setPositiveButton("예", listener)
        builder.setNegativeButton("아니오", listener)
        builder.setOnCancelListener {}                                                   // 다이얼 로그 띄우기 전에 앱이 종료 되지 않게 설정

        builder.show()

    }
}
