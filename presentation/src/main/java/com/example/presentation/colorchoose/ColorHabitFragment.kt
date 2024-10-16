package com.example.presentation.colorchoose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.databinding.FragmentColorHabitBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColorHabitFragment : Fragment(R.layout.fragment_color_habit) {
    private var _binding: FragmentColorHabitBinding? = null
    private val binding get() = _binding!!
    private val colorViewModel: ColorViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setColorButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}