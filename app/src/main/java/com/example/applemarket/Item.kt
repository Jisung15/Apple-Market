package com.example.applemarket

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(val dImage: Int,
    val dTitle: String,
    val dSubTitle: String,
    val dPrice: String,
    val dChat: Int,
    val dHeart: Int
) : Parcelable
