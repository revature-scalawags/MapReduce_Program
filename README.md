# Project 1

## Requirements
[ x ] Download example mapreduce.jar file into local file system

[ x ] Download wikipedia pageview data into local file system in hadoop

[ X ] Unzip .gz files and move them to hdfs under input directory

[ X ] Run an example map/reduce program with a sample txt file

[ X ] Create my own .jar file with customized filters
- assembly
- plug-in

[ X ] Answer questions using a customized .jar file
- **[Question #1]** - Find the most viewed English Wiki page on 10/20/2020    
   - Create a customized mapper class to select only English Wiki page data.
   - There are 24 pageview files in one day - (00:00am ~ 23:00pm). If a page name matches throughout the 24 pageview files, all relevant pageview numbers must be combined and printed out as a whole. 
        >**Original Dataset Format** (01:00am file & 02:00am file)<br />
        >`<Language of Wiki Page> | <Page Name> | <Pageview Number> | <N/A>`<br />
        >`ab Москва_1980 11 0`<br />
        >`cd Ureum 251 0`<br />
        >`en 2020Presidential-Election 250 0`<br />
        >`en 2020Presidential-Election 1020 0`<br />

        >**MapReduced Data Output**<br />
        >`2020Presidential-Election 1270`<br />
    - All the mapreduced data output must be sorted by values in a descending order to show only **top 10 most viewed Wiki pages** on 10/20/2020. *Map/Reduce program by default sort the output by keys. 
    - A second map/reduce program is created for sorting by values. Run the second map/reduce program on the ouput produced by the first map/reduce program. <br /><br />
- **[Question #2]** - What are the top 3 recommended **Starbucks** stores that are geographically the most accessible to me, based on the analysis of my driving data from 11/23/2020 to 12/31/2020 <br /> <br />
    - **[Step 1]** - Show the most appeared 5 addresses in my GPSTracking.csv file using a map/reduce program. <br /> <br />
    - **[Step 2]** - Select only the Starbucks stores that are located in the same or closest city/state to the 5 addresses from [Step 1].
    [Starbucks Locations Dataset URL](https://github.com/chrismeller/StarbucksLocations) <br /> <br />
    - **[Step 3]** - Use Google Map's Distance Matrix API to compute a travel distance between 5 addresses from [Step 1] and selected Starbucks store locations from [Step 2]. [Google Map's Distance Matrix API](https://developers.google.com/maps/documentation/distance-matrix/start) <br /> <br />
    - **[Step 4]** - Use a simple algorithm to decide top 3 Starbucks stores that are most accessible to me. [Separate Repo for Starbucks Location Recommendation App](https://github.com/spark131008/Project1-StarbucksLocationRecommendation) <br /> <br />
