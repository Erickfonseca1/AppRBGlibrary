package com.example.rgblibrary

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FormActivity: AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etRed: EditText
    private lateinit var etGreen: EditText
    private lateinit var etBlue: EditText
    private lateinit var btSave: Button
    private lateinit var btCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        this.etName = findViewById(R.id.etNameForm)
        this.etRed = findViewById(R.id.etRedForm)
        this.etGreen = findViewById(R.id.etGreenForm)
        this.etBlue = findViewById(R.id.etBlueForm)
        this.btSave = findViewById(R.id.btSaveForm)
        this.btCancel = findViewById(R.id.btCancelForm)

        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("Color", Color::class.java )
        } else {
            intent.getSerializableExtra("Color")
        } as? Color

        this.etName.setText(color?.name)
        this.etRed.setText(color?.red?.toString())
        this.etGreen.setText(color?.green?.toString())
        this.etBlue.setText(color?.blue?.toString())
        this.btSave.setOnClickListener{ save(color) }
        this.btCancel.setOnClickListener{ cancel() }
    }

    private fun save(color: Color?) {
        val name = this.etName.text.toString()
        val red = this.etRed.text.toString().toInt()
        val green = this.etGreen.text.toString().toInt()
        val blue = this.etBlue.text.toString().toInt()
        var newOrUpdatedColor: Color

        if (color == null) {
            newOrUpdatedColor = Color(name, red, green, blue)
        } else {
            newOrUpdatedColor = color
            newOrUpdatedColor.name = name
            newOrUpdatedColor.red = red
            newOrUpdatedColor.green = green
            newOrUpdatedColor.blue = blue
        }

        val intent = Intent().apply {
            putExtra("COLOR", color)
            putExtra("NAME", name)
            putExtra("RED", red)
            putExtra("GREEN", green)
            putExtra("BLUE", blue)
        }
        setResult(RESULT_OK, intent)

        finish()
    }

    private fun cancel() {
        finish()
    }

}