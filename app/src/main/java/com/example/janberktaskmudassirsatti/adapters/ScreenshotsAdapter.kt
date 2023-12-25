package com.example.janberktaskmudassirsatti.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.janberktaskmudassirsatti.ads.loadNativeAd
import com.example.janberktaskmudassirsatti.ads.populateNativeAdView
import com.example.janberktaskmudassirsatti.databinding.CustomNativeViewBinding
import com.example.janberktaskmudassirsatti.databinding.NativeAdItemLayoutBinding
import com.example.janberktaskmudassirsatti.databinding.ScreenshootItemLayoutBinding
import com.example.janberktaskmudassirsatti.models.ScreenshotViewsDataModel
import com.example.janberktaskmudassirsatti.utill.ActionType
import com.example.janberktaskmudassirsatti.utill.AppConstants.TYPE_AD
import com.example.janberktaskmudassirsatti.utill.AppConstants.TYPE_SCREENSHOT
import com.example.janberktaskmudassirsatti.utill.showPopupMenu

class ScreenshotsAdapter(
    private val callback: (ScreenshotViewsDataModel.TemplatesScreenshot, ActionType) -> Unit,
    private val context: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val adapterData = mutableListOf<ScreenshotViewsDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SCREENSHOT -> {
                val binding = ScreenshootItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ScreenshotViewHolder(binding)
            }

            else -> {
                val binding = NativeAdItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                AdViewHolder(binding)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_SCREENSHOT -> {
                val headerHolder = holder as ScreenshotViewHolder
                headerHolder.bindData(adapterData[position] as ScreenshotViewsDataModel.TemplatesScreenshot)
            }

            TYPE_AD -> {
                val dataHolder = holder as AdViewHolder
                dataHolder.bindData()
            }
        }
    }

    override fun getItemCount(): Int = adapterData.size

    override fun getItemViewType(position: Int): Int {
        return when (adapterData[position]) {
            is ScreenshotViewsDataModel.TemplatesScreenshot -> TYPE_SCREENSHOT
            is ScreenshotViewsDataModel.TemplatesAd -> TYPE_AD
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ScreenshotViewsDataModel>) {
        adapterData.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    inner class ScreenshotViewHolder(private val binding: ScreenshootItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: ScreenshotViewsDataModel.TemplatesScreenshot) {
            binding.item = item.imagePath
            binding.btnAction.setOnClickListener {
                it.showPopupMenu(callback = { action ->
                    callback.invoke(item, action)
                })
            }

        }
    }

    inner class AdViewHolder(private val binding: NativeAdItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData() {
            binding.apply {
                context.loadNativeAd { nativeAdLambda ->
                    val adBinding = CustomNativeViewBinding.inflate(context.layoutInflater)
                    nativeAdLambda?.let { populateNativeAdView(it, adBinding) }
                    nativeAd.removeAllViews()
                    nativeAd.addView(adBinding.root)
                }
            }
        }
    }
}

