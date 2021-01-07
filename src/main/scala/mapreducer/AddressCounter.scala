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
  *   1. Select only the rows that contain the same city name as appeeared in the mapreduced GPSTrackingOutput.txt.
  *   2. Pull data from Street1 column, City column, and CountrySubdivisionsCode(aka. State) column, 
  *      put them together into a single string, and pass it to the mapper class as a key.
  * 
  *   *[Prerequisites]*
  *     1. GPSTracking.csv (raw file)
  *     3. SBT("1.4.4")
  *     4. Scala("2.13.3")
  * 
  */

object AddressCounter {

  class AddressCounterMap extends Mapper[LongWritable, Text, Text, IntWritable] {

    @throws[IOException]
    override def map(
        key: LongWritable,
        values: Text,
        context: Mapper[LongWritable, Text, Text, IntWritable]#Context
    ): Unit = {
      val line: String = values.toString
      val count: IntWritable = new IntWritable(1)

      //Excluding heading and rows that show addess as unknown
      if (!line.contains("Date")) {
        if(!line.contains("Unknown")){
          val addressWithZipCode = line.split("\"")(1).split(" ")
          val addressWithoutZipCode = addressWithZipCode.take(addressWithZipCode.length-1)
          var key: String = ""
          for(i <- addressWithoutZipCode){
            key = key + i + " "
          }
        context.write(new Text(key), count)
        }
      }
    }
  }

  class AddressCounterReduce extends Reducer[Text, IntWritable, Text, IntWritable] {
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
    job.setJobName("AddressCounter")
    job.setMapperClass(classOf[AddressCounterMap])

    job.setReducerClass(classOf[AddressCounterReduce])

    job.setMapOutputKeyClass(classOf[Text])        
    job.setMapOutputValueClass(classOf[IntWritable])  
    job.setOutputKeyClass(classOf[Text])            
    job.setOutputValueClass(classOf[IntWritable])     


    job.setInputFormatClass(classOf[TextInputFormat])

    FileInputFormat.setInputPaths(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))

    val success = job.waitForCompletion(true)
    System.exit(if (success) 0 else 1)

  }
}