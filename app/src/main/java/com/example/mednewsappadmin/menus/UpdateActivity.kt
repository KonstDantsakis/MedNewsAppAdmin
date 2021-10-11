package com.example.mednewsappadmin.menus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mednewsappadmin.R
import com.example.mednewsappadmin.firebase.UploadLinkDb
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class UpdateActivity : AppCompatActivity() {
    protected val uploadsCollectionRef = Firebase.firestore.collection("uploads")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        buttonUpdate.setOnClickListener {
            val oldPost = getOldPost()
            val newPost = getNewPostMap()
            updatePost(oldPost, newPost)
        }
    }
    private fun getOldPost(): UploadLinkDb {
        val title = editTextTitle1.text.toString()
        val url = editTextUrl.text.toString()
        return UploadLinkDb(title, url)
    }
    private fun getNewPostMap(): Map<String, Any> {
        val title = editTextUpdateTitle.text.toString()
        val url = editTextUpdateUrl.text.toString()
        val map = mutableMapOf<String, Any>()
        if(title.isNotEmpty()) {
            map["title"] = title
        }
        if (url.isNotEmpty()) {
            map["url"] = url
        }
        return map
    }
    private fun updatePost(post: UploadLinkDb, newPostMap: Map<String, Any>)  = CoroutineScope(Dispatchers.IO).launch {
        val postQuery = uploadsCollectionRef
            .whereEqualTo("title", post.title)
            .get()
            .await()
        if(postQuery.documents.isNotEmpty()) {

            for(document in postQuery) {
                try{
                    uploadsCollectionRef.document(document.id).set(
                        newPostMap,
                        SetOptions.merge()
                    ).await()

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UpdateActivity, "Data updated", Toast.LENGTH_LONG).show()
                    }

                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@UpdateActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }

        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UpdateActivity, "No post found in database", Toast.LENGTH_LONG).show()
            }
        }
    }








}