# Project 1

## Requirements
[ x ] Create .jar file using a map/reduce program

[ X ] Answer questions using a customized .jar file <br /> <br />
- <img src="https://i.pinimg.com/originals/54/d7/a2/54d7a2f5ab4004654d18686baf3f44c8.jpg" width="200">   
- **[Question #1]** - Find the most viewed English Wiki page on 10/20/2020  
   - Create a customized mapper class to select only English Wiki page data.
   - There are 24 pageview files in one day - (00:00am ~ 23:00pm). If a page name matches throughout the 24 pageview files, all relevant pageview numbers must be combined and printed out as a whole. [Wikipedia Pageview Dataset URL](https://dumps.wikimedia.org/other/pageviews/2020/2020-10/)
        >**Original Pageview Dataset Format** (01:00am file & 02:00am file)<br />
        >`<Language of Wiki Page> | <Page Name> | <Pageview Number> | <N/A>`<br />
        >`ab Москва_1980 11 0`<br />
        >`cd Ureum 251 0`<br />
        >`en 2020Presidential-Election 250 0`<br />
        >`en 2020Presidential-Election 1020 0`<br />

        >**MapReduced Data Output**<br />
        >`2020Presidential-Election 1270`<br />
    - All the mapreduced data output must be sorted by values in a descending order to show only **top 10 most viewed Wiki pages** on 10/20/2020. *Map/Reduce program by default sort the output by keys. 
    - A second map/reduce program is created for sorting by values. Run the second map/reduce program on the ouput produced by the first map/reduce program. <br /><br />
<br /> <br />
- <img src = "https://media-assets-03.thedrum.com/cache/images/thedrum-prod/s3-screen_shot_2019-08-27_at_1.36.42_pm--default--1280.png" width="200">
- **[Question #2]** - What are the top 3 recommended **Starbucks** stores that are geographically the most accessible to me, based on the analysis of my driving data from 11/23/2020 to 12/31/2020 <br /> <br />
    - **[Step 1]** - Show top 5 most appeared addresses in the GPSTracking.csv using a map/reduce program. 
        >**Original GPS Tracker Dataset Format** <br />
        >`<Date> | <Type> | <Speed> | <Address> | <Distance>` <br />
        >`12/31/20 23:11,Heartbeat,1mph,"10910 Brunson Dr, Duluth, GA 30097",778.12` <br />
        >`12/10/20 13:27,Move,11mph,"10910 Brunson Dr, Duluth, GA 30097",620.12` <br />
        >`12/9/20 13:27,Move,17mph,"10910 Brunson Dr, Duluth, GA 30097",601.28` <br />
        >`11/22/20 15:17,Heartbeat,0mph,"6045 Bartam Cove, Duluth, GA 30097",550.12` <br />

        >**MapReduced Data Output**<br />
        >`10910 Brunson Dr, Duluth, GA  3` <br />
        >`6045 Bartam Cove, Duluth, GA  1` <br /> 

    
    - **[Step 2]** - Display U.S. Starbucks stores that are only located in the same city and state as appeared in the addresses from [Step 1].[Starbucks Locations Dataset URL](https://github.com/chrismeller/StarbucksLocations)
        >**Original Starbucks Dataset Format** <br />
        >`<StarbucksID> | <Name> | <BrandName> | <StoreNumber> | <PhoneNumber> | <OwnershipType> | etc...` <br />
        >`1f2d204f-e773-4361-9158-0008307dbd5e,10357,Target Virginia T-847,Starbucks,76666-97597,218-741-6603,LS,1001 13th St S,,,Duluth,GA,US,557923254,-92.55,47.51,-360,Central Standard Time,GMT-06:00 America/Chicago,12/8/2013 5:41:59 PM,2/3/2017 12:00:00 AM` <br />

        >**MapReduced Data Output**<br />
        >`1001 13th St S, Duluth, GA 1` <br />
    - **[Step 3]** - Use Google's Distance Matrix API to compute travel distances between 5 addresses from [Step 1] and each Starbucks store location from [Step 2]. [Google Map's Distance Matrix API](https://developers.google.com/maps/documentation/distance-matrix/start) 
     ![](starbucksDistance.gif) <br />
    - **[Step 4]** - Use an algorithm to recommend top 3 Starbucks stores that are most accessible to me (aka. Top 3 Starbucks Near Me Recommendation). [Separate Repo for Starbucks Near Me Recommendation App](https://github.com/spark131008/Project1-StarbucksLocationRecommendation) <br /> <br />

