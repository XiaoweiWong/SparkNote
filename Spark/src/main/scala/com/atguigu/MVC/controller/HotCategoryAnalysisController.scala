package com.atguigu.MVC.controller

import com.atguigu.MVC.service.HotCategoryAnalysisService
import com.atguigu.summer.framework.core.TController

/**
 * @author david 
 * @create 2020-09-01 下午 4:31 
 */
class HotCategoryAnalysisController extends TController{

  private val service: HotCategoryAnalysisService = new HotCategoryAnalysisService

  override def execute() ={
         val array: Array[(String, (Int, Int, Int))] = service.analysis1()
          array.foreach(println)
  }
}
