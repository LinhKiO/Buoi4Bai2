package com.example.buoi4_bai2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buoi4_bai2.R
import com.example.buoi4_bai2.Product

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.tvProductName)
    private val priceTextView: TextView = itemView.findViewById(R.id.tvProductPrice)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.tvProductDescription)

    fun bind(product: Product) {
        nameTextView.text = product.name
        priceTextView.text = "Giá: ${product.price} VNĐ"
        descriptionTextView.text = product.description
    }
}
