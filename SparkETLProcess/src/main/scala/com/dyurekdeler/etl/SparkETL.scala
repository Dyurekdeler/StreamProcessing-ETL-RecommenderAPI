package com.dyurekdeler.etl

import java.util.Properties
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions.{count, desc, rank, sum}


object SparkETL {

  def loadTableAsDF(spark: SparkSession, sourceUrl: String, connectionProperties: Properties, tableName: String): DataFrame ={
    spark.read.jdbc(sourceUrl, tableName, connectionProperties)
  }

  def saveDFAsTable(dataFrame: DataFrame, targetUrl: String, connectionProperties: Properties, tableName: String): Unit ={
    dataFrame.write.mode(SaveMode.Overwrite).jdbc(targetUrl, tableName, connectionProperties)
  }

  def main(args: Array[String]): Unit ={

    Logger.getLogger("org").setLevel(Level.ERROR)

    val spark = SparkSession.builder.appName("ETL").master("local[*]").getOrCreate()

    val sourceUrl = "jdbc:postgresql://localhost:5432/data-db"
    val targetUrl = "jdbc:postgresql://localhost:5432/bestseller-db"
    val username = "postgres"
    val pw = "123456"

    val connectionProperties = new Properties()
    connectionProperties.put("user", username)
    connectionProperties.put("password", pw)

    //read tables from database into dataframes
    val products = loadTableAsDF(spark, sourceUrl, connectionProperties, "products")
    val orders = loadTableAsDF(spark, sourceUrl, connectionProperties, "orders")
    val order_items = loadTableAsDF(spark, sourceUrl, connectionProperties, "order_items")

    //all purchases made
    val allPurchases = orders.join(order_items, usingColumn = "order_id").select("user_id", "product_id")
      .orderBy("user_id").distinct()

    //amount sold for each product
    val saleAmountOfProduct = allPurchases.join(products, usingColumn = "product_id")
      .groupBy("product_id", "category_id").agg(count("*").alias("sale_amount"))
      .orderBy(desc("sale_amount"))

    //rank (append row numbering) saleAmountOfProduct table to filter it later
    val windowSpec  = Window.partitionBy("category_id").orderBy(desc("sale_amount"))
    val saleRanking = saleAmountOfProduct.withColumn("rank",rank.over(windowSpec))

    //filter rank column less than or equal to ten (yields most sold ten products)
    //get top ten bestseller products of each category
    val bestsellerProducts = saleRanking.filter("rank <= 10")
      .select("product_id", "category_id", "sale_amount")

    //save dataframes to database, tables will be created automatically
    saveDFAsTable(products, targetUrl, connectionProperties, "products")
    saveDFAsTable(bestsellerProducts, targetUrl, connectionProperties, "bestseller_product")

    spark.stop()

  }

}
