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

// Id,StarbucksId,Name,BrandName,StoreNumber,PhoneNumber,OwnershipType,Street1,Street2,Street3,City,CountrySubdivisionCode,CountryCode,PostalCode,Longitude,Latitude,TimezoneOffset,TimezoneId,TimezoneOlsonId,FirstSeen,LastSeen
// 1f2d204f-e773-4361-9158-0008307dbd5e,10357,Target Virginia T-847,Starbucks,76666-97597,218-741-6603,LS,1001 13th St S,,,Virginia,MN,US,557923254,-92.55,47.51,-360,Central Standard Time,GMT-06:00 America/Chicago,12/8/2013 5:41:59 PM,2/3/2017 12:00:00 AM
// 072752df-b30b-4f3f-a9b8-000ae32f8666,14667,Target Trumbull T-1956,Starbucks,76306-93245,203-455-0102,LS,120 Hawley Ln,,,Trumbull,CT,US,066115347,-73.15,41.23,-300,Eastern Standard Time,GMT-05:00 America/New_York,12/8/2013 5:41:59 PM,2/3/2017 12:00:00 AM
// d387414f-827a-49b1-8002-000ced29086e,16329,Albertsons-Oceanside #6733,Starbucks,73636-67161,760-729-3468,LS,3450 Marron Rd,,,Oceanside,CA,US,920564672,-117.30,33.18,-480,Pacific Standard Time,GMT-08:00 America/Los_Angeles,12/8/2013 5:41:59 PM,2/3/2017 12:00:00 AM
// c247cc83-e908-4046-a9e1-000de0f73ea5,1015467,SEC Lincoln & Yosemite - Lone Tree,Starbucks,27851-193845,(720) 467-4703,CO,9998 Commons St.,,,Lone Tree,CO,US,80124,-104.88,39.54,-420,Mountain Standard Time,GMT-07:00 America/Denver,8/16/2016 12:00:00 AM,2/3/2017 12:00:00 AM
/**
  * This scala map/reduce program performs 2 primary functions:
  *   1. Select only the rows that contain the same city name and state as appeeared in the q2sortedGPSoutput.txt.
  *   2. Pull data from Street1 column, append city name and state to the Street1 value, and pass the complete address to the mapper class as a key.
  * 
  *   *[Prerequisites]*
  *     1. StarbucksStoresLocations.csv (raw file)
  *     2. q2sortedGPSoutput.txt (mapreduced file)
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

      /** Displaying only the rows that match the following conditions:
       *  - U.S. Starbucks stores only
       *  - City name and state as appeared in the top 5 addresses from q2sortedGPSoutput.txt.
       */
      val wordsInLine = line.split(",")
      val storeChecker: Boolean = wordsInLine.exists(_.equalsIgnoreCase("starbucks"))
      val cityChecker: Boolean = wordsInLine.exists(_.equalsIgnoreCase("duluth"))
      val stateChecker: Boolean = wordsInLine.exists(_.equalsIgnoreCase("ga"))
      val countryChecker: Boolean = wordsInLine.exists(_.equalsIgnoreCase("us"))
      val checkersList = List(storeChecker, cityChecker, stateChecker, countryChecker)
      val multipleConditionChecker: Boolean = checkersList.forall(_ == true) 

      if(multipleConditionChecker){
      val reducedLine = wordsInLine.drop(7)
      val streetName = reducedLine.head
      val completeAddress = streetName +", Duluth, GA"
      context.write(new Text(completeAddress), new IntWritable(1))
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