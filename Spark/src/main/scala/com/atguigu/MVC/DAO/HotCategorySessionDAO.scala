package com.atguigu.MVC.DAO

import com.atguigu.sparkcore.bean.UserVisitAction
import com.atguigu.summer.framework.core.TDao
import org.apache.spark.rdd.RDD

/**
 * @author david 
 * @create 2020-09-01 下午 4:31 
 */
class HotCategorySessionDAO extends TDao{
  //行为对象进行封装
  def getUserVisitAction(path:String) ={
        val rdd: RDD[String] = readFile(path)
    rdd.map(
      //把行数据封装成样例类对象
      line=>{
        val datas = line.split("_")
        UserVisitAction(
          datas(0),
          datas(1).toLong,
          datas(2),
          datas(3).toLong,
          datas(4),
          datas(5),
          datas(6).toLong,
          datas(7).toLong,
          datas(8),
          datas(9),
          datas(10),
          datas(11),
          datas(12).toLong,
        )
      }

    )
  }

}
