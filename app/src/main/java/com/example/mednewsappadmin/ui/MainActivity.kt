package com.example.mednewsappadmin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mednewsappadmin.R
import com.example.mednewsappadmin.adapter.UploadAdapter
import com.example.mednewsappadmin.firebase.UploadLinkDb
import com.example.mednewsappadmin.menus.DeleteActivity
import com.example.mednewsappadmin.menus.UpdateActivity
import com.example.mednewsappadmin.menus.UploadActivity
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: UploadAdapter
    private lateinit var uploadLinkList: ArrayList<UploadLinkDb>
    private lateinit var db: FirebaseFirestore
    protected val uploadsCollectionRef = Firebase.firestore.collection("uploads")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadLink.layoutManager = LinearLayoutManager(this)
        uploadLink.setHasFixedSize(true)

        uploadLinkList = arrayListOf()

        myAdapter = UploadAdapter(uploadLinkList)
        uploadLink.adapter = myAdapter

        EventChangeListener()

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuIflater = menuInflater
        menuIflater.inflate(R.menu.add_link, menu)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.addLink) {
            val intent = Intent(applicationContext, UploadActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.deleteLink) {
            val intent = Intent(applicationContext, DeleteActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.updateLink) {
            val intent = Intent(applicationContext, UpdateActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("uploads").
            addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?
                ) {

                    if(error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    var i: Int = 0
                    for(dc : DocumentChange in value?.documentChanges!!) {

                        if(dc.type == DocumentChange.Type.ADDED) {
                            uploadLinkList.add(dc.document.toObject(UploadLinkDb::class.java))
                        }
                        if(dc.type == DocumentChange.Type.MODIFIED) {
                            uploadLinkList.contains(dc.document.toObject(UploadLinkDb::class.java))

                        }
                        if(dc.type == DocumentChange.Type.REMOVED) {
                            uploadLinkList.remove(dc.document.toObject(UploadLinkDb::class.java))
                        }


                    }

                    myAdapter.notifyDataSetChanged()

                }
        })

    }


}