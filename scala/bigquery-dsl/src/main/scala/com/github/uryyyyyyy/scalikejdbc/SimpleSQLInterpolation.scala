package com.github.uryyyyyyy.scalikejdbc

import com.github.uryyyyyyy.scalikejdbc.tables.CityTable
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.bigquery.{BigQueryOptions, DatasetId}
import scalikejdbc._
import scalikejdbc.bigquery._

object SimpleSQLInterpolation {
  def main(args: Array[String]): Unit = {
    val c = CityTable.syntax("c")
    val projectId = "hoge"
    val datasetId = "mydataset"

    // instantiate BigQuery service and DatsetId
    val credentials = GoogleCredentials.getApplicationDefault()
    val bigQuery = BigQueryOptions.newBuilder()
      .setCredentials(credentials)
      .setProjectId(projectId)
      .build()
      .getService

    val dataset = DatasetId.of(projectId, datasetId)

    // build query by QueryDSL then execute
    val executor = new QueryExecutor(bigQuery, QueryConfig())

    val response = bq {
      select(c.result.*).from(CityTable in dataset as c)
    }.map(CityTable(c.resultName)).list.run(executor)

    val aa = response.result
    aa.foreach(println)
  }
}
