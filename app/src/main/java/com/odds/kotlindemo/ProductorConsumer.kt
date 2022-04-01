package com.odds.kotlindemo

import kotlin.concurrent.thread

/**
 * ClassName:ProductorConsumer
 * Description: 生产者，消费者模型, 仓库容量最大为4，一共生产100个
 * 2022/3/25 2:07 下午
 * wiki:
 */
class ProductorConsumer {

    companion object {
        //库存商品
        var count = 0
        // Kotlin中的每个类都继承自Any，但Any不声明wait（），notify（）和notifyAll（），这意味着这些方法不能在Kotlin类上调用
        val lock = Object()

        //入口
        public fun main() {
            product()
            consume()
        }

        private fun product() {
            thread(start = true) {
                for (index in 1..100) {
                    synchronized(lock){
                        if (count < 4) {
                            lock.notify()
                        } else {
                            lock.wait()
                        }
                        //生产耗时100ms
                        Thread.sleep(10)
                        //商品数+1
                        count++
                        println("生产 $index ----- 库存 $count")
                    }
                }
            }
        }

        private fun consume() {
            thread(start = true) {
                for (index in 1..100) {
                    synchronized(lock){
                        if (count > 0) {
                            lock.notify()
                        } else {
                            lock.wait()
                        }
                        //消费耗时100ms
                        Thread.sleep(10)
                        //商品数-1
                        count--
                        println("消费 $index ----- 库存 $count")
                    }

                }

            }
        }
    }

}



