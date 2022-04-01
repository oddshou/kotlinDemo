package com.odds.kotlindemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.odds.kotlindemo.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            //1、这里不能直接调用delay，会显示错误，挂起函数只能被其他挂起函数或协程调用
//            delay(3000)

            //2、lauch 与 runBlocking都能在全局开启一个协程，但 lauch 是非阻塞的 而 runBlocking 是阻塞的(阻塞当前运行的线程)
            //3、使用GlobalScope 创建协程非常危险，它会创建一个顶层的协程，生命周期随应用。运行异常不会终止。

            // 多线程 synchronized wait，notify
//            TestThread.run()
            // join
//            TestThread.joinTest()

            // 生产消费模型
//            ProductorConsumer.main()


            runBlocking {
                coroutineScopeTest()
            }
            println("end")
        }

        GlobalScope.async {  }

        MainScope().launch {  }

    }

    fun repeatCoroutine(){
        repeat(100_1000){
            CoroutineScope(Dispatchers.Default).launch {
                delay(5000L)
                print(".")
            }
        }
    }

    fun asyncTest(){
        val job = CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()
            Log.i("TAG", "1.withContextTest")
            //async 并发执行
            val task1 = async(Dispatchers.IO) {
                delay(2000)
                Log.e("TAG", "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }
            Log.i("TAG", "2.withContextTest")
            val task2 = async(Dispatchers.IO) {
                delay(1000)
                Log.e("TAG", "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }
            // 调用 task1.await() 会等待task1 完成，task2.await()同理，await 为挂起函数
            Log.e("TAG", "task1 = ${task1.await()}  , task2 = ${task2.await()} , 耗时 ${System.currentTimeMillis() - time1} ms  [当前线程为：${Thread.currentThread().name}]")
        }
        //job.join 可以等待协程结束，但是它本身也是 suspend 方法，需要在协程或者 suspend 方法中调用
//        job.join()
    }

    /**
     * 通过 withContext 可以调度协程的上下文环境，Main<->IO 线程切换等等
     */
    fun withContextTest(){
        CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()
            Log.i("TAG", "1.withContextTest")
            //withContext 串行执行
            val task1 = withContext(Dispatchers.IO){
                delay(2000)
                Log.e("TAG", "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }
            Log.i("TAG", "2.withContextTest")
            val task2 = withContext(Dispatchers.IO) {
                delay(1000)
                Log.e("TAG", "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }
            Log.i("TAG", "3.withContextTest $task1 - $task2")
        }
    }

    fun runBlockingTest() {
        //注意 runBlocking 是常规函数而非挂起函数,阻塞当前线程
        runBlocking {
            delay(3000)    //延时500ms
            Log.e("TAG", "1.runBlocking.... [当前线程为：${Thread.currentThread().name}]")
        }
        //此处会被阻塞，需要等协程体执行完
        Log.e("TAG", "2.BtnClick.... [当前线程为：${Thread.currentThread().name}]")
    }

    suspend fun coroutineScopeTest(){
        //coroutineScope 是一个挂起函数
        coroutineScope {
            // launch 非阻塞
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
    }

    fun onThread() {
        Thread {
            //Dispatchers.Default,Main,IO,
            CoroutineScope(Dispatchers.Default).launch {
                delay(3000)
                //会跳转到 Dispatchers 的线程，default为：DefaultDispatcher-worker 的线程，main 为main线程
                Log.i("TAG", "1.执行CoroutineScope.... [当前线程为：${Thread.currentThread().name}]")
            }
            //使用自己新开的线程
            Log.i("TAG", "2.BtnClick.... [当前线程为：${Thread.currentThread().name}]")
        }.start()
    }
}