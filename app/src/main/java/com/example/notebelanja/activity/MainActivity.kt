package com.example.notebelanja.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebelanja.R
import com.example.notebelanja.adapter.TransactionAdapter
import com.example.notebelanja.entity.Transaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi RecyclerView dan Adapter
        transactionAdapter = TransactionAdapter()
        rv_user.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }

        // Dummy data untuk testing
        val dummyData = listOf(
            Transaction(1, "Beli Gula", "2025-01-21", 25000, 1, "Gula 1 kg"),
            Transaction(2, "Beli Tepung", "2025-01-22", 15000, 2, "Tepung 2 kg"),
            Transaction(3, "Beli Telur", "2025-01-23", 30000, 3, "Telur 1 lusin")
        )
        transactionAdapter.submitList(dummyData)

        // Floating button untuk menambahkan transaksi
        floatingbtn.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            startActivity(intent)
        }
    }
}
