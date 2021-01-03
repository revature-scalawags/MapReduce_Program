package mapreducer

import java.io.{BufferedReader, FileReader, IOException}
import java.lang.Iterable
import java.nio.ByteBuffer

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{Text, IntWritable, LongWritable}
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.{TextInputFormat, FileInputFormat, KeyValueTextInputFormat}
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.Counter
import org.apache.hadoop.util._
import scala.collection.JavaConverters._
import org.apache.hadoop.io.WritableComparator

object DescendingSortByValue {

  //Setting up IntComparator class, so that my map/reduce program can sort depending on this algorithm
  class IntComparator extends WritableComparator {
    override def compare(
        b1: Array[Byte],
        s1: Int,
        l1: Int,
        b2: Array[Byte],
        s2: Int,
        l2: Int
    ): Int = {
      val v1: Int = ByteBuffer.wrap(b1, s1, l1).getInt()
      val v2: Int = ByteBuffer.wrap(b2, s2, l2).getInt()

      return v1.compareTo(v2) * -1
    }
  }

  /** SortMap
   * Taking in an input [Text, Text] format  because of [KeyValueTextInputFormat]
   * Writing an output in [IntWritable, Text] format. (valuePart goes to IntWritable for sorting.)
   * All sorting happens in a key phase of a map class
   */
  class SortMap extends Mapper[Text, Text, IntWritable, Text] {
    @throws[IOException]
    override def map(
        key: Text,
        value: Text,
        context: Mapper[Text, Text, IntWritable, Text]#Context
    ): Unit = {
      val keyPart = key.toString
      val valuePart = value.toString.toInt
      context.write(new IntWritable(valuePart), new Text(keyPart))
    }
  }

  /** SortReduce
   * Taking in an input in [IntWritable, Text] format from <SortMap> class
   * No need to sum the IntWritables. This reduce class is only for a sorting purpose.
   * Switching from [IntWritable, Text] format back to [Text, IntWritable] format
   */
  class SortReduce extends Reducer[IntWritable, Text, Text, IntWritable] {
    @throws[IOException]
    override def reduce(
        key: IntWritable,
        values: Iterable[Text],
        context: Reducer[IntWritable, Text, Text, IntWritable]#Context
    ): Unit = {
      values.forEach { x =>
        context.write(x, key)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val job = Job.getInstance()

    job.setJarByClass(this.getClass)
    job.setJobName("Descending Sort By Value")
    job.setMapperClass(classOf[SortMap])

    job.setReducerClass(classOf[SortReduce])
    job.setNumReduceTasks(1)       //Setting the reduce task to 1

    job.setMapOutputKeyClass(classOf[IntWritable])  
    job.setMapOutputValueClass(classOf[Text])       
    job.setOutputKeyClass(classOf[IntWritable])     
    job.setOutputValueClass(classOf[Text])          
    job.setSortComparatorClass(classOf[IntComparator])

    job.setInputFormatClass(classOf[KeyValueTextInputFormat])    //Using [KeyValueTextInputFormat] because it's always taking in mapreduced input

    FileInputFormat.setInputPaths(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    val success = job.waitForCompletion(true)
    System.exit(if (success) 0 else 1)

  }
}
