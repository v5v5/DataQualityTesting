Task.
1)Create database from nation.sql script

- open http://127.0.0.1:8081/ in browser
- click Import > Choose file > Select nation.sql
- click button 'Go'


2)Using the data from nation database create the table ‘countryRank’ that will display 
Ranking of countries by its area from largest to smallest. Ranking shold be calculated for regions and continent. 

--
-- Table structure for table `countryRank`
--

DROP TABLE IF EXISTS `countryRank`;
CREATE TABLE `countryRank` (
  `rank` int(11) DEFAULT NULL,
  `continent` varchar(255) DEFAULT NULL,
  `region` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `area` decimal(10,2) DEFAULT NULL,
  `country_code3` char(3) DEFAULT NULL,
  `nday` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8

a)National holiday field shouldn’t  contain NULL values, put ‘no data insead’
b)If notional holiday contains the date in future it should be changed to current date
c)Get rid of the duplicates
Provide sql scrpits and screenshots as evidence.

insert into countryRank (rank, continent, region, name, area, country_code3, nday)
select * from (
	select RANK() OVER (PARTITION BY region ORDER BY area DESC) AS rank,
		cont.name continent, r.name region, coun.name name, area, country_code3, national_day
	from countries coun
	join regions r on r.region_id = coun.region_id 
	join continents cont on cont.continent_id = r.continent_id
	group by coun.name -- Get rid of the duplicates
	order by continent, region, area desc
) a

-- National holiday field shouldn’t  contain NULL values, put ‘no data insead’
-- If notional holiday contains the date in future it should be changed to current date
update countryRank
set nday = CASE 
	WHEN nday is NULL then 'no data'
	WHEN nday > cast(now() as date) then cast(now() as date)
	else nday 
end
where 1

select * 
from countryRank

3)Answer the question – what can be done on a database level to avoid problems with duplicates and Null values?
- in order to avoid duplicates it should use CONSTRAINTS UNIQUE for column `name` of table `countries`
- in order to avoid Null it should do not use `national_day` date DEFAULT NULL, but use `national_day` date NOT NULL


