    package com.example.galacticon

    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView

    class MainActivity : AppCompatActivity() {
        private lateinit var linearLayoutManager:LinearLayoutManager
        lateinit var recyclerView:RecyclerView
        private lateinit var adapter: RecyclerAdapter
        lateinit var listOfString:List<String>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            listOfString = listOf<String>("shubham","vishal","vijay","sagar")
            recyclerView = findViewById(R.id.recyclerViewId)
            linearLayoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = linearLayoutManager
            adapter = RecyclerAdapter(listOfString)
            recyclerView.adapter = adapter

        }
    }