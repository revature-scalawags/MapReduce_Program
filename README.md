# Project 1

## Requirements
[ x ] Create .jar file using a map/reduce program

[ X ] Answer questions using a customized .jar file <br /> <br />
- <img src="https://i.pinimg.com/originals/54/d7/a2/54d7a2f5ab4004654d18686baf3f44c8.jpg" width="200">   
- **[Question #1]** - Find the most viewed English Wiki page on 10/20/2020  
   - Create a customized mapper class to select only English Wiki page data.
   - There are 24 pageview files in one day - (00:00am ~ 23:00pm). If a page name matches throughout the 24 pageview files, all relevant pageview numbers must be combined and printed out as a whole. [Wikipedia Pageview Dataset URL](https://dumps.wikimedia.org/other/pageviews/2020/2020-10/)
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
<br /> <br />
- <img src = "https://media-assets-03.thedrum.com/cache/images/thedrum-prod/s3-screen_shot_2019-08-27_at_1.36.42_pm--default--1280.png" width="200">
- **[Question #2]** - What are the top 3 recommended **Starbucks** stores that are geographically the most accessible to me, based on the analysis of my driving data from 11/23/2020 to 12/31/2020 <br /> <br />
    - **[Step 1]** - Show top 5 most appeared addresses in the GPSTracking.csv file using a map/reduce program. <br /> <br />
    - **[Step 2]** - Display U.S. Starbucks stores that are only located in the same city and state as appeared in the addresses from [Step 1].
    [Starbucks Locations Dataset URL](https://github.com/chrismeller/StarbucksLocations) <br /> <br />
    - **[Step 3]** - Use Google's Distance Matrix API to compute travel distances between 5 addresses from [Step 1] and each Starbucks store location from [Step 2]. [Google Map's Distance Matrix API](https://developers.google.com/maps/documentation/distance-matrix/start) <br /> <br />
    - **[Step 4]** - Use an algorithm to recommend top 3 Starbucks stores that are most accessible to me (aka. Top 3 Starbucks Near Me Recommendation). [Separate Repo for Starbucks Near Me Recommendation App](https://github.com/spark131008/Project1-StarbucksLocationRecommendation) <br /> <br />
