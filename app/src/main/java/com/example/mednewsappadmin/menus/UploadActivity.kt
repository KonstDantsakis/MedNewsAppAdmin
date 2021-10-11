package com.example.mednewsappadmin.menus


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mednewsappadmin.R
import com.example.mednewsappadmin.firebase.UploadLinkDb
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class UploadActivity : AppCompatActivity() {

    protected val uploadsCollectionRef = Firebase.firestore.collection("uploads")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        button.setOnClickListener {
            val uploadTitle = editTextUploadTitle.text.toString()
            val uploadUrl = editTextUploadUrl.text.toString()
            val uploadPost = UploadLinkDb(uploadTitle, uploadUrl)
            upload(uploadPost)
        }
    }
    private fun upload(uploadPost: UploadLinkDb ) = CoroutineScope(Dispatchers.IO).launch {

        try {

            uploadsCollectionRef.add(uploadPost).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, "Successfully uploaded", Toast.LENGTH_LONG).show()
            }

        }
        catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }
}