package com.example.applemarket

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // DataList, RecyclerView Adapter에 대한 변수 선언
//    private val dataList by lazy { DataListManager.dataList(this) }
    private val adapter by lazy { MyAdapter(dataList) }
    private val dataListManager by lazy { DataListManager.getInstance(this) }
    private val dataList by lazy { dataListManager.getDataList().toMutableList() }

    // intent를 사용하기 위해 선언하는 변수
    companion object {
        const val EXTRA_ITEM = "item"
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

        // Adapter, RecyclerView 연결
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // 이건 구분선 넣는 코드
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // DetailPage 에서 바뀐 DataList를 받음. 이미 MainPage를 실행 해서 DetailPage를 불렀다. 그 상태에서 한 번 더 Main을 실행하는 건 낭비이다, 그래서 RegisterForActivityResult 사용
        val resultValue =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val item = result.data?.getParcelableExtra<Item>(EXTRA_ITEM)
                    if (item != null) {
                        updateItem(dataList, item)
                        adapter.notifyDataSetChanged()         // Adapter 모든 데이터 업데이트
                    }
                }
            }

        // 여기는 MainPage의 DataList를 DetailPage로 보내는 부분
        adapter.click = object : MyAdapter.OnClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_ITEM, dataList[position])                                          // 원본 데이터 리스트를 보냄
                resultValue.launch(intent)
            }
        }

        // 아이템 길게 눌러 삭제 하는 부분
        adapter.longClick = object : MyAdapter.LongClick {
            override fun onLongClick(view: View, position: Int) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle(R.string.delete_dialog_title)
                builder.setMessage(R.string.delete_text)
                builder.setIcon(R.mipmap.ic_launcher)

                val listener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {                          // 예 버튼 눌렀을 때
                            Toast.makeText(this@MainActivity,
                                "${dataList[position].dItemText} 삭제 완료",
                                Toast.LENGTH_SHORT).show()                              // 아이템을 잘 맞게 삭제했는지에 대한 여부를 토스트 메세지로 확인을 시켜 주는 부분

                            dataList.removeAt(position)                                 // 데이터 리스트에서 해당 포지션에 맞는 아이템을 지움
//                            adapter.notifyItemRemoved(position)                         // 어댑터의 아이템도 해당되는 포지션에 맞는 걸로 지움
                            adapter.notifyDataSetChanged()                              // 삭제 하고 나면 전체 데이터 업데이트

                            binding.tvEmpty.isVisible = dataList.isEmpty()

//                            if (dataList.isEmpty()) {                                   // 모든 아이템이 삭제 되면 "모든 항목이 삭제되었습니다"라고 써있는 TextView 보이게 설정
//                                binding.tvEmpty.visibility = View.VISIBLE
//                            } else {
//                                binding.tvEmpty.visibility = View.GONE
//                            }
                        }

                        // 아니오 버튼 누르면 그냥 다이얼 로그만 종료
                        DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()
                    }
                }

                builder.setPositiveButton(R.string.dialog_yes_button, listener)
                builder.setNegativeButton(R.string.dialog_no_button, listener)

                builder.show()
            }
        }

        // 알림 설정 하는 부분.. 여기는 아직 더 공부가 필요하다.
        // 알림 소리 안 들리게 하려면 어떻게 해야 할까요..?
        binding.ivMainTitleAlarm.setOnClickListener {
            alarm()
        }

        // floating button 설정 부분
        // 애니메이션도 각각 설정을 해 준다.
        val floatingButton = binding.floatingButton
        val inAnimation = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val outAnimation = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var top = true

        // RecyclerView가 스크롤 되었을 때 설정이다.
        with(binding) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!binding.recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {    // 스크롤이 멈춰 있고, 위치가 최상단일 경우. 항상 최상단부터 스크롤을 하니 이 상태가 기본이다.
                        floatingButton.startAnimation(outAnimation)                                                                // 페이드 아웃(= 점점 투명해진다) 적용
                        floatingButton.visibility =
                            View.GONE                                                                      // 버튼을 완전히 숨김
                        top = true

                    } else {                                                                                                       // 이제부터 스크롤 시작
                        if (top) {
                            floatingButton.startAnimation(inAnimation)                                                             // 페이드 인(= 점점 불투명해진다) 적용
                            floatingButton.visibility =
                                View.VISIBLE                                                               // top이 true일 때 스크롤을 한다. 그 때 버튼을 보이게 한다.
                            floatingButton.setOnClickListener {                                                                    // floating button 누르면 최상단으로 이동 (= 0번 position 위치로 이동을 한다)
                                recyclerView.smoothScrollToPosition(0)
                            }
                            top =
                                false                                                                                          // false로 top을 바꿈(= 최상단으로 올라가고, 스크롤이 멈추면 다시 true가 됨)
                        }
                    }
                }
            })
        }

        // 아이템을 추가할 때 쓸 Sample Data
        val newItem = Item(R.drawable.camp_icon, getString(R.string.item1_text), getString(R.string.item1_address), 0, 40, 1000, getString(R.string.item1_name), getString(R.string.item1_message))

        // 그 Sample Data(=Item 객체 형태의 데이터들)를 버튼 누르면 아이템에 추가하게 하고, 그 아이템(= newItem)을 RecyclerView 맨 위에 추가하는 코드
        // 버튼 넣을 공간이 없어서 타이틀의 아래 화살표 ImageView옆에 넣어두었습니다.
        // 아이템을 전부 삭제하고 추가 버튼을 누르면 빈 RecyclerView에 item1을 추가
        // notifyItemInserted가 그걸 도와준다... (처음 알았네요)
        binding.addButton.setOnClickListener {
            Toast.makeText(this, R.string.item_plus_text, Toast.LENGTH_SHORT).show()
            binding.tvEmpty.visibility = View.GONE
            dataList.add(0, newItem)
            adapter.notifyItemInserted(0)
            adapter.notifyDataSetChanged()
        }
    }

    private fun alarm () {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder

        // 채널 생성
        val channelId = "channel"
        val channelName = "My Channel"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT                                                    // 여기는 중요도 설정.. HIGH로 해도 상관은 없지만 일단 DEFAULT로 설정
        ).apply {
            description = "My Channel One Description"                                                // 이건 뭔지 모르겠다.. ㅋㅋㅋ
            setShowBadge(true)                                                                        // 배지 설정 (알림 하나씩 쌓일 때마다 아이콘 위에 숫자 뜨게)

            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)            // 알림 소리를 기본 소리로 설정

            // 그 기본 알림 소리의 속성을 설정
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            setSound(uri, audioAttributes)                                                             // 알림 소리를 uri와 audioAttributes 변수에 설정한 소리로 한다는 뜻(?)
            enableVibration(true)                                                              // 알림 올 때 진동 발생 여부 -> true
        }

        manager.createNotificationChannel(channel)                                                     // 만든 채널 등록
        builder = NotificationCompat.Builder(this, channelId)                                   // builder 생성

//        // builder 설정. 여기선 알림에 무슨 내용이 들어갈 지 정하는 것이다.
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setWhen(System.currentTimeMillis())                                                     // 알림 시간 -> 현재 시간 설정
        builder.setContentTitle("키워드 알림")
        builder.setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")                                          // 이건 왜 .toString()일까요..?

        manager.notify(11, builder.build())                                                          // ivMainTitleAlarm 버튼을 눌렀을 때 알림 실행
    }

    // 좋아요 개수, 이미지 업데이트 하는 부분
    // 데이터 리스트 안에서 찾는다. 좋아요 개수가 바뀐 아이템이 뭔지를 찾아서(= 제목은 바뀌지 않으니(직접 수정을 하지 않는 이상) 그걸로 찾는다)
    // 맞는 아이템을 찾으면 그 아이템의 좋아요 개수, 이미지를 업데이트 한다.
    // 근데 "dHeartCheck는 이미지가 아닌데?" 라는 의문을 가질 수 있지만 어댑터에서 이미지를 맞춰서 바꿔 준다. 걱정 No
    // Detail Page에서 했던 건 Detail Page의 하트 이미지를 바꾸는 것이고, 우리 Main Page의 하트 이미지는 어댑터에 있으니... ㅋㅋㅋ
    private fun updateItem(dataList: MutableList<Item>, newItem: Item) {
//        for (i in dataList.indices) {
//            if (dataList[i].dItemText == newItem.dItemText) {
//                dataList[i].dHeart = newItem.dHeart
//                dataList[i].dHeartCheck = newItem.dHeartCheck
//            }
//        }

        val item = dataList.find { it.dItemText == newItem.dItemText }
        item?.apply {
            dHeart = newItem.dHeart
            dHeartCheck = newItem.dHeartCheck
        }
    }

    // 뒤로 가기 버튼 눌렀을 때 다이얼 로그 설정
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.finish_dialog_title)
        builder.setMessage(R.string.finish_text)
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> finish()                                // 예 버튼 누르면 그냥 앱을 종료
                DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()                       // 아니오 버튼 누르면 다이얼 로그 종료
            }
        }

        builder.setPositiveButton(R.string.dialog_yes_button, listener)
        builder.setNegativeButton(R.string.dialog_no_button, listener)
        builder.setOnCancelListener {}                                                   // 다이얼 로그 띄우기 전에 앱이 종료 되지 않게 설정

        builder.show()
    }
}