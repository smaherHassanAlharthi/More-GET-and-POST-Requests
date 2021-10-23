package com.example.moregetandpostrequests

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getandpostlocation.APIClient
import com.example.moregetandpostrequests.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myRv: RecyclerView
    private lateinit var rvAdapter: RVAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun getUsers(v: View) {
            //show progress Dialog
            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Please wait")
            progressDialog.show()

            val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
            var data: Users? = null
            val call: Call<Users?>? = apiInterface!!.getUsersInfo()

            call?.enqueue(object : Callback<Users?> {
                override fun onResponse(
                    call: Call<Users?>?,
                    response: Response<Users?>
                ) {
                    progressDialog.dismiss()
                    data = response.body()
                    data?.let { setRV(it) } //if not null set RV

                }
                override fun onFailure(call: Call<Users?>, t: Throwable?) {
                    Toast.makeText(applicationContext, "Unable to load data!", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                    call.cancel()
                }
            })
    }


    fun addUser(v: View) {
        //check if user inputs are not empty
        if (binding.etName.text.isNotEmpty() && binding.etLocation.text.isNotEmpty()  ) {
            val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
            val user = User(binding.etLocation.text.toString(), binding.etName.text.toString())
            val call: Call<User> = apiInterface!!.addUsersInfo(user)

            call?.enqueue(object : Callback<User?> {
                override fun onResponse(
                    call: Call<User?>?,
                    response: Response<User?>
                ) {

                    Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show()

                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to add person.", Toast.LENGTH_SHORT)
                        .show()
                    call.cancel()
                }
            })
        } else {
            Toast.makeText(applicationContext, "Please do not leave it empty!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setRV(data: Users) {
        myRv = findViewById(R.id.rvUsers)
        rvAdapter = RVAdapter(data, this)
        myRv.adapter = rvAdapter
        myRv.layoutManager = LinearLayoutManager(applicationContext)
    }

}

