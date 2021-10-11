package com.example.mednewsappadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mednewsappadmin.R
import com.example.mednewsappadmin.firebase.UploadLinkDb
import kotlinx.android.synthetic.main.item_upload_link.view.*

class UploadAdapter ( private val uploadLinkList: ArrayList<UploadLinkDb>
        ): RecyclerView.Adapter<UploadAdapter.TextViewHolder>() {
    inner class TextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return TextViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_upload_link, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {

        val uploadLinks: UploadLinkDb = uploadLinkList[position]

        holder.itemView.upTitle.text = uploadLinks.title
        holder.itemView.upUrl.text = uploadLinks.url



    }

    override fun getItemCount(): Int {
        return uploadLinkList.size

    }
}