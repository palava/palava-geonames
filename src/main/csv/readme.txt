The CSV file contains data harvested from http://www.world-airport-codes.com/ and http://www.geonames.org/. Geonames provides a database of all geographical locations in the world (we're interested in cities and countries). Name matching for countries and cities was done to find the correct entry in the geo name data for each entry in airport code. And because I also needed the dutch names, I included them in a very non relational-correct way in the table :). With the geo_name_ids for the city- and country names, you should be able to find a localized name for each airport_codes record in the "Alternate Names" file provided by Geonames. 

See http://download.geonames.org/export/dump/ for Geonames downloads and information.

The CSV file containing the airport codes has the following fields, encoded in unicode:

airport_code
airport_name
city_name
country_name
country_code
latitude
longitude
world_area_code
country_name_nl
city_name_nl
city_name_geo_name_id
country_name_geo_name_id

The latter four are from the geo names data set.

The CSV file was written by MySQL by executing the following command:

SELECT airport_code, airport_name, city_name, country_name, country_code, latitude, longitude, world_area_code, country_name_nl, city_name_nl, city_name_geo_name_id, country_name_geo_name_id 
FROM airport_codes 
INTO OUTFILE './airport_codes_with_geo_name_ids_and_nl_names.csv' 
FIELDS TERMINATED BY ',' 
OPTIONALLY ENCLOSED BY '"' 
LINES TERMINATED BY '\n';


The CSV file containing cities has the following fields:

geo_name_id
latitude
longitude

It was written by this command:

SELECT geo_name_id, latitude, longitude
FROM cities
INTO OUTFILE './city_geo_name_ids_and_coordinates.csv'
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n';



If you need to contact me about errors, suggestions, or whatever, you can send an e-mail message to wiebe@halfgaar.net