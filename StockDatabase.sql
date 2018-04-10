drop database if exists stockTicker;
create database if not exists stockTicker;
use stockTicker;

CREATE TABLE stockNames
(	stockabrev	VARCHAR(5) NOT NULL,
  	stockname	VARCHAR(50) NOT NULL,
    
	PRIMARY KEY (stockabrev) );
    
    
INSERT INTO stockNames VALUES ('AAPL', 'Apple Inc.');
INSERT INTO stockNames VALUES ('AMZN', 	'Amazon.com, Inc.');
INSERT INTO stockNames VALUES ('BUD',	'Anheuser-Busch (Budweiser)');
INSERT INTO stockNames VALUES ('CAKE',	'The Cheesecake Factory Incorporated');
INSERT INTO stockNames VALUES ('CBRL',	 'Cracker Barrel Old Country Store, Inc.');
INSERT INTO stockNames VALUES ('CROX ',	'Crocs, Inc.');
INSERT INTO stockNames VALUES ('DENN',	'Denny’s Corporation');
INSERT INTO stockNames VALUES ('DNKN',	'Dunkin’ Brands Group, Inc.');
INSERT INTO stockNames VALUES ('EBAY',	'eBay Inc.');
INSERT INTO stockNames VALUES ('FB', 'Facebook, Inc.');
INSERT INTO stockNames VALUES ('GOOGL',	'Alphabet Inc.');
INSERT INTO stockNames VALUES ('HOG',	'Harley-Davidson Motorcycles');
INSERT INTO stockNames VALUES ('INTC',	'Intel Corporation');
INSERT INTO stockNames VALUES ('MMM',	'3M Company');
INSERT INTO stockNames VALUES ('NDLS',	'Noodles & Company');
INSERT INTO stockNames VALUES ('PZZA',	'Papa John’s International, Inc.');
INSERT INTO stockNames VALUES ('RRGB',	'Red Robin Gourmet Burgers, Inc.');
INSERT INTO stockNames VALUES ('SFLY',	'Shutterfly, Inc.');
INSERT INTO stockNames VALUES ('SNAP',	'Snap Inc.');
INSERT INTO stockNames VALUES ('TRIP',	'TripAdvisor, Inc.');
INSERT INTO stockNames VALUES ('WEN',	'Wendy’s Company (The)');
INSERT INTO stockNames VALUES ('Z',	'Zillow Group, Inc. ');
    
CREATE TABLE portfolio
(	portfolioid	CHAR(4) NOT NULL,
	stockabrev	VARCHAR(5) NOT NULL,
	stockprice		NUMERIC(20,2) NOT NULL,
	stockhigh		NUMERIC(20,2) NOT NULL,
    stocklow		NUMERIC(20,2) NOT NULL,
    stockopen 	NUMERIC(20,2) NOT NULL,
    stockclose 	NUMERIC(20,2),
	stockupdate	TIMESTAMP NOT NULL,
    stocksowned NUMERIC (20, 2),
    
    PRIMARY KEY (portfolioid),
    FOREIGN KEY (stockabrev) REFERENCES stockNames(stockabrev) );
