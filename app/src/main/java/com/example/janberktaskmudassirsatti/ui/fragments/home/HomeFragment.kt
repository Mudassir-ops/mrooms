package com.example.janberktaskmudassirsatti.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.adapters.ScreenshotsAdapter
import com.example.janberktaskmudassirsatti.databinding.FragmentHomeBinding
import com.example.janberktaskmudassirsatti.models.ImageModel
import com.example.janberktaskmudassirsatti.models.ScreenshotViewsDataModel
import com.example.janberktaskmudassirsatti.service.MyScreenShootServiceJanBerk
import com.example.janberktaskmudassirsatti.utill.ActionType
import com.example.janberktaskmudassirsatti.utill.DataState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private var dataList = ArrayList<ScreenshotViewsDataModel>()
    private val screenshotsAdapter: ScreenshotsAdapter by lazy {
        ScreenshotsAdapter(callback = { it, actionType ->

            when (actionType) {
                ActionType.ACTION_OPEN -> {
                    val action =
                        it.displayName?.let { it1 ->
                            HomeFragmentDirections.actionNavigationHomeToNavigationViewScreenshot(
                                it.imagePath,
                                it1
                            )
                        }
                    if (findNavController().currentDestination?.id == R.id.navigation_home) {
                        action?.let {
                            findNavController().navigate(it)
                        }
                    }
                }

                ActionType.ACTION_SHARE -> {

                }

                else -> {

                }
            }

        }, context = requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ContextCompat.startForegroundService(
            context ?: return, Intent(context, MyScreenShootServiceJanBerk::class.java)
        )
        binding?.apply {
            val mLayoutManager = GridLayoutManager(context, 3)
            mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (dataList[position]) {
                        is ScreenshotViewsDataModel.TemplatesAd -> 3
                        else -> 1
                    }
                }
            }
            rvScreenshots.run {
                adapter = screenshotsAdapter
                layoutManager = mLayoutManager
                hasFixedSize()
            }
        }
        viewModel.screenShotDataState.observe(viewLifecycleOwner) { dataState ->
            dataList.clear()
            when (dataState) {
                is DataState.Success<List<ImageModel>> -> {
                    dataState.data.mapIndexed { index, imageModel ->
                        dataList.add(
                            ScreenshotViewsDataModel.TemplatesScreenshot(
                                imageModel.contentUri,
                                imagePath = imageModel.imagePath,
                                displayName = imageModel.displayName
                            )
                        )
                        if ((index + 1) % 6 == 0) {
                            dataList.add(ScreenshotViewsDataModel.TemplatesAd(0))
                        }
                    }
                    screenshotsAdapter.setData(dataList)
                }

                is DataState.Error -> {

                }

                is DataState.Loading -> {

                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

