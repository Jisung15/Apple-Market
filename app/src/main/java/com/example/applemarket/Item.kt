package com.example.applemarket

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/// 데이터 클래스 (이미지, 아이템 이름, 주소, 가격, 채팅 수, 좋아요 수, 이름, 메세지, 좋아요 클릭 여부 등이 담겨 있음)
@Parcelize
data class Item(
    val dImage: Int,
    val dItemText: String,
    val dAddress: String,
    val dPrice: String,
    val dChat: Int,
    var dHeart: Int,
    val dName: String,
    val dMessage: String,
    var dHeartCheck: Boolean = false
) : Parcelable
