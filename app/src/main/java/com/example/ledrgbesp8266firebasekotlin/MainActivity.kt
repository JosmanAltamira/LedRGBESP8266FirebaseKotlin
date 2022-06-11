package com.example.ledrgbesp8266firebasekotlin

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.ledrgbesp8266firebasekotlin.library.ColorPicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var state: Int
        var cycle: Int
        val database = Firebase.database
        val databaseReference = database.reference

        val ledColorPicker: ColorPicker = findViewById(R.id.ledColorPicker)
        val ledToggleButton: ToggleButton = findViewById(R.id.ledToggleButton)
        val cycleToggleButton: ToggleButton = findViewById(R.id.cycleToggleButton)

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                state = dataSnapshot.child("state").getValue<Int>()!!
                cycle = dataSnapshot.child("cycle").getValue<Int>()!!

                ledToggleButton.isChecked = state != 0
                cycleToggleButton.isChecked = cycle != 0

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        ledColorPicker.listener = object : ColorPicker.Listener{
            override fun colorChanged(color: Int) {
                val red: Int = Color.red(color)
                val green: Int = Color.green(color)
                val blue: Int = Color.blue(color)

                databaseReference.child("cycle").setValue(0)
                databaseReference.child("red").setValue(255-red)
                databaseReference.child("green").setValue(255-green)
                databaseReference.child("blue").setValue(255-blue)
            }
        }

        ledToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("state").setValue(1)
            } else {
                databaseReference.child("state").setValue(0)
            }
        }

        cycleToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                databaseReference.child("cycle").setValue(1)
            } else {
                databaseReference.child("cycle").setValue(0)
            }
        }

    }
}