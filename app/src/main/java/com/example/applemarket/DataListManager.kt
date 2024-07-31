package com.example.applemarket

import android.content.Context

class DataListManager (appContext: Context) {
    private val dataList = mutableListOf<Item>()
//    fun dataList(context: Context) : MutableList<Item> {
    init {
            dataList.add(
                Item(
                R.drawable.sample1,
                appContext.getString(R.string.sample1_title),
                appContext.getString(R.string.sample1_address),
                1000,
                25,
                13,
                appContext.getString(R.string.sample_1_name),
                appContext.getString(R.string.sample_1_message)
            ))
            dataList.add(
                Item(
                R.drawable.sample2,
                appContext.getString(R.string.sample2_title),
                appContext.getString(R.string.sample2_address),
                20000,
                28,
                8,
                appContext.getString(R.string.sample_2_name),
                appContext.getString(R.string.sample_2_message)
            ))
            dataList.add(
                Item(
                R.drawable.sample3,
                appContext.getString(R.string.sample3_title),
                appContext.getString(R.string.sample3_address),
                10000,
                5,
                23,
                appContext.getString(R.string.sample_3_name),
                appContext.getString(R.string.sample_3_message)
            ))
            dataList.add(
                Item(
                R.drawable.sample4,
                appContext.getString(R.string.sample4_title),
                appContext.getString(R.string.sample4_address),
                10000,
                17,
                14,
                appContext.getString(R.string.sample_4_name),
                appContext.getString(R.string.sample_4_message)
            ))
            dataList.add(
                Item(
                R.drawable.sample5,
                appContext.getString(R.string.sample5_title),
                appContext.getString(R.string.sample5_address),
                150000,
                9,
                22,
                appContext.getString(R.string.sample_5_name),
                appContext.getString(R.string.sample_5_message)
            ))
            dataList.add(
                Item(
                R.drawable.sample6,
                appContext.getString(R.string.sample6_title),
                appContext.getString(R.string.sample6_address),
                50000,
                16,
                25,
                appContext.getString(R.string.sample_6_name),
                appContext.getString(R.string.sample_6_message)
            ))
            dataList.add(
                Item(
                R.drawable.sample7,
                appContext.getString(R.string.sample7_title),
                appContext.getString(R.string.sample7_address),
                150000,
                54,
                142,
                appContext.getString(R.string.sample_7_name),
                appContext.getString(R.string.sample_7_message)
            ))
            dataList.add(Item(
                R.drawable.sample8,
                appContext.getString(R.string.sample8_title),
                appContext.getString(R.string.sample8_address),
                180000,
                7,
                31,
                appContext.getString(R.string.sample_8_name),
                appContext.getString(R.string.sample_8_message)
            ))
            dataList.add(Item(
                R.drawable.sample9,
                appContext.getString(R.string.sample9_title),
                appContext.getString(R.string.sample9_address),
                30000,
                28,
                7,
                appContext.getString(R.string.sample_9_name),
                appContext.getString(R.string.sample_9_message)
            ))
            dataList.add(Item(
                R.drawable.sample10,
                appContext.getString(R.string.sample10_title),
                appContext.getString(R.string.sample10_address),
                190000,
                6,
                40,
                appContext.getString(R.string.sample_10_name),
                appContext.getString(R.string.sample_10_message)
            )
        )
    }

    fun getDataList(): List<Item> = dataList

    /*fun addItem(item: Item) {
        dataList.add(item)
    }

    fun removeItem(item: Item) {
        dataList.remove(item)
    }

    fun updateItem(updatedItem: Item) {
        val index = dataList.indexOfFirst { it.dItemText == updatedItem.dItemText }
        if (index != -1) {
            dataList[index] = updatedItem
        }
    }*/

    companion object {
        @Volatile
        private var INSTANCE: DataListManager? = null

        fun getInstance(context: Context): DataListManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataListManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}