package com.example.fragmentos.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fragmentos.R
import com.example.fragmentos.TransporteApplication
import com.example.fragmentos.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        val application = requireActivity().application as TransporteApplication
        LoginViewModelFactory(application.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result.success) {
                // Navega para a tela principal após o login
                findNavController().navigate(R.id.action_nav_login_to_nav_home)
            } else {
                Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Ação do botão corrigida
        binding.buttonRegister.setOnClickListener {
            // Navega para a nova tela de registro
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}