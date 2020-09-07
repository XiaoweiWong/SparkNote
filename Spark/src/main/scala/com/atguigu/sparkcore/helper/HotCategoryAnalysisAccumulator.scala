package com.atguigu.sparkcore.helper

import com.atguigu.sparkcore.bean.HotCategory
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

/**
 * @author david 
 * @create 2020-09-02 上午 10:50 
 */
/**
 * 继承：定义泛型输入，输出
 * IN:(品类，行为)string,string
 * OUT:map(string，hotcategory)
 *
 * 重写方法
 */
class HotCategoryAnalysisAccumulator extends AccumulatorV2[(String,String),mutable.Map[String,HotCategory]]{

  private val hotCategoryMap= mutable.Map[String, HotCategory]()

  override def isZero: Boolean = hotCategoryMap.isEmpty

  override def copy(): AccumulatorV2[(String, String), mutable.Map[String, HotCategory]] = {
     new HotCategoryAnalysisAccumulator

  }

  override def reset(): Unit = {
    hotCategoryMap.clear()
  }

  override def add(v: (String, String)): Unit = {

    val cid: String = v._1
    val actiontype: String = v._2

    val category: HotCategory = hotCategoryMap.getOrElse(cid,HotCategory(cid,0,0,0))

    actiontype match {
      case "click"=> category.clickCount += 1
      case "pay" => category.payCount += 1
      case "oder" =>category.oderCount += 1
      case _ =>
    }
    hotCategoryMap(cid) = category

  }

  /**
   * 多个累加器合并
   * @param other
   */
  override def merge(other: AccumulatorV2[(String, String), mutable.Map[String, HotCategory]]): Unit = {
    other.value.foreach {//获取两个map的key,value
      case (cid,hotCategory) => {
        val hc: HotCategory = hotCategoryMap.getOrElse(cid, HotCategory(cid, 0, 0, 0))
            hc.clickCount += hotCategory.clickCount
            hc.oderCount += hotCategory.oderCount
            hc.payCount += hotCategory.payCount
            hotCategoryMap(cid)= hc
      }
    }

  }

  override def value: mutable.Map[String, HotCategory] =hotCategoryMap
}
