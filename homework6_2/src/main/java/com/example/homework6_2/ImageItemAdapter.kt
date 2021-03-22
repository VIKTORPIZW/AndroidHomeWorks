package com.example.homework6_2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageItemAdapter(private val imageItemList: List<ImageItem>):
        RecyclerView.Adapter<ImageItemAdapter.ImageItemViewHolder>() {

    class ImageItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val imageItem: ImageView = itemView.findViewById(R.id.image_item)

        fun bind(workItem: ImageItem){
            val name = workItem.fileName
            val pathUri = workItem.imageUri
            val image = Glide.with(itemView)
                    .load(workItem.imageUri)
                    image.into(imageItem)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context,ImageFullActivity::class.java)
                intent.putExtra("fileName",name)
                intent.putExtra("filePath",pathUri)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        holder.bind(imageItemList.get(position))
    }

    override fun getItemCount(): Int {
        return imageItemList.size
    }
}