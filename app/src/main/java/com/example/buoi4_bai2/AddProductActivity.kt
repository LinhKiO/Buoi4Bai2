package com.example.buoi4_bai2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.database.FirebaseDatabase

class AddProductActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtPrice: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSaveProduct: Button

    private var isFormEmpty = true  // Biến kiểm tra xem form có thay đổi hay không

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Khởi tạo các thành phần
        edtName = findViewById(R.id.edtName)
        edtPrice = findViewById(R.id.edtPrice)
        edtDescription = findViewById(R.id.edtDescription)
        btnSaveProduct = findViewById(R.id.btnSaveProduct)

        // Lắng nghe sự thay đổi trên các trường nhập liệu
        edtName.addTextChangedListener { isFormEmpty = false }
        edtPrice.addTextChangedListener { isFormEmpty = false }
        edtDescription.addTextChangedListener { isFormEmpty = false }

        // Lưu sản phẩm vào Firebase khi bấm nút "Lưu"
        btnSaveProduct.setOnClickListener {
            val name = edtName.text.toString().trim()
            val price = edtPrice.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            // Kiểm tra dữ liệu nhập vào
            if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo đối tượng Product mới
            val productId = FirebaseDatabase.getInstance().getReference("products").push().key ?: return@setOnClickListener
            val product = Product(productId, name, price, description)

            // Lưu sản phẩm vào Firebase Realtime Database
            val databaseReference = FirebaseDatabase.getInstance().getReference("products")
            databaseReference.child(productId).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(this, "Sản phẩm đã được thêm", Toast.LENGTH_SHORT).show()

                    // Quay về MainActivity ban đầu sau khi thêm sản phẩm thành công
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Xóa tất cả các activity trong back stack
                    startActivity(intent)
                    finish()  // Đóng AddProductActivity
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Xử lý khi người dùng ấn nút quay lại (Back Pressed)
    override fun onBackPressed() {
        super.onBackPressed()

        // Quay về MainActivity ban đầu khi không thực hiện thay đổi gì
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Xóa tất cả các activity trong back stack
        startActivity(intent)
        finish()  // Đóng ProductDetailActivity
    }
}
