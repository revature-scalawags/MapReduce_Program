package mapreducer

import java.io.{BufferedReader, FileReader, IOException}
import java.lang.Iterable
import java.nio.ByteBuffer

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
import org.apache.hadoop.io.WritableComparator
import org.apache.hadoop.mapred.TextOutputFormat



object AscendingSort {

class IntComparator extends WritableComparator{
    override def compare(b1: Array[Byte], s1: Int, l1: Int, b2: Array[Byte], s2: Int, l2: Int): Int = {
        val v1: Int = ByteBuffer.wrap(b1, s1, l1).getInt()
        val v2: Int = ByteBuffer.wrap(b2, s2, l2).getInt()

        return v2.compareTo(v1) * -1
    }
}
  
  class SortMap extends Mapper[LongWritable, Text, IntWritable, Text]{

    @throws[IOException]
    override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, IntWritable, Text]#Context): Unit ={
      val line: String = value.toString
      val tokens = line.split("\\t")
      val valuePart = tokens(1).toInt
        context.write(new IntWritable(valuePart), new Text(tokens(0)))
    }
  }

  class SortReduce extends Reducer[IntWritable, Text, Text, IntWritable]{
    @throws[IOException]
    override def reduce(key: IntWritable, list: Iterable[Text], context: Reducer[IntWritable, Text, Text, IntWritable]#Context): Unit = {
        list.forEach{x =>
            context.write(x, key)
        }
    }
  }


  def main(args: Array[String]): Unit = {
    // val configuration = new Configuration
    val job = Job.getInstance()

    job.setJarByClass(this.getClass)
    job.setJobName("Sort By Value")
    job.setMapperClass(classOf[SortMap])

    // job.setCombinerClass(classOf[PageViewReduce])
    job.setReducerClass(classOf[SortReduce])
    job.setNumReduceTasks(1)

    job.setMapOutputKeyClass(classOf[IntWritable])
    job.setMapOutputValueClass(classOf[Text])
    job.setOutputKeyClass(classOf[IntWritable])
    job.setOutputValueClass(classOf[Text])
    job.setSortComparatorClass(classOf[IntComparator])

    job.setInputFormatClass(classOf[TextInputFormat])

    FileInputFormat.setInputPaths(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    
    val success = job.waitForCompletion(true)
    System.exit(if(success) 0 else 1 )

  }
}