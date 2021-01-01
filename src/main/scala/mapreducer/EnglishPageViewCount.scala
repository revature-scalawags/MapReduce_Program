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
import scala.collection.mutable.ArrayBuffer


object EnglishPageViewCount {
  
  class PageViewMap extends Mapper[LongWritable, Text, Text, IntWritable]{

    @throws[IOException]
    override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit ={
      val line: String = value.toString
      
      //Showing only English Wikipedia pages
      if(line.split("\\s")(0).contains("en")){
        val pageName: String = line.split("\\s")(1)
        //var pageViews = new IntWritable(line.split("\\s")(2).toInt)
        line.split("\\s").filter(_.length>0).foreach((word: String) => {
          context.write(new Text(pageName), new IntWritable(1))
        })
      }else{}
    }
  }

  class PageViewReduce extends Reducer[Text, IntWritable, Text, IntWritable]{
    @throws[IOException]
    override def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
      var sum = values.asScala.foldLeft(0)(_+_.get())
      context.write(key, new IntWritable(sum))
    }

  }


  def main(args: Array[String]): Unit = {
    val configuration = new Configuration
    val job = Job.getInstance(configuration, "EnglishPageViewCount")

    job.setJarByClass(this.getClass)
    job.setMapperClass(classOf[PageViewMap])

    job.setCombinerClass(classOf[PageViewReduce])
    job.setReducerClass(classOf[PageViewReduce])

    job.setInputFormatClass(classOf[TextInputFormat])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])

    FileInputFormat.setInputPaths(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    System.exit(if(job.waitForCompletion(true)) 0 else 1 )

  }


}