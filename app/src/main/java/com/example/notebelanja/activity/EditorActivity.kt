package com.example.notebelanja.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.yourapp.R
import com.example.yourapp.entity.AppDatabase
import com.example.yourapp.entity.Transaction
import java.text.SimpleDateFormat
import java.util.*

class EditorActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var titleInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var detailInput: EditText
    private lateinit var saveButton: Button
    private lateinit var database: AppDatabase

    private var imagePath: String? = null
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        imageView = findViewById(R.id.uploadImage)
        titleInput = findViewById(R.id.titleInput)
        dateInput = findViewById(R.id.dateInput)
        amountInput = findViewById(R.id.amountInput)
        categorySpinner = findViewById(R.id.categorySpinner)
        detailInput = findViewById(R.id.detailInput)
        saveButton = findViewById(R.id.saveButton)

        database = AppDatabase.getInstance(applicationContext)

        // Toolbar Back Button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Set default date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateInput.setText(dateFormat.format(calendar.time))

        // Date picker
        dateInput.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    dateInput.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Populate category spinner
        val categories = listOf("Food", "Transport", "Health", "Entertainment", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Image upload
        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Save data
        saveButton.setOnClickListener {
            val title = titleInput.text.toString()
            val date = dateInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()
            val category = categorySpinner.selectedItem.toString()
            val detail = detailInput.text.toString()

            if (title.isNotEmpty() && amount != null) {
                val transaction = Transaction(
                    id = null,
                    title = title,
                    date = date,
                    amount = amount,
                    category = category,
                    detail = detail,
                    imagePath = imagePath
                )
                database.transactionDao().insert(transaction)
                Toast.makeText(this, "Transaction saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val uri = data?.data
            imagePath = uri.toString()
            Glide.with(this).load(imagePath).into(imageView)
        }
    }
}
