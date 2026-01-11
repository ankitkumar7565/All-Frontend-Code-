package com.example.managementtask


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managementtask.Data.DataAdapter
import com.example.managementtask.Data.DataModel
import com.example.managementtask.Data.SessionManager
import com.google.firebase.database.*

class Dashboard : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var list: ArrayList<DataModel>
    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val session = SessionManager(this)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val logoutButton =findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            session.logout()
            startActivity(Intent(this, Registration::class.java))
            finish()
        }
        database = FirebaseDatabase.getInstance().getReference("records")
        list = ArrayList()

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = DataAdapter(
            list,
            onEdit = {
                val intent = Intent(this, Form::class.java)
                intent.putExtra("id", it.id)
                intent.putExtra("title", it.title)
                intent.putExtra("desc", it.desc)
                startActivity(intent)
            },
            onDelete = {
                database.child(it.id!!).removeValue()
            }
        )

        recyclerView.adapter = adapter

        btnAdd.setOnClickListener {
            startActivity(Intent(this, Form::class.java))
        }

        fetchData()
    }

    // 🔹 READ
    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (data in snapshot.children) {
                    val model = data.getValue(DataModel::class.java)
                    if (model != null) list.add(model)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}