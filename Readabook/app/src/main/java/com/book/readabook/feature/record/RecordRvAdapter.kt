package com.book.readabook.feature.record

import com.book.readabook.databinding.RecordItemBinding
import com.book.readabook.model.data.RecordData

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView


class RecordRvAdapter : RecyclerView.Adapter<RecordRvAdapter.RecentViewHolder>(){
    private val items = ArrayList<RecordData>()
    var itemClick : OnItemClick? = null
    private val limit = 12

    interface OnItemClick {
        fun onClick(view: View, position: Int,text : String)
    }

    inner class RecentViewHolder(private val binding: RecordItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : RecordData){
            binding!!.tvRecord.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecordItemBinding.inflate(layoutInflater)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        if(itemClick!= null){
            holder.itemView?.setOnClickListener {
                itemClick!!.onClick(it,position,items[position].text)
            }
        }
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        if(items.size>limit){
            return limit
        }

        else{
            return items.size
        }
    }

    fun setList(recordData : List<RecordData>){
        items.clear()
        items.addAll(recordData)
    }

    fun deleteList(){
        items.clear()
    }

}