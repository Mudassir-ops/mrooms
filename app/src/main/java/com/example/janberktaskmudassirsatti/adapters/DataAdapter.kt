package com.example.janberktaskmudassirsatti.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.janberktaskmudassirsatti.R


class DataAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val adapterData = mutableListOf<DataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SCREENSHOT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.screenshoot_item_layout, parent, false)
                ScreenshotViewHolder(view)
            }

            TYPE_AD -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.native_ad_item_layout, parent, false)
                AdViewHolder(view)
            }

            else -> throw Exception("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_SCREENSHOT -> {
                val headerHolder = holder as ScreenshotViewHolder
                headerHolder.bindData(adapterData[position] as DataModel.TemplatesScreenshot)
            }

            TYPE_AD -> {
                val dataHolder = holder as AdViewHolder
                dataHolder.bindData(adapterData[position] as DataModel.TemplatesAd)
            }
        }
    }

    override fun getItemCount(): Int = adapterData.size

    override fun getItemViewType(position: Int): Int {
        return when (adapterData[position]) {
            is DataModel.TemplatesScreenshot -> TYPE_SCREENSHOT
            is DataModel.TemplatesAd -> TYPE_AD
        }
    }

    fun setData(data: List<DataModel>) {
        adapterData.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_SCREENSHOT = 0
        private const val TYPE_AD = 1
    }

    inner class ScreenshotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: DataModel.TemplatesScreenshot) {}
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: DataModel.TemplatesAd) {
            Log.e("Mudassir", "bindData: ${item.dataList}")
        }
    }
}

