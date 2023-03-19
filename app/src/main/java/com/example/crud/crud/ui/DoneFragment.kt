package com.example.crud.crud.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.R
import com.example.crud.crud.HomeFragmentDirections
import com.example.crud.crud.helper.BaseFragment
import com.example.crud.crud.helper.FirebaseHelper
import com.example.crud.crud.model.Task
import com.example.crud.crud.ui.adapter.TaskAdapter
import com.example.crud.databinding.FragmentDoingBinding
import com.example.crud.databinding.FragmentDoneBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class DoneFragment : BaseFragment() {
    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!
    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTasks()
    }

    private fun initAdapter(){
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), taskList){
                task , select ->
            optionSelected(task , select)
        }
        binding.rvTask.adapter = taskAdapter
    }
    private fun optionSelected(task: Task, select: Int){
        when(select){
            TaskAdapter.SELECT_REMOVE ->{
                deleteTask(task)
            }
            TaskAdapter.SELECT_EDIT ->{
                // passando o objeto task
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }
            TaskAdapter.SELECT_BACK ->{
                task.status = 1
                updateTask(task)
            }
        }
    }

    private fun updateTask(task: Task){
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(requireContext(), "Tarefa atualizada com sucesso", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                binding.progressBar.isVisible = false
                Toast.makeText(requireContext(), "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteTask(task: Task) {
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser()?: "")
            .child(task.id)
            .removeValue()

        taskList.remove(task)
        taskAdapter.notifyDataSetChanged()
    }

    private fun getTasks(){
        FirebaseHelper
            .getDataBase()
            .child("task")
            .child(FirebaseHelper.getIdUser()?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        taskList.clear()
                        for (snap in snapshot.children){
                            val task = snap.getValue(Task::class.java) as Task
                            if (task.status ==  2) taskList.add(task)
                        }

                        taskList.reverse()
                        initAdapter()
                    }
                    taskEmpty()
                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Erro", Toast.LENGTH_SHORT).show()                }

            })
    }

    private fun taskEmpty(){
        binding.textInfo.text = if (taskList.isEmpty()){
            "Nenhuma tarefea cadastrada."
        }else{
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}