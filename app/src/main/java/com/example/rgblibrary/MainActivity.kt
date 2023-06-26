package com.example.rgblibrary

import android.app.Instrumentation.ActivityResult
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var rvPalette: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private var palette = Palette()
    private var formResult: ActivityResultLauncher<Intent>? = null

    init {
        this.palette.add(Color("Red", 100, 0, 0))
        this.palette.add(Color("Green", 0, 100, 0))
        this.palette.add(Color("Blue", 0, 0, 100))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.rvPalette = findViewById(R.id.rvMainPallete)
        this.fabAdd = findViewById(R.id.fabMainAdd)

        this.rvPalette.adapter = PaletteAdapter(this.palette.get(), OnItemClick())
        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvPalette)

        this.formResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("COLOR", Color::class.java)
                } else {
                    it.data?.getSerializableExtra("COLOR")
                } as Color

                if (color.id == this.palette.size()) {
                    this.palette.add(color)
                    (this.rvPalette.adapter as PaletteAdapter).notifyItemInserted(this.palette.size())
                } else {

                    val changeColor = this@MainActivity.palette.get(color.id)

                    changeColor.name = color.name
                    changeColor.red = color.red
                    changeColor.green = color.green
                    changeColor.blue = color.blue

                    (this.rvPalette.adapter as PaletteAdapter).notifyItemChanged(color.id)
                }
            }
        }

        this.fabAdd.setOnClickListener {
            val intent = Intent( this, FormActivity::class.java)
            this@MainActivity.formResult?.launch(intent)
        }
    }

    inner class OnItemClick: OnItemClickRView {
        override fun onItemClick(position: Int) {
            val intent = Intent(this@MainActivity, FormActivity::class.java).apply {
                putExtra("COLOR", this@MainActivity.palette.get(position))
            }
            this@MainActivity.formResult?.launch(intent)
        }
    }

    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.START or ItemTouchHelper.END
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
            target: ViewHolder
        ): Boolean {
            (this@MainActivity.rvPalette.adapter as PaletteAdapter).move(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            if (direction == ItemTouchHelper.END) {
                val builder = AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("Confirm")
                    setMessage("Do you want delete this color?")

                    setPositiveButton("Yes") {
                        _, _ -> (this@MainActivity.rvPalette.adapter as PaletteAdapter).delete(position)
                    }

                    setNegativeButton("Cancel") {
                        dialog, _ ->
                        dialog.dismiss()
                        (this@MainActivity.rvPalette.adapter as PaletteAdapter).notifyItemChanged(position)
                    }
                }
                builder.create().show()
            }

            if (direction == ItemTouchHelper.START) {
                (this@MainActivity.rvPalette.adapter as PaletteAdapter).notifyItemChanged(position)

                val color = (this@MainActivity.rvPalette.adapter as PaletteAdapter).list[position]
                val hexColor = String.format("#%02X%02X%02X", color.red, color.green, color.blue)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, hexColor)
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        inner class OnClick(var viewHolder: ViewHolder): DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                (this@MainActivity.rvPalette.adapter as PaletteAdapter).delete(viewHolder.adapterPosition)
            }
        }

    }
}