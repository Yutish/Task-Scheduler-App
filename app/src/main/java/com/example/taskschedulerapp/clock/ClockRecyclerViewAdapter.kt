package com.example.taskschedulerapp.clock

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskschedulerapp.R
import com.example.taskschedulerapp.database.Task

class ClockRecyclerViewAdapter(
    private var itemList: List<Task>
) :
    RecyclerView.Adapter<ClockRecyclerViewAdapter.TaskViewHolder>() {


    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerViewTextItem: TextView = itemView.findViewById(R.id.recyclerViewTextItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_item, parent, false)
        return TaskViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val list = itemList[position]
        val simpleDateFormat = SimpleDateFormat("hh:mm a")
        holder.recyclerViewTextItem.text = simpleDateFormat.format(list.startTime)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}