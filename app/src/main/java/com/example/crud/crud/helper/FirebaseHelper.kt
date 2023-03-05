package com.example.crud.crud.helper

import com.example.crud.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper {
    companion object{

        fun getDataBase() = FirebaseDatabase.getInstance().reference

        private fun getAuth() = FirebaseAuth.getInstance()

        fun getIdUser() = getAuth().uid

        fun isAuthenticated() = getAuth().currentUser != null

        fun validError(error: String):Int{
            return when{
                error.contains("There is no user record corresponding to this identifier") ->{
                    R.string.conta_sem_registro
                }error.contains("The email address is badly formatted") ->{
                    R.string.email_invalido
                }error.contains("The password is invalid or the user does not have a password") ->{
                    R.string.senha_invalida
                }error.contains("The email address is already in use by another account") ->{
                    R.string.email_ja_usando
                }error.contains("The given password is invalid") ->{
                    R.string.senha_fraca
                }
                else -> {
                    R.string.erro_generico
                }
            }
        }
    }
}