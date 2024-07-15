package com.example.applemarket

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val dImage: Int,
    val dItemText: String,
    val dAddress: String,
    val dPrice: String,
    val dChat: Int,
    var dHeart: Int,
    val dName: String,
    val dMessage: String
) : Parcelable
