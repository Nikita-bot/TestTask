package ru.pazderin.bindata


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class MyAdapter(private val results:ArrayList<String>, val cellClickListener:CellClickListener):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val res = itemView.findViewById<TextView>(R.id.textView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rview_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.res.text = "${position+1}: "+results.get(position)
        holder.itemView.setOnClickListener {
            cellClickListener.onClick(results.get(position))
        }
    }

    override fun getItemCount(): Int = results.size
}
