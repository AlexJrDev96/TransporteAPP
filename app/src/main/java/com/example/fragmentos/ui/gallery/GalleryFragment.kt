package com.example.fragmentos.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragmentos.R
import com.example.fragmentos.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonAluno.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_alunoFragment)
        }

        binding.imageButtonResponsavel.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_responsavelFragment)
        }

        binding.imageButtonTripulante.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_tripulanteFragment)
        }

        binding.imageButtonTurma.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_turmaFragment2)
        }

        binding.imageButtonEscola.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_escolaFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}