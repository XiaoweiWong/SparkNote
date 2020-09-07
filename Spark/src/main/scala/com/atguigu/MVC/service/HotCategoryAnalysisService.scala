package com.atguigu.MVC.service

import com.atguigu.MVC.DAO.HotCategoryAnalysisDAO
import com.atguigu.sparkcore.bean.HotCategory
import com.atguigu.sparkcore.helper.HotCategoryAnalysisAccumulator
import com.atguigu.summer.framework.core.TService
import com.atguigu.summer.framework.util.EnvUtil
import org.apache.spark.rdd.RDD

import scala.collection.mutable

/**
 * @author david 
 * @create 2020-09-01 下午 4:32 
 */
class HotCategoryAnalysisService extends TService {
  private val dao: HotCategoryAnalysisDAO = new HotCategoryAnalysisDAO

  /**
   * 基本的数据分析方法
   *
   * @return
   */
  override def analysis() = {
    //读取电商的数据
    val valueRDD: RDD[String] = dao.readFile("input/user_visit_action.txt")
    //对商品点击的统计 : line => (category,clickCount)

    valueRDD.cache()
    val actionRDD: RDD[(String, Int)] = valueRDD.map(
      action => {
        val arrays: Array[String] = action.split("_")
        (arrays(6), 1)
      }
    ).filter(_._1 != "-1")
    val actionCount: RDD[(String, Int)] = actionRDD.reduceByKey(_ + _)


    //对品类下单的统计
    val value2RDD: RDD[String] = valueRDD.map(
      action => {
        val arrays: Array[String] = action.split("_")
        arrays(8)
      }).filter(_ != "null")

    val oderRDD: RDD[(String, Int)] = value2RDD.flatMap(
      id => {
        val ids = id.split(",")
        ids.map(id => (id, 1))
      }
    )
    val oderCount: RDD[(String, Int)] = oderRDD.reduceByKey(_ + _)


    //对商品支付的统计
    val value3RDD: RDD[String] = valueRDD.map(
      action => {
        val arrays: Array[String] = action.split("_")
        arrays(10)
      }).filter(_ != "null")

    val payRDD: RDD[(String, Int)] = value3RDD.flatMap(
      id => {
        val ids = id.split(",")
        ids.map(id => (id, 1))
      }
    )
    val payCount: RDD[(String, Int)] = payRDD.reduceByKey(_ + _)
    //将统计结果转换结构,(元素1，元素2，元素3)
    //（品类，点击数量），（品类，下单数量），（品类，支付数量）
    //(品类，（点击数量，下单数量，支付数量）)

    val action1RDD: RDD[(String, (Int, Int, Int))] = actionCount.map {
      case (id, clickCounts) => {
        (id, (clickCounts, 0, 0))
      }
    }
    val oder1RDD: RDD[(String, (Int, Int, Int))] = oderCount.map {
      case (id, oderCounts) => {
        (id, (0, oderCounts, 0))
      }
    }
    val payRDD1: RDD[(String, (Int, Int, Int))] = payCount.map {
      case (id, payCounts) => {
        (id, (0, 0, payCounts))
      }
    }

    val valueCount: RDD[(String, (Int, Int, Int))] = action1RDD.union(oder1RDD).union(payRDD1)
    val reduceRDD: RDD[(String, (Int, Int, Int))] = valueCount.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3)
      }
    )
    val sortRDD: RDD[(String, (Int, Int, Int))] = reduceRDD.sortBy(_._2, false)


    //      //将转换后的数据进行排序
    //    val sortRDD: RDD[(String, (Int, Int, Int))] = map3RDD.sortBy(_._2,false)
    //
    //对排序结果进行取前十名
    val result: Array[(String, (Int, Int, Int))] = sortRDD.take(10)
    result
  }


  /**
   * 数据分析优化1
   *
   * @return
   */
  def analysis1() = {
    //读取电商的数据
    val actionRDD: RDD[String] = dao.readFile("input/user_visit_action.txt")
    //对商品点击的统计 : line => (category,clickCount)
    val flatmapRDD = actionRDD.flatMap(
      action => {
        val datas = action.split("_")
        if (datas(6) != "-1") {
          List((datas(6), (1, 0, 0)))
        } else if (datas(8) != "null") {
          val ids1 = datas(8).split(",")
          ids1.map(id => (id, (0, 1, 0)))
        } else if (datas(10) != "null") {
          val ids2 = datas(10).split(",")
          ids2.map(id => (id, (0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    val reduceRDD: RDD[(String, (Int, Int, Int))] = flatmapRDD.reduceByKey(
      (t1, t2) => {
        (t1._1 + t2._2, t2._2 + t1._2, t1._3 + t2._3)
      }
    )
    reduceRDD.sortBy(_._2, false).take(10)

  }

  /**
   * 数据分析优化2
   *
   * @return
   */
  def analysis2() = {
    //读取电商的数据
    val actionRDD: RDD[String] = dao.readFile("input/user_visit_action.txt")
    val flatMapRDD: RDD[(String, HotCategory)] = actionRDD.flatMap(
      action => {
        val datas = action.split("_")
        if (datas(6) != "-1") {
          List((datas(6), HotCategory(datas(6), 1, 0, 0)))
        } else if (datas(8) != "null") {
          val ids1 = datas(8).split(",")
          ids1.map(id => (id, HotCategory(id, 0, 1, 0)))
        } else if (datas(10) != "null") {
          val ids2 = datas(10).split(",")
          ids2.map(id => (id, HotCategory(id, 0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    val reduceRDD: RDD[(String, HotCategory)] = flatMapRDD.reduceByKey(
      (c1, c2) => {
        c1.clickCount = c1.clickCount + c2.clickCount
        c1.oderCount = c1.oderCount + c2.oderCount
        c1.payCount = c1.payCount + c2.payCount
        c1
      }
    )
    reduceRDD.collect.sortWith(
      (left, right) => {
        var leftHC = left._2
        var rightHC = right._2
        if (leftHC.clickCount > rightHC.clickCount) {
          true
        } else if (leftHC.clickCount == rightHC.clickCount) {
          if (leftHC.oderCount > rightHC.oderCount) {
            true
          } else if (leftHC.oderCount == rightHC.oderCount) {
            leftHC.payCount > rightHC.payCount

          } else {
            false
          }
        } else {
          false

        }
      }
    ).take(10)
    //对商品点击的统计 : line => (category,clickCount)
    // TODO 对品类进行点击的统计
    //line =>
    //    click = HotCategory(1, 0, 0)
    //    order = HotCategory(0, 1, 0)
    //    pay   = HotCategory(0, 0, 1)

  }


  /**
   * 数据分析优化3
   * 使用累加器对数据进行聚合
   *
   * @return
   */
  def analysis3() = {
    //读取电商的数据
    val actionRDD = dao.readFile("input/user_visit_action.txt")
    //声明累加器对数据进行聚合，是在helper目录新建的类
    val myAccumulator: HotCategoryAnalysisAccumulator = new HotCategoryAnalysisAccumulator
    EnvUtil.getEnv().register(myAccumulator,"hotcategory")
    actionRDD.foreach(
      //使用累加器
      action =>{
        val datas = action.split("_")
        if (datas(6) != "-1") {
          myAccumulator.add(datas(6),"click")
        } else if (datas(8) != "null") {
          val ids = datas(8).split(",")
          ids.foreach(
            id =>{
              myAccumulator.add(id,"oder")
            }
          )
        } else if (datas(10) != "null") {
          val ids = datas(10).split(",")
          ids.foreach(
            id =>{
              myAccumulator.add(id,"pay")
            }
          )
        } else {
          Nil
        }
      }
    )
    //获取累加器的值
    val accValue: mutable.Map[String, HotCategory] = myAccumulator.value
    val hotCategories: mutable.Iterable[HotCategory] = accValue.map(_._2)
    hotCategories.toList.sortWith(
      (leftHC, rightHC) => {

        if (leftHC.clickCount > rightHC.clickCount) {
          true
        } else if (leftHC.clickCount == rightHC.clickCount) {
          if (leftHC.oderCount > rightHC.oderCount) {
            true
          } else if (leftHC.oderCount == rightHC.oderCount) {
            leftHC.payCount > rightHC.payCount

          } else {
            false
          }
        } else {
          false

        }
      }
    ).take(10)


  }

}
