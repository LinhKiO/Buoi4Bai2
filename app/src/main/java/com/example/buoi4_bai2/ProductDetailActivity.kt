package com.example.buoi4_bai2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        nameEditText = findViewById(R.id.editTextName)
        priceEditText = findViewById(R.id.editTextPrice)
        descriptionEditText = findViewById(R.id.editTextDescription)
        saveButton = findViewById(R.id.buttonSave)
        deleteButton = findViewById(R.id.buttonDelete)

        // Lấy productId từ Intent
        productId = intent.getStringExtra("productId")

        if (productId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("products").child(productId!!)
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(Product::class.java)
                    if (product != null) {
                        nameEditText.setText(product.name)
                        priceEditText.setText(product.price)
                        descriptionEditText.setText(product.description)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

            saveButton.setOnClickListener {
                updateProduct()
            }

            deleteButton.setOnClickListener {
                deleteProduct()
            }
        }
    }

    private fun updateProduct() {
        val name = nameEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedProduct = Product(
            id = productId ?: "",
            name = name,
            price = price,
            description = description
        )

        val databaseReference = FirebaseDatabase.getInstance().getReference("products").child(productId!!)
        databaseReference.setValue(updatedProduct)
            .addOnSuccessListener {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()

                // Quay về MainActivity ban đầu sau khi cập nhật thành công
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Xóa tất cả các activity trong back stack
                startActivity(intent)
                finish()  // Đóng ProductDetailActivity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteProduct() {
        FirebaseDatabase.getInstance().getReference("products").child(productId!!)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show()

                // Quay về MainActivity ban đầu sau khi xóa thành công
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Xóa tất cả các activity trong back stack
                startActivity(intent)
                finish()  // Đóng ProductDetailActivity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show()
            }
    }

    // Xử lý khi người dùng bấm back (quay lại mà không thực hiện thay đổi)
    override fun onBackPressed() {
        super.onBackPressed()

        // Quay về MainActivity ban đầu khi không thực hiện thay đổi gì
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Xóa tất cả các activity trong back stack
        startActivity(intent)
        finish()  // Đóng ProductDetailActivity
    }
}
