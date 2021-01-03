# Project 1

## Requirements
[ x ] Download example mapreduce.jar file into local file system

[ x ] Download wikipedia pageview data into local file system in hadoop

[ X ] Unzip .gz files and move them to hdfs under input directory

[ X ] Run sample application text with sample txt file using mapreduce

[ X ] Create my own .jar file to filter out
- assembly
- plug-in

[ X ] Answer questions using a customized .jar file
- **[Question #1]** - Find the most viewed English webpage on Wikipedia on 10/20/2020    
   - Create a customized mapper class to select only English webpage data.
   - There are 24 pageview files in one day - (00:00am ~ 23:00pm). If a page name matches throughout the 24 pageview files, all relevant pageview numbers must be combined and printed as a whole. 
        >**Original Dataset Format** (01:00am file & 02:00am file)<br />
        >`ab Москва_1980 11 0`<br />
        >`cd Ureum 251 5 0`<br />
        >`en 2020Presidential-Election 250 0`<br />
        >`en 2020Presidential-Election 1020 0`<br />

        >**MapReduced Data Result**<br />
        >`2020Presidential-Election 1270`<br />
    - All the mapreduced data result must be sorted in a descending order and show only **top 10 most viewed pages** on Wikipedia on 10/20/2020. *Map/Reduce program by default sort the output by keys.


[  ] use hive to create query and run the query
