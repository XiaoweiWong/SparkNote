package com.atguigu.sparkcore.req

import com.atguigu.MVC.controller.WordCountController
import com.atguigu.summer.framework.core.TApplication


/**
 * @author david 
 * @create 2020-09-01 下午 12:59 
 */
object wordCountApplication extends App with TApplication {

  start("spark"){
    val controller= new WordCountController
    controller.execute()
  }

}
