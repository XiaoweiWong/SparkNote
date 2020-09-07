package com.atguigu.MVC.controller

import com.atguigu.MVC.service.WordCountService
import com.atguigu.summer.framework.core.TController


/**
 * @author david 
 * @create 2020-09-01 下午 3:29 
 */
class WordCountController extends TController{
private val wordCountService: WordCountService = new WordCountService

  override def execute(): Unit = {
   val wordCountArray: Array[(String, Int)] = wordCountService.analysis()
    println(wordCountArray.mkString(","))

  }
}
