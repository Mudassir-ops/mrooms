package com.example.janberktaskmudassirsatti.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.janberktaskmudassirsatti.Appconstants.DataState
import com.example.janberktaskmudassirsatti.adapters.DataAdapter
import com.example.janberktaskmudassirsatti.databinding.FragmentHomeBinding
import com.example.janberktaskmudassirsatti.models.ImageModel
import com.example.janberktaskmudassirsatti.service.MyScreenShootServiceJanBerk
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val dataAdapter: DataAdapter by lazy {
        DataAdapter()
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
            button.setOnClickListener {
                if (!Settings.canDrawOverlays(context ?: return@setOnClickListener)) {
                    val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(myIntent)
                }
            }
            dataAdapter.setData(arrayListOf())
        }

        viewModel.screenShotDataState.observe(viewLifecycleOwner) {
            it?.let { dataState ->
                when (dataState) {
                    is DataState.Success<ImageModel> -> {

                    }

                    is DataState.Error -> {

                    }

                    is DataState.Loading -> {

                    }

                    else -> {}
                }
            }
            Log.e("mudassirSatti", "onViewCreated: $it")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

