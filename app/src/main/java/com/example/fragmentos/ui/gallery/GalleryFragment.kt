package com.example.fragmentos.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentos.databinding.FragmentGalleryBinding
import androidx.navigation.fragment.findNavController
import com.example.fragmentos.R


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

// 1. Encontre o botão pelo binding

        binding.imageButtonAluno.setOnClickListener {

// 2. Use findNavController() para obter o controlador

// 3. Chame navigate() passando o ID da AÇÃO que você criou no XML

            findNavController().navigate(R.id.action_nav_gallery_to_alunoFragment)

        }

// Repita para os outros botões

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
}