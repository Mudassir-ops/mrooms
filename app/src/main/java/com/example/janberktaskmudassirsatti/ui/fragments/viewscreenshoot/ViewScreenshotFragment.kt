package com.example.janberktaskmudassirsatti.ui.fragments.viewscreenshoot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.janberktaskmudassirsatti.databinding.FragmentViewScreenshotBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewScreenshotFragment : Fragment() {

    private val viewModel by viewModels<ViewScreenshotViewModel>()
    private var _binding: FragmentViewScreenshotBinding? = null
    private val binding get() = _binding
    private val args: ViewScreenshotFragmentArgs by navArgs()

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
    }
}