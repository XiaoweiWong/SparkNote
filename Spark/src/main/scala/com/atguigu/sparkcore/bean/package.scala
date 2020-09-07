package com.atguigu.sparkcore

/**
 * @author david 
 * @create 2020-09-01 下午 9:30 
 */


/**
 * scala中一个文件可以写很多个类，
 * 类名可以不与文件名一样，大量的相似功能的类写在一起，分门别类的便于管理，
 * 相同作用的类写在一个文件内，不要一个类写在一个文件里，可以采用多个类写在一个包对象中
 * 而java中一般是一个文件一个公共类。类名与文件名相同
 */
package object bean {

  case class HotCategory(
                categoryid:String,
                var clickCount:Int,
                var oderCount:Int,
                var payCount:Int
  )
  //用户访问动作表
  case class UserVisitAction(
              date: String,//用户点击行为的日期
              user_id: Long,//用户的ID
              session_id: String,//Session的ID
              page_id: Long,//某个页面的ID
              action_time: String,//动作的时间点
              search_keyword: String,//用户搜索的关键词
              click_category_id: Long,//某一个商品品类的ID
              click_product_id: Long,//某一个商品的ID
              order_category_ids: String,//一次订单中所有品类的ID集合
              order_product_ids: String,//一次订单中所有商品的ID集合
              pay_category_ids: String,//一次支付中所有品类的ID集合
              pay_product_ids: String,//一次支付中所有商品的ID集合
              city_id: Long
                            )//城市 id



}
