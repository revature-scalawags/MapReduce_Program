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