package com.alejo.altausuario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val marksCollection: CollectionReference

    init {
        FirebaseApp.initializeApp(this)
        marksCollection = FirebaseFirestore.getInstance().collection("marks")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        save_button.setOnClickListener {saveMark(
                Mark(
                    name_editText.text.toString(),
                    group_editText.text.toString(),
                    mark_editText.text.toString().toDouble()
                )
            )
        }

        addMarksListener()


    }


    private fun saveMark(mark: Mark) {
        marksCollection.add(mark).addOnSuccessListener {
            Toast.makeText(this, "Regsistro guardado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el registro", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addMarksListener() {
        marksCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes)
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error leyendo las notas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addChanges(changes: List<DocumentChange>) {
        for (change in changes) {
            if (change.type == DocumentChange.Type.ADDED) {
                addToList(change.document.toObject(Mark::class.java))
            }
        }
    }

    private fun addToList(mark: Mark) {
        var text = markList_textView.text.toString()
        text += mark.toString() + "\n"
        markList_textView.text = text
    }
}


