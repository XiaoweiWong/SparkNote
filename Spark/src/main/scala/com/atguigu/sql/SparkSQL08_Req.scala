package com.atguigu.sql
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, LongType, MapType, StringType, StructField, StructType}

/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL08_Req {

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "atguigu")
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    //启动hive支持
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    import spark.implicits._
    spark.sql(" use atguigu")
    //spark.sql("show databases")
    //TODO 获取满足条件的数据
    spark.sql(
      """
        |select
        |	a.*,
        |	c.area,
        |	p.product_name,
        | c.city_name
        |from user_visit_action a
        |join city_info c on c.city_id = a.city_id
        |join product_info p on p.product_id = a.click_product_id
        |where a.click_product_id > -1
        """.stripMargin).createOrReplaceTempView("t1")
    //根据区域和商品进行分组，统计商品点击的数量
    //北京、上海、深圳（商品的点击总和，每一个城市点击总和  =>(total,MAP（城市，点击数））
    //自定义聚合函数，in :cityname :String
    //              buffer :缓冲结构 （total,map）
    //              out:remark:String

    val udaf: CityRemarkUDAF = new CityRemarkUDAF



    spark.udf.register("cityRemark",udaf)
    spark.sql(
      """
        |select
        |	area,
        |	product_name,
        |	count(*) as clickCount,
        | cityRemark(city_name)
        |from t1 group by area,product_name
        """.stripMargin).createOrReplaceTempView("t2")
        //将统计结果根据数量进行排序，
    spark.sql(
      """
        |select
        |	*,
        |	rank() over(partition by area order by clickCount desc) as rank
        |from t2
        """.stripMargin).createOrReplaceTempView("t3")
    //将排序结果取前三名
    spark.sql(
      """
        |select
        |	*
        |from t3
        |where rank <= 3
        |
        """.stripMargin).show


    //TODO 自定义聚合函数
    spark.stop()



  }

  /**
   * 自定义聚合函数
   */
  class CityRemarkUDAF extends UserDefinedAggregateFunction{
    /**
     * 重写方法：输入数据类型
     */
    override def inputSchema: StructType ={
      StructType(Array(StructField("cityName",StringType)))
    }

    /**
     * 缓冲区中的数据应该是totalcnt,Map[cityname,cnt]
     * @return
     */
    override def bufferSchema: StructType = {
      StructType(Array(
        StructField("totalcnt",LongType),
        StructField("citymap",MapType(StringType,LongType))
      ))
    }

    /**
     * 返回城市备注的字符串
     * @return
     */
    override def dataType: DataType = {
        StringType
    }

    override def deterministic: Boolean = true

    /**
     * 缓存区格式化
     * @param buffer
     */
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
        buffer(0)=0L  //buffer.update(0,0L)
        buffer(1)= Map[String,Long]()

    }

    /**
     * 更新缓存区
     * @param buffer
     * @param input
     */
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {

      val cityName: String = input.getString(0)//获取一个 字符串

      //点击总和要增加
      buffer(0)=buffer.getLong(0)+1
      //城市点击增加
      val cityMap: Map[String, Long] = buffer.getAs[Map[String,Long]](1)
      val newClickCount = cityMap.getOrElse(cityName,0L)+1
      buffer(1) = cityMap.updated(cityName,newClickCount)

    }

    /**
     * 合并缓冲区
     * @param buffer1
     * @param buffer2
     */
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      //合并点击数量
      buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
      //合并城市点击map
      val map1: Map[String, Long] = buffer1.getAs[Map[String, Long]](1)
      val map2: Map[String, Long] = buffer2.getAs[Map[String, Long]](1)
      buffer1(1) = map1.foldLeft(map2) {
        case (map, (k, v)) => {
          map.updated(k, map.getOrElse(k, 0L) + v)
        }
      }

    }

    /**
     * 对缓冲区进行计算并返回
     * @param buffer
     * @return
     */
    override def evaluate(buffer: Row): Any = {
      val totalcnt: Long = buffer.getLong(0)
      val cityMap = buffer.getMap[String,Long](1)
//      if (cityMap.size > 2){
//
//      }else{
//
//      }
      val cityCountList: List[(String, Long)] = cityMap.toList.sortWith(
        (left, right) => left._2 > right._2
      ).take(2)
      val hasRest = cityMap.size > 2  //还有其他地区城市
      var rest = 0L
      val s = new StringBuilder
      var r = 0L
      cityCountList.foreach{
        case (city,cnt) => {
          r = cnt*100/totalcnt
          s.append(city+" "+r+"%,")
          rest = rest + r
        }
      }
      if (hasRest){
        s.toString()+"其他："+(100-rest)+"%"
      }else{
        s.toString()
      }
    }
  }
}