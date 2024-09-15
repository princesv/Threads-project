package com.example.threadsproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    lateinit var buttonStartSimpleThread: Button
    lateinit var tvSimpleThreadResult: TextView
    lateinit var buttonCoroutineThread: Button
    lateinit var tvCoroutineThreadResult: TextView
    lateinit var toastFollowers: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonStartSimpleThread=findViewById(R.id.start_simple_thread)
        tvSimpleThreadResult=findViewById(R.id.simple_thread_result)
        buttonCoroutineThread=findViewById(R.id.start_coroutine_thread)
        tvCoroutineThreadResult=findViewById(R.id.coroutine_thread_result)
        toastFollowers=findViewById(R.id.toast_followers)
        setOnClickListenerOnSimpleThreadButton()
        setOnClickListenerOnCoroutineThreadButton()
        setOnClickListenertOnToastFollowers()
    }
    fun setOnClickListenerOnSimpleThreadButton() {
        buttonStartSimpleThread.setOnClickListener() {
            val t = Thread (
                Runnable{
                    for (i in 1..10) {
                        Thread.sleep(100)
                        tvSimpleThreadResult.setText(i.toString())
                    }
                }
            )
            t.start()
        }
    }
    fun setOnClickListenerOnCoroutineThreadButton(){
        buttonCoroutineThread.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                updateCoroutineTv()
            }
        }
    }

    suspend fun updateCoroutineTv(){
        for (i in 1..10) {
            delay(100)
            tvCoroutineThreadResult.setText(i.toString())
        }
    }

    //-------------------ToastFollowers----------------------------------->
    suspend fun getFollowers():Int{
        delay(1000)
        return 50
    }
    suspend fun toastFollowers(){
        val job= CoroutineScope(Dispatchers.IO).async {
            getFollowers()
        }
        var followers=99
        val job2= CoroutineScope(Dispatchers.IO).launch{
            followers=job.await()
            Log.e("TAG","Within withContext IO: $followers")
            delay(1000)
            followers=followers+1

        }
        withContext(Dispatchers.Main){
            Log.e("TAG","Within Main context: $followers ")
        }
       // job.join()
      //  Toast.makeText(this,"Followers: $res",Toast.LENGTH_SHORT).show()
      //  Log.e("TAG","Followers: ${job.await()}")
    }
    fun setOnClickListenertOnToastFollowers(){
        toastFollowers.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                toastFollowers()
            }
        }
    }
    //<-------------------ToastFollowers-----------------------------------

}