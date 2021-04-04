package com.dyurekdeler.etl

import com.dyurekdeler.etl.SparkETL.loadTableAsDF
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import java.util.Properties


class SparkETLTest extends AnyFunSuite with BeforeAndAfterAll {

  @transient var spark: SparkSession = _
  @transient var sourceUrl: String = _
  @transient var connectionProperties: Properties = _

  override def beforeAll(): Unit = {
    spark = SparkSession.builder.appName("ETLTest").master("local[*]").getOrCreate()
    sourceUrl = "jdbc:postgresql://localhost:5432/data-db"

    val username = "postgres"
    val pw = "123456"

    connectionProperties = new Properties()
    connectionProperties.put("user", username)
    connectionProperties.put("password", pw)

  }

  override def afterAll(): Unit = {
    spark.stop()
  }

  test("Products Table Loading"){
    val products = loadTableAsDF(spark, sourceUrl, connectionProperties, "products")
    val productsCount = products.count()
    assert(productsCount==1000, "there are 1000 products in data-db")

  }

  test("Orders Table Loading"){
    val orders = loadTableAsDF(spark, sourceUrl, connectionProperties, "orders")
    val ordersCount = orders.count()
    assert(ordersCount==20000, "there are 20000 orders in data-db")

  }

  test("Order_Items Table Loading"){
    val orderItems = loadTableAsDF(spark, sourceUrl, connectionProperties, "order_items")
    val orderItemsCount = orderItems.count()
    assert(orderItemsCount==23538, "there are 23538 order_items in datatable")

  }


}
