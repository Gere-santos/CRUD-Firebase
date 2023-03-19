package com.example.crud.crud.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.crud.R
import com.example.crud.crud.helper.BaseFragment
import com.example.crud.crud.helper.FirebaseHelper
import com.example.crud.crud.helper.initToolbar
import com.example.crud.crud.model.Task
import com.example.crud.databinding.FragmentFormTaskBinding
import com.example.crud.databinding.FragmentTodoBinding


class FormTaskFragment : BaseFragment() {
    private val args: FormTaskFragmentArgs by navArgs()
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var newTask: Boolean = true
    private var statusTask: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolBar)
        initListeners()
        getArgs()
    }

    private fun getArgs(){
        args.task.let {
            if (it != null){
                task = it
                configTask()

            }
        }
    }

    private fun configTask(){
        newTask = false
        statusTask = task.status
        binding.textToolBar.text = "Editando Tarefa"
        binding.editDescription.setText(task.description)
        setStatus()
    }
    private fun setStatus(){
        binding.radioGroup.check(
            when(task.status){
                0 -> {
                    R.id.rbTodo
                }
                1 -> {
                    R.id.rbDoing
                }
                else -> {
                    R.id.rbDone
                }
            }
        )
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener { validateTask() }
        binding.radioGroup.setOnCheckedChangeListener{_, id ->
        statusTask = when(id){
            R.id.rbTodo -> 0
            R.id.rbDoing -> 1
            else -> 2
        }
        }
    }

    private fun validateTask(){
        val description = binding.editDescription.text.toString().trim()
        if (description.isNotEmpty()){
            hideKeyboard()
            binding.progressBar.isVisible = true

            if (newTask) task = Task()
                task.description = description
                task.status = statusTask

            saveTask()
        }else{
            Toast.makeText(requireContext(), "Preencha a tarefa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveTask(){
    FirebaseHelper
        .getDataBase()
        .child("task")
        .child(FirebaseHelper.getIdUser() ?: "")
        .child(task.id)
        .setValue(task)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                if (newTask){
                    findNavController().popBackStack()
                    Toast.makeText(requireContext(), "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show()
                }else{
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), "Tarefa atualizada com sucesso", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            binding.progressBar.isVisible = false
            Toast.makeText(requireContext(), "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}