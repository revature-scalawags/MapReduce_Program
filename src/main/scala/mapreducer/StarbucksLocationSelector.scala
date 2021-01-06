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

//Id,StarbucksId,Name,BrandName,StoreNumber,PhoneNumber,OwnershipType,Street1,Street2,Street3,City,CountrySubdivisionCode,CountryCode,PostalCode,Longitude,Latitude,TimezoneOffset,TimezoneId,TimezoneOlsonId,FirstSeen,LastSeen
//1f2d204f-e773-4361-9158-0008307dbd5e,10357,Target Virginia T-847,Starbucks,76666-97597,218-741-6603,LS,1001 13th St S,,,Virginia,MN,US,557923254,-92.55,47.51,-360,Central Standard Time,GMT-06:00 America/Chicago,12/8/2013 5:41:59 PM,2/3/2017 12:00:00 AM
/**
  * This scala map/reduce program performs 2 primary functions:
  *   1. Select only the rows that contain the same city name as appeeared in the mapreduced GPSTrackingOutput.txt.
  *   2. Pull data from Street1 column, City column, and CountrySubdivisionsCode(aka. State) column, 
  *      put them together into a single string, and pass it to the mapper class as a key.
  * 
  *   *[Prerequisites]*
  *     1. StarbucksStoresLocations.csv (raw file)
  *     2. GPSTrackingOutput.txt (mapreduced file)
  *     3. SBT("1.4.4")
  *     4. Scala("2.13.3")
  */


object StarbucksLocationSelector {

  class LocationSelectorMap extends Mapper[LongWritable, Text, Text, IntWritable] {

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

  class LocationSelectorReduce extends Reducer[Text, IntWritable, Text, IntWritable] {
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
    job.setJobName("StarbucksLocationsSelector")
    job.setMapperClass(classOf[LocationSelectorMap])

    job.setReducerClass(classOf[LocationSelectorReduce])

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