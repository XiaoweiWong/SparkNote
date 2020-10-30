package com.atguigu.Streaming.req.service

import java.sql.{Connection, Date, PreparedStatement, ResultSet}
import java.text.SimpleDateFormat

import com.atguigu.Streaming.req.bean.Ad_Click_Log
import com.atguigu.Streaming.req.dao.BlackListDao
import com.atguigu.summer.framework.core.TService
import com.atguigu.summer.framework.util.JDBCUtil
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

import scala.collection.mutable.ListBuffer

/**
 * @author david 
 * @create 2020-10-29 上午 11:30 
 */
class BlackListService extends TService{
  private val blackListDao: BlackListDao = new BlackListDao

  override def analysis() {
    val ds: DStream[String] = blackListDao.readKafka()
    //todo 将数据转换为样例类来使用
    val logDS: DStream[Ad_Click_Log] = ds.map(
      data => {
        val datas = data.split(" ")
        Ad_Click_Log(datas(0), datas(1), datas(2), datas(3), datas(4))
      }
    )

    //todo 周期性的获取黑名单的信息，判断当前用户的点击数据是否在黑名单中
    val reduceDS: DStream[((String, String, String), Int)] = logDS.transform(
      rdd => {
        val connection: Connection = JDBCUtil.getConnection()
        val statement: PreparedStatement = connection.prepareStatement(
          """
            |SELECT userid FROM black_list
            |""".stripMargin
        )
        val rs: ResultSet = statement.executeQuery()
        val blackIds = ListBuffer[String]()
        while (rs.next()) {
          blackIds.append(rs.getString(1))
        }
        rs.close()
        statement.close()
        connection.close()

        //如果用户在黑名单中，那么将数据过滤掉，不进行统计
        val filterRDD: RDD[Ad_Click_Log] = rdd.filter(
          log => {
            !blackIds.contains(log.userId)
          }
        )
        //将正常的数据进行点击量的统计
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val keyToOneRDD: RDD[((String, String, String), Int)] = filterRDD.map(
          log => {
            val date: Date = new Date(log.ts.toLong)
            ((sdf.format(date), log.userId, log.adId), 1)
          }
        )
        keyToOneRDD.reduceByKey(_ + _)
      }
    )
    reduceDS.foreachRDD(
      rdd =>{

        val conn = JDBCUtil.getConnection()
        val statement1: PreparedStatement = conn.prepareStatement(
          """
            |insert into user_ad_count(dt,userid,adid,count)
            |values(?,?,?,?)
            |on duplicate key
            |update count = count + ?
            |""".stripMargin
        )
        val statement2: PreparedStatement = conn.prepareStatement(
          """
            |insert into black_list(userid)
            |select  userid from user_ad_count
            |where dt=? and userid=? and adid=? and count>= 100
            |on duplicate key
            |update userid = ?
            |""".stripMargin
        )
        rdd.foreach {

          //每一个采集周期用户点击同一个广告的数量，要统计一天的，要有状态保存
          //思路1：使用算子updateStateByKey => checkpoint => HDFS =>产生大量小文件
          //思路2：统计结果应该放在mysql、redis（过期数据自动清除）中
          case ((day, userid, adid), sum) => {
            //有状态保存
            //新增更新用户点击量
            statement1.setString(1,day)
            statement1.setString(2,userid)
            statement1.setString(3,adid)
            statement1.setLong(4,sum)
            statement1.setLong(5,sum)
            statement1.executeUpdate()
            //获取最新用户的统计数据
            //判断是否超过阈值
            //如果超过阈值，拉入黑名单
            statement2.setString(1,day)
            statement2.setString(2,userid)
            statement2.setString(3,adid)
            statement2.setString(4, userid)
            statement2.executeUpdate()


            statement1.close()
            statement2.close()
            conn.close()
          }
        }
      }
    )





    ds.print()
  }
  /**
   * 数据分析
   *
   * @return
   */
   def analysis1() {
    val ds: DStream[String] = blackListDao.readKafka()
    //todo 将数据转换为样例类来使用
    val logDS: DStream[Ad_Click_Log] = ds.map(
      data => {
        val datas = data.split(" ")
        Ad_Click_Log(datas(0), datas(1), datas(2), datas(3), datas(4))
      }
    )

    //todo 周期性的获取黑名单的信息，判断当前用户的点击数据是否在黑名单中
    val reduceDS: DStream[((String, String, String), Int)] = logDS.transform(
      rdd => {
        val connection: Connection = JDBCUtil.getConnection()
        val statement: PreparedStatement = connection.prepareStatement(
          """
            |SELECT userid FROM black_list
            |""".stripMargin
        )
        val rs: ResultSet = statement.executeQuery()
        val blackIds = ListBuffer[String]()
        while (rs.next()) {
          blackIds.append(rs.getString(1))
        }
        rs.close()
        statement.close()
        connection.close()

        //如果用户在黑名单中，那么将数据过滤掉，不进行统计
        val filterRDD: RDD[Ad_Click_Log] = rdd.filter(
          log => {
            !blackIds.contains(log.userId)
          }
        )
        //将正常的数据进行点击量的统计
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val keyToOneRDD: RDD[((String, String, String), Int)] = filterRDD.map(
          log => {
            val date: Date = new Date(log.ts.toLong)
            ((sdf.format(date), log.userId, log.adId), 1)
          }
        )
        keyToOneRDD.reduceByKey(_ + _)
      }
    )
    reduceDS.foreachRDD(
      rdd =>{
        rdd.foreach {
          //每一个采集周期用户点击同一个广告的数量，要统计一天的，要有状态保存
          //思路1：使用算子updateStateByKey => checkpoint => HDFS =>产生大量小文件
          //思路2：统计结果应该放在mysql、redis（过期数据自动清除）中
          case ((day, userid, adid), sum) => {
              //有状态保存
            //新增更新用户点击量
            val conn = JDBCUtil.getConnection()
            val statement1: PreparedStatement = conn.prepareStatement(
              """
                |insert into user_ad_count(dt,userid,adid,count)
                |values(?,?,?,?)
                |on duplicate key
                |update count = count + ?
                |""".stripMargin
            )
            statement1.setString(1,day)
            statement1.setString(2,userid)
            statement1.setString(3,adid)
            statement1.setLong(4,sum)
            statement1.setLong(5,sum)
            statement1.executeUpdate()
            //获取最新用户的统计数据
            //判断是否超过阈值
            //如果超过阈值，拉入黑名单
            val statement2: PreparedStatement = conn.prepareStatement(
              """
                |insert into black_list(userid)
                |select  userid from user_ad_count
                |where dt=? and userid=? and adid=? and count>= 100
                |on duplicate key
                |update userid = ?
                |""".stripMargin
            )
            statement2.setString(1,day)
            statement2.setString(2,userid)
            statement2.setString(3,adid)
            statement2.setString(4, userid)
            statement2.executeUpdate()


            statement1.close()
            statement2.close()
            conn.close()
          }
        }
      }
    )





    ds.print()
  }
}
