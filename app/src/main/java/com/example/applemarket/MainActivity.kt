package com.example.applemarket

import android.annotation.SuppressLint
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

    // intent를 사용하기 위해 선언하는 변수
    companion object {
        const val ITEM = "item"
    }

    @SuppressLint("NotifyDataSetChanged")
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
                1000,
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
                20000,
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
                10000,
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
                10000,
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
                150000,
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
                50000,
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
                150000,
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
                180000,
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
                30000,
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
                190000,
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
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // DetailPage 에서 바뀐 DataList를 받음. 이미 MainPage를 실행 해서 DetailPage를 불렀다. 그 상태에서 한 번 더 Main을 실행하는 건 낭비이다, 그래서 RegisterForActivityResult 사용
        val resultValue =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val item = result.data?.getParcelableExtra<Item>(ITEM)
                    if (item != null) {
                        updateItem(dataList, item)
                        adapter.notifyDataSetChanged()         // Adapter 모든 데이터 업데이트
                    }

                    // 여기서 DetailActivity를 "다시" 실행하려고 했으나 굳이 그럴 필요가 없다는 결론에 도달 -> 이렇게 주석 처리
                    // 왜? 굳이 "한 번 더" 실행해야 할까? 아래 adapter.click 코드와 겹쳐서 Detail Page가 두 번 실행된다.
//                    val intent = Intent(this, DetailActivity::class.java)
//                    intent.putExtra(ITEM, item)
//                    startActivity(intent)
                }
            }

        // 여기는 MainPage의 DataList를 DetailPage로 보내는 부분
        adapter.click = object : MyAdapter.OnClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(ITEM, dataList[position])                                          // 원본 데이터 리스트를 보냄
                resultValue.launch(intent)
            }
        }

        // 아이템 길게 눌러 삭제 하는 부분
        adapter.longClick = object : MyAdapter.LongClick {
            override fun onLongClick(view: View, position: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("삭제")
                builder.setMessage("정말로 이 상품을 삭제 하시겠습니까?")
                builder.setIcon(R.mipmap.ic_launcher)

                val listener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {                          // 예 버튼 눌렀을 때
                            Toast.makeText(this@MainActivity,
                                "${dataList[position].dItemText} 삭제 완료",
                                Toast.LENGTH_SHORT).show()                             // 삭제한 아이템 목록을 토스트 메세지로 확인을 시켜 주는 부분

                            dataList.removeAt(position)                                 // 데이터 리스트에서 해당 포지션에 맞는 아이템을 지움
                            adapter.notifyItemRemoved(position)                        // 어댑터의 아이템(= 메인 페이지에서 스크롤 되는 아이템 목록 중 하나)도 해당되는 포지션에 맞는 걸로 지움
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
                description = "My Channel One Description"                                                // 이건 뭔지 모르겠다.. ㅋㅋㅋ
                setShowBadge(true)                                                                        // 배지 설정 (알림 하나씩 쌓일 때마다 아이콘 위에 숫자 뜨게)

                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)                // 알림 소리를 기본 소리로 설정

                // 그 기본 알림 소리의 속성을 설정
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)                                                             // 알림 소리를 uri와 audioAttributes 변수에 설정한 소리로 한다는 뜻
                enableVibration(true)                                                              // 알림 올 때 진동 발생 여부 -> true
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
        val inAnimation = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val outAnimation = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true

        // RecyclerView가 스크롤 되었을 때 설정이다.
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!binding.recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {    // 스크롤이 멈춰 있고, 위치가 최상단일 경우. 항상 최상단부터 스크롤을 하니 이 상태가 기본이다.
                    floatingButton.startAnimation(outAnimation)                                                                // 페이드 아웃(= 점점 투명해진다) 적용
                    floatingButton.visibility = View.GONE                                                                      // 버튼을 완전히 숨김
                    isTop = true

                } else {                                                                                                       // 이제부터 스크롤 시작
                    if (isTop) {
                        floatingButton.visibility = View.VISIBLE                                                           // isTop이 true일 때 스크롤을 한다. 그 때 버튼을 보이게 한다.
                        floatingButton.startAnimation(inAnimation)                                                         // 페이드 아웃의 반대 설정 적용
                        floatingButton.setOnClickListener {                                              // floating button 누르면 최상단으로 이동 (= 0번 position 위치로 이동을 한다)
                            binding.recyclerView.smoothScrollToPosition(0)
                        }
                        isTop = false                                                                    // false로 isTop을 바꿈(= 최상단으로 올라가고, 스크롤이 멈추면 다시 true가 됨)
                    }
                }
            }
        })

        // Sample Data
        val item1 = Item(R.drawable.camp_icon, getString(R.string.item1_text), getString(R.string.item1_adress), 0, 40, 1000, getString(R.string.item1_name), getString(R.string.item1_message))

        // 그 Sample Data를 버튼 누르면 추가하게 하는 코드
        binding.ivMainTitleImage.setOnClickListener {
            Toast.makeText(this, getString(R.string.add_toast_ext), Toast.LENGTH_SHORT).show()
            dataList.add(0, item1)
            adapter.notifyDataSetChanged()
        }
    }

    // 좋아요 개수, 이미지 업데이트 하는 부분
    // 데이터 리스트 안에서 찾는다.
    // 좋아요 개수가 바뀐 아이템이 뭔지를 찾아서(= 제목은 바뀌지 않으니(직접 수정을 하지 않는 이상) 그걸로 찾는다)
    // 맞는 아이템을 찾으면 그 아이템의 좋아요 개수, 이미지를 업데이트 한다.
    // 근데 "dHeartCheck는 이미지가 아닌데?" 라는 의문을 가질 수 있지만 어댑터에서 이미지를 맞춰서 바꿔 준다. 걱정 NoNo
    // Detail Page에서 했던 건 Detail Page의 하트 이미지를 바꾸는 것이고, 우리 Main Page의 하트 이미지는 어댑터에 있으니... ㅋㅋㅋ
    private fun updateItem(dataList: MutableList<Item>, newItem: Item) {
        for (i in dataList.indices) {
            if (dataList[i].dItemText == newItem.dItemText) {
                dataList[i].dHeart = newItem.dHeart
                dataList[i].dHeartCheck = newItem.dHeartCheck
            }
        }
    }

    // 뒤로 가기 버튼 눌렀을 때 다이얼 로그 설정
    // 왜 onBackPressed가 빨간 줄이 뜰까요..? 동작은 잘 되어서 상관은 없긴 하지만... ㅋㅋㅋ
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
