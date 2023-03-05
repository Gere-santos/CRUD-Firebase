package com.example.crud.crud.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.crud.crud.helper.FirebaseHelper
import com.example.crud.databinding.FragmentRecoveryAccountBinding
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
                }else{
                    Toast.makeText(requireContext(), FirebaseHelper.validError(task.exception?.message.toString()), Toast.LENGTH_SHORT).show()

                }
                binding.progressBar.isVisible = false
            }
    }
}