package com.blu.imdg

import org.apache.ignite.configuration._
import org.apache.ignite.spark.{IgniteContext, IgniteRDD}
import org.apache.spark.{SparkConf, SparkContext}

object RDDProducer extends App {
  val conf = new SparkConf().setAppName("SparkIgnitePro")
  val sc = new SparkContext(conf)
  val ic = new IgniteContext[Int, Int](sc, () => new IgniteConfiguration())
  val sharedRDD: IgniteRDD[Int, Int] = ic.fromCache("partitioned")
  sharedRDD.savePairs(sc.parallelize(1 to 100000, 10).map(i => (i, i)))
}

object RDDConsumer extends App {
  val conf = new SparkConf().setAppName("SparkIgniteCon")
  val sc = new SparkContext(conf)
  val ic = new IgniteContext[Int, Int](sc, () => new IgniteConfiguration())
  val sharedRDD = ic.fromCache("partitioned")
  val lessThanTen = sharedRDD.filter(_._2 < 10)
  println("The count is:::::::::::: "+lessThanTen.count())
}