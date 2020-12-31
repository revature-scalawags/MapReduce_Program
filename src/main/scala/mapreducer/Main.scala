package mapreducer


import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.{TextInputFormat, FileInputFormat}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapreduce.lib.output.FileInputFormat
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text

object Main extends App {
  
  if(args.length != 2){
    println("Usage: Main [input directory] [output directory]")
    system.exti(-1)
  }

  val job = Job
}