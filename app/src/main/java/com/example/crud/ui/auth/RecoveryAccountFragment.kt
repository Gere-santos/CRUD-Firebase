package com.example.crud.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.crud.R
import com.example.crud.databinding.FragmentRecoveryAccountBinding
import com.example.crud.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoveryAccountFragment : Fragment() {
    private var _binding: FragmentRecoveryAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoveryAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initClicks()
    }

    private fun initClicks() {
        binding.btnRecovery.setOnClickListener {
            validateData()
        }

    }
    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()

        if (email.isNotEmpty()){
            binding.progressBar.isVisible = true
            recoveryAccountUser(email)
        }else{
            Toast.makeText(requireContext(), "Digite seu email", Toast.LENGTH_SHORT).show()
        }
    }
    private fun recoveryAccountUser(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Acabamos de enviar um link para o seu email", Toast.LENGTH_SHORT).show()

                }
                binding.progressBar.isVisible = false
            }
    }
}