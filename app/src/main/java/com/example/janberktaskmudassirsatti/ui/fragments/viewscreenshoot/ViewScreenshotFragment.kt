package com.example.janberktaskmudassirsatti.ui.fragments.viewscreenshoot

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.janberktaskmudassirsatti.databinding.FragmentViewScreenshotBinding
import com.example.janberktaskmudassirsatti.utill.AllKotlinCallBacks.listener
import com.example.janberktaskmudassirsatti.utill.getImgUri
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ViewScreenshotFragment : Fragment() {

    private val viewModel by viewModels<ViewScreenshotViewModel>()
    private var _binding: FragmentViewScreenshotBinding? = null
    private val binding get() = _binding
    private val args: ViewScreenshotFragmentArgs by navArgs()
    private val intentSenderLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    listener?.invoke()
                    findNavController().navigateUp()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewScreenshotBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.viewModel = viewModel
        viewModel.imageURL.set(args.imageUri)
        viewModel.screenShotTitle.set(args.imageTitle)

        binding?.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnDelete.setOnClickListener {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    deleteImage(imagePath = args.imageUri)
                } else {
                    deleteImageBelowQ(imagePath = args.imageUri)
                }
            }
        }
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
}