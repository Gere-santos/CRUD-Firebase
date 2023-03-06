package com.example.crud.crud.model

import android.os.Parcelable
import com.example.crud.crud.helper.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.parcelize.Parcelize
@Parcelize
data class Task (
    var id: String = "",
    var title: String = "",
    var status: Int = 0
): Parcelable{
    init{
        this.id = FirebaseHelper.getDataBase().push().key ?: ""
    }
}