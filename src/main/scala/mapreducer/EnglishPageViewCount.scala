package mapreducer

import java.io.{BufferedReader, FileReader, IOException}
import java.lang.Iterable

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{Text, IntWritable, LongWritable}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.{TextInputFormat, FileInputFormat}
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.Counter
import org.apache.hadoop.util._
import scala.collection.JavaConverters._

/**
  * This scala map/reduce program performs 2 primary functions:
  *   1. Select only English Wikipedia pages from raw dataset and pass them to the mapper class
  *   2. If a page name matches throughout the 24 pageview files, all relevant pageview numbers are combined and printed out as a whole. 
  * 
  *   *[Prerequisites]*
  *     1. Wikipedia Pageview files (raw file)
  *     3. SBT("1.4.4")
  *     4. Scala("2.13.3")
  */

object EnglishPageViewCount {

  class PageViewMap extends Mapper[LongWritable, Text, Text, IntWritable] {

    @throws[IOException]
    override def map(
        key: LongWritable,
        values: Text,
        context: Mapper[LongWritable, Text, Text, IntWritable]#Context
    ): Unit = {
      val line: String = values.toString

      //Displaying only English Wikipedia pages
      if (line.substring(0, 2).equals("en")) {
        val pageName: String = line.split("\\s")(1)
        var pageViews = line.split("\\s")(2).toInt
        context.write(new Text(pageName), new IntWritable(pageViews))
      }
    }
  }

  class PageViewReduce extends Reducer[Text, IntWritable, Text, IntWritable] {
    @throws[IOException]
    override def reduce(
        key: Text,
        values: Iterable[IntWritable],
        context: Reducer[Text, IntWritable, Text, IntWritable]#Context
    ): Unit = {
      var sum = 0
      values.forEach(sum += _.get())
      context.write(key, new IntWritable(sum))
    }
  }

  def main(args: Array[String]): Unit = {
    val job = Job.getInstance()

    job.setJarByClass(this.getClass)
    job.setJobName("EnglishPageViewCount")
    job.setMapperClass(classOf[PageViewMap])

    job.setReducerClass(classOf[PageViewReduce])

    job.setMapOutputKeyClass(classOf[Text])           //Setting the key type emitted by a map class (If smae type as reduce class, it is optional.)
    job.setMapOutputValueClass(classOf[IntWritable])  //Setting the value type emitted by a map class (If smae type as reduce class, it is optional.)
    job.setOutputKeyClass(classOf[Text])              //Setting the key type expected as output from both the map and reduce phases.
    job.setOutputValueClass(classOf[IntWritable])     //Setting the value type expected as output from both the map and reduce phases.


    /** Input Format & Output Format
      * InputFormat             |  Description                                      |  Key                                       |  Value
      * -------------------------------------------------------------------------------------------------------------------------------------------------------
      * TextInputFormat         |  Default format; reads lines of text files        |  The byte offset of the line               |  The line contents
      * KeyValueInputFormat     |  Parses lines into key, val pairs                 |  Everything up to the first tab character  |  The remainder of the line
      * SequenceFileInputFormat |  A Hadoop-specific high-performance binary format |  user-defined                              |  user-defined
      * 
      * 
      * OutputFormat             | Description                                      
      * --------------------------------------------------------------------------------------------------
      * TextOutputFormat         | Default; writes lines in "key \t value" form      
      * SequenceFileOutputFormat | Writes binary files suitable for reading into subsequent MapReduce jobs            
      * NullOutputFormat         | Disregards its inputs
      */

    job.setInputFormatClass(classOf[TextInputFormat])

    FileInputFormat.setInputPaths(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    val success = job.waitForCompletion(true)
    System.exit(if (success) 0 else 1)

  }
}
