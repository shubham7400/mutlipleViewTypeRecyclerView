package com.example.messangerapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messangerapp.AdapterClasses.UserAdapter
import com.example.messangerapp.ModelClasses.Users
import com.example.messangerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private var userAdapter: UserAdapter? = null
    private var mUsers: List<Users>? = null
    lateinit var searchUsersEt: EditText
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.searchList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mUsers = ArrayList()
        retrieveAllUsers()

        searchUsersEt = view.findViewById(R.id.searchUsersET)
        searchUsersEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchForUsers(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {
             }

        })

        return view
    }

    private fun retrieveAllUsers() {
         val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")
      /*  Log.d("tag ", " refUsers sssssssssssssssssssssssssssssssss: "+refUsers)*/
        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if (searchUsersEt.text.toString() == "")
                {
                    for(snapshot in p0.children)
                    {
                        var value = snapshot.getValue() as HashMap<*,*>

                        var user:Users = Users(value["uid"].toString(),value["username"].toString(),value["profile"].toString(),value["cover"].toString(),value["status"].toString(),value["search"].toString(),value["facebook"].toString(),value["intagram"].toString(),value["website"].toString())
                        if (!(user.getUID().equals(firebaseUserId)))
                        {
                                Log.d("tag ", "Users : "+user.getUsername())
                            Log.d("tag", "all values : "+user.getUID())
                            (mUsers as ArrayList<Users>).add(user)
                        }

                       /* Log.d("tag ", "U : "+user.getUsername())
                        Log.d("tag", "id : "+user.getUID())*/

                    }
                }

              /*  Log.d("tage", "mUsers : "+mUsers)
                Log.d("tage", "context : "+context)*/
                /*Log.d("tage", "mUsers : "+mUsers)*/

                userAdapter = UserAdapter(context, mUsers!!,false)
                recyclerView.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun searchForUsers(username: String)
    {
        val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
        val queryUsers = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search").startAt(username).endAt(username+"\uf8ff")

        queryUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for(snapshot in p0.children)
                {
                    var value = snapshot.getValue() as HashMap<*,*>
                    var user:Users = Users(value["uid"].toString(),value["username"].toString(),value["profile"].toString(),value["cover"].toString(),value["status"].toString(),value["search"].toString(),value["facebook"].toString(),value["intagram"].toString(),value["website"].toString())
                    if (!(user.getUID().equals(firebaseUserId)))
                    {
                        (mUsers as ArrayList<Users>).add(user)
                    }

                }
                userAdapter = UserAdapter(context!!, mUsers!!,false)
                recyclerView.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}