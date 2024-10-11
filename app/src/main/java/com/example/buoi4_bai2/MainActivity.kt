package com.example.buoi4_bai2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirebaseRecyclerAdapter<Product, ProductViewHolder>
    private lateinit var btnAddProduct: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnAddProduct = findViewById(R.id.btnAddProduct)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Thiết lập FirebaseRecyclerOptions
        val options = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(FirebaseDatabase.getInstance().getReference("products"), Product::class.java)
            .build()

        // Tạo FirebaseRecyclerAdapter
        adapter = object : FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Product) {
                holder.bind(model)
                holder.itemView.setOnClickListener {
                    // Khi bấm vào sản phẩm, mở ProductDetailActivity
                    val intent = Intent(this@MainActivity, ProductDetailActivity::class.java)
                    intent.putExtra("productId", model.id)
                    startActivity(intent)
                }
            }
        }

        // Thiết lập adapter cho RecyclerView
        recyclerView.adapter = adapter

        // Bắt sự kiện cho nút "Thêm Sản Phẩm"
        btnAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    // Khi Activity được bắt đầu, lắng nghe các thay đổi từ Firebase
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    // Dừng việc lắng nghe khi Activity không còn hiển thị
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
