package com.example.mednewsappadmin.menus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mednewsappadmin.R
import com.example.mednewsappadmin.firebase.UploadLinkDb
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_delete.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DeleteActivity : AppCompatActivity() {

    protected val uploadsCollectionRef = Firebase.firestore.collection("uploads")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        buttonDelete.setOnClickListener {
            val oldPost = getOldPosts()
            delete(oldPost)
        }
    }
    private fun getOldPosts(): UploadLinkDb {
        val title = editTextDeleteTitle.text.toString()
        val url = editTextDeleteUrl.text.toString()
        return UploadLinkDb(title, url)
    }
    private fun delete(deletePost: UploadLinkDb) = CoroutineScope(Dispatchers.IO).launch {

        val uploadQuery = uploadsCollectionRef
            .whereEqualTo("title", deletePost.title)
            .get()
            .await()

        if (uploadQuery.documents.isNotEmpty()) {
            for(document in uploadQuery) {
                try{
                    uploadsCollectionRef.document(document.id).delete().await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DeleteActivity, "Deleted successfully", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DeleteActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@DeleteActivity, "No upload found", Toast.LENGTH_LONG).show()
            }
        }

    }
}