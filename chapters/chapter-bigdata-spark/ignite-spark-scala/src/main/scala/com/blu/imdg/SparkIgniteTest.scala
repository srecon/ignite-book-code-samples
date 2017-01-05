package com.blu.imdg

import org.apache.ignite.configuration._
import org.apache.ignite.spark.{IgniteContext, IgniteRDD}
import org.apache.spark.{SparkConf, SparkContext}

object RDDProducer extends App {
  val conf = new SparkConf().setAppName("SparkIgniteProducer")
  val sc = new SparkContext(conf)
  val ic = new IgniteContext[Int, Int](sc, () => new IgniteConfiguration())
  val sharedRDD: IgniteRDD[Int, Int] = ic.fromCache("IgniteRDD")
  sharedRDD.savePairs(sc.parallelize(1 to 1000, 10).map(i => (i, i)))
}

object RDDConsumer extends App {
  val conf = new SparkConf().setAppName("SparkIgniteConsume")
  val sc = new SparkContext(conf)
  val ic = new IgniteContext[Int, Int](sc, () => new IgniteConfiguration())
  val sharedRDD = ic.fromCache("IgniteRDD")
  val lessThanTwenty = sharedRDD.filter(_._2 < 20)
  println("The count is:::::::::::: "+lessThanTwenty.count())
}