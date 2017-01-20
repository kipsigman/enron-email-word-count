package kipsigman.enron

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import java.io._

object EmailWordCount {
  
  val minCount = 10
  
  def main(args: Array[String]) {
    
    val conf = new SparkConf().setAppName("Enron Email Word Count")
      // .setMaster("local[4]")
    val sc = new SparkContext(conf)
    
    try {
      // Get content of all email files
      // TODO: This doesn't crawl to sub-folders of users' root mail folders
      val inputPath = "data/maildir/*/*"
      val fileContents = sc.wholeTextFiles(inputPath)
      println(s"Total email count = ${fileContents.count()}")
      
      // Uncomment to debug sub-root mail folder crawling
//      fileContents.foreach(fileContent => {
//        if (fileContent._1.contains("crandell-s/inbox/")) {
//          println(s"Path=${fileContent._1}")
//        }
//      })
      
      // Parse email content into records of headers/body
      val fileEmailRecords = fileContents.map(fileContent => (fileContent._1, EmailParser.parse(fileContent._2)))
      
      // Map to collection of bodies only, as we're only doing word count on these
      val bodies = fileEmailRecords.map(fileRecord => fileRecord._2(EmailParser.Key.Body))
      
      val words = bodies.flatMap(body => {
        // Replace newlines with spaces for ease of tokenizing words
        val bodyNoNewlines = body.trim.replaceAll("""\s*\n\s*""", " ")
        // tokenize words
        bodyNoNewlines.split("""\W+""")
      })
      
      // Get word counts, filter to wods with minimum count, and sort by count desc
      val wordCounts = words.map(word => (word, 1))
        .reduceByKey(_ + _)
        .filter(wordCount => wordCount._2 >= minCount)
        .sortBy(wordCount => wordCount._2, false)
        
      // Write word counts to output file
      val outputPath = "data/out/emails-word-count"
      FileUtil.rmrf(outputPath)
      wordCounts.saveAsTextFile(outputPath)
    } finally {
      sc.stop()
    }
    
  }
}