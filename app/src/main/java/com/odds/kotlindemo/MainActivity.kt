package com.odds.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.odds.kotlindemo.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            //1、这里不能直接调用delay，会显示错误，挂起函数只能被其他挂起函数或协程调用
//            delay(3000)

            //2、lauch 与 runBlocking都能在全局开启一个协程，但 lauch 是非阻塞的 而 runBlocking 是阻塞的
            //3、使用GlobalScope 创建协程非常危险，它会创建一个顶层的协程，生命周期随应用。运行异常不会终止。
            GlobalScope.launch {
                delay(3000)
                Log.i("TAG","1.执行CoroutineScope.... [当前线程为：${Thread.currentThread().name}]")
            }
            Log.i("TAG","2.BtnClick.... [当前线程为：${Thread.currentThread().name}]")
        }
    }
}