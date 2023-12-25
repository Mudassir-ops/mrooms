package com.example.janberktaskmudassirsatti.ui.fragments.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.adapters.ScreenshotsAdapter
import com.example.janberktaskmudassirsatti.ads.isAdLoaded
import com.example.janberktaskmudassirsatti.ads.loadInterAd
import com.example.janberktaskmudassirsatti.ads.showInterAd
import com.example.janberktaskmudassirsatti.databinding.FragmentHomeBinding
import com.example.janberktaskmudassirsatti.models.ImageModel
import com.example.janberktaskmudassirsatti.models.ScreenshotViewsDataModel
import com.example.janberktaskmudassirsatti.service.MyScreenShootServiceJanBerk
import com.example.janberktaskmudassirsatti.utill.ActionType
import com.example.janberktaskmudassirsatti.utill.AllKotlinCallBacks.listener
import com.example.janberktaskmudassirsatti.utill.AppConstants.TAG
import com.example.janberktaskmudassirsatti.utill.DataState
import com.example.janberktaskmudassirsatti.utill.checkPermissionGranted
import com.example.janberktaskmudassirsatti.utill.getImgUri
import com.example.janberktaskmudassirsatti.utill.gone
import com.example.janberktaskmudassirsatti.utill.invisible
import com.example.janberktaskmudassirsatti.utill.isServiceRunning
import com.example.janberktaskmudassirsatti.utill.multiPermission
import com.example.janberktaskmudassirsatti.utill.permissionArray
import com.example.janberktaskmudassirsatti.utill.shareImage
import com.example.janberktaskmudassirsatti.utill.show
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val activityResultLaunch: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (!Settings.canDrawOverlays(context ?: return@registerForActivityResult)) {
            Log.e(TAG, "Still Not Allowed ")
        } else {
            if (context?.checkPermissionGranted(permissionArray) == true) {
                val isForegroundServiceRunning = isServiceRunning(
                    context ?: return@registerForActivityResult,
                    MyScreenShootServiceJanBerk::class.java
                )
                if (!isForegroundServiceRunning) {
                    ContextCompat.startForegroundService(
                        context ?: return@registerForActivityResult,
                        Intent(context, MyScreenShootServiceJanBerk::class.java)
                    )
                }
            } else {
                context?.multiPermission(permissionArray = permissionArray,
                    onMultiPermissionGranted = {
                        listener?.invoke()
                        val isForegroundServiceRunning = isServiceRunning(
                            context ?: return@multiPermission,
                            MyScreenShootServiceJanBerk::class.java
                        )
                        if (!isForegroundServiceRunning) {
                            ContextCompat.startForegroundService(
                                context ?: return@multiPermission,
                                Intent(context, MyScreenShootServiceJanBerk::class.java)
                            )
                        }
                    },
                    onMultiPermissionDenied = {
                        binding?.permissionLayout?.show()
                    },
                    onMultiPermissionError = {
                        binding?.permissionLayout?.show()
                    })
            }
        }

    }
    private var dataList = ArrayList<ScreenshotViewsDataModel>()
    private val screenshotsAdapter: ScreenshotsAdapter by lazy {
        ScreenshotsAdapter(callback = { it, actionType ->
            when (actionType) {
                ActionType.ACTION_OPEN -> {
                    /** Here when screen navigate interstitial ad shown each time*/
                    activity?.showInterAd()
                    val action = it.displayName?.let { it1 ->
                        HomeFragmentDirections.actionNavigationHomeToNavigationViewScreenshot(
                            it.imagePath, it1
                        )
                    }
                    if (findNavController().currentDestination?.id == R.id.navigation_home) {
                        action?.let {
                            findNavController().navigate(it)
                        }
                    }
                }

                ActionType.ACTION_SHARE -> {
                    context?.shareImage(
                        imagePath = it.imagePath, shareTitle = "Enjoy Your Screenshot"
                    )
                }

                else -> {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        deleteImage(imagePath = it.imagePath)
                    } else {
                        deleteImageBelowQ(imagePath = it.imagePath)
                    }
                }
            }

        }, context = requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.loadInterAd()
        listener = {
            viewModel.fetchAllScreenShots()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Before starting Foreground Service  I need Post notification permission for 13 above and Manage overlay s to launch Activity for screenshot on notification click */
        if (!Settings.canDrawOverlays(context ?: return)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context?.packageName}")
            )
            activityResultLaunch.launch(intent)
        } else {
            if (context?.checkPermissionGranted(permissionArray) == true) {
                val isForegroundServiceRunning = isServiceRunning(
                    context ?: return, MyScreenShootServiceJanBerk::class.java
                )
                if (!isForegroundServiceRunning) {
                    ContextCompat.startForegroundService(
                        context ?: return, Intent(context, MyScreenShootServiceJanBerk::class.java)
                    )
                }
            } else {
                context?.multiPermission(permissionArray = permissionArray,
                    onMultiPermissionGranted = {
                        binding?.rvScreenshots?.show()
                        val isForegroundServiceRunning = isServiceRunning(
                            context ?: return@multiPermission,
                            MyScreenShootServiceJanBerk::class.java
                        )
                        if (!isForegroundServiceRunning) {
                            ContextCompat.startForegroundService(
                                context ?: return@multiPermission,
                                Intent(context, MyScreenShootServiceJanBerk::class.java)
                            )
                        }
                    },
                    onMultiPermissionDenied = {
                        binding?.permissionLayout?.show()
                    },
                    onMultiPermissionError = {
                        binding?.permissionLayout?.show()
                    })
            }
        }
        binding?.swipeToRefresh?.setOnRefreshListener(this)
        viewModel.screenShotDataState.observe(viewLifecycleOwner) { dataState ->
            dataList.clear()
            when (dataState) {
                is DataState.Success<List<ImageModel>> -> {
                    if (dataState.data.isNotEmpty()) {
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
                        binding?.apply {
                            val mLayoutManager = GridLayoutManager(context, 3)
                            mLayoutManager.spanSizeLookup =
                                object : GridLayoutManager.SpanSizeLookup() {
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
                        binding?.noDataLayout?.gone()
                        binding?.loadingIndicator?.gone()
                        binding?.rvScreenshots?.show()
                        screenshotsAdapter.setData(dataList)
                    } else {
                        binding?.loadingIndicator?.gone()
                        binding?.rvScreenshots?.invisible()
                        binding?.ivNoData?.show()
                    }
                }

                is DataState.Error -> {
                    binding?.rvScreenshots?.invisible()
                    binding?.loadingIndicator?.gone()
                    binding?.ivNoData?.show()
                }

                is DataState.Loading -> {
                    binding?.ivNoData?.gone()
                    binding?.rvScreenshots?.invisible()
                    binding?.loadingIndicator?.show()
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isAdLoaded = false
    }


    private fun deleteImage(imagePath: String) {
        val uris = arrayListOf<Uri?>()
        val uriOfCurrentFile = context?.getImgUri(imagePath)
        if (uriOfCurrentFile != null) {
            uris.add(uriOfCurrentFile)
        }
        if (uris.size > 0) {
            val intentSender = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    activity?.contentResolver?.let {
                        MediaStore.createDeleteRequest(
                            it, uris
                        ).intentSender
                    }
                }

                else -> null
            }
            intentSender?.let { sender ->
                intentSenderLauncher.launch(
                    IntentSenderRequest.Builder(sender).build()
                )
            }
        }
    }

    private fun deleteImageBelowQ(imagePath: String) {
        val file = File(imagePath)
        if (file.exists()) {
            val isDeleted = file.delete()
            if (isDeleted) {
                listener?.invoke()
                findNavController().navigateUp()
            }
        }
    }

    private val intentSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    listener?.invoke()
                    findNavController().navigateUp()
                }
            }
        }

    override fun onRefresh() {
        binding?.swipeToRefresh?.isRefreshing = false
        listener?.invoke()
    }
}

