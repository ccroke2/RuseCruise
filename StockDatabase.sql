drop database if exists stockTicker;
create database if not exists stockTicker;
use stockTicker;

CREATE TABLE stockNames
(	stockid   		CHAR(4)			 NOT NULL,
  	stockabrev	VARCHAR(5) NOT NULL,
	stockname	VARCHAR(50) NOT NULL,
    
	PRIMARY KEY (stockid) );
    
    
INSERT INTO stockNames VALUES ('0001', 'AAPL', 'Apple Inc.');
INSERT INTO stockNames VALUES ('0002', 'AMZN', 	'Amazon.com, Inc.');
INSERT INTO stockNames VALUES ('0003', 'BUD',	'Anheuser-Busch (Budweiser)');
INSERT INTO stockNames VALUES ('0004', 'CAKE',	'The Cheesecake Factory Incorporated');
INSERT INTO stockNames VALUES ('0005', 'CBRL',	 'Cracker Barrel Old Country Store, Inc.');
INSERT INTO stockNames VALUES ('0006', 'CROX ',	'Crocs, Inc.');
INSERT INTO stockNames VALUES ('0007', 'DENN',	'Denny’s Corporation');
INSERT INTO stockNames VALUES ('0008', 'DNKN',	'Dunkin’ Brands Group, Inc.');
INSERT INTO stockNames VALUES ('0009', 'EBAY',	'eBay Inc.');
INSERT INTO stockNames VALUES ('0010', 'FB', 'Facebook, Inc.');
INSERT INTO stockNames VALUES ('0011', 'GOOGL',	'Alphabet Inc.');
INSERT INTO stockNames VALUES ('0012', 'HOG',	'Harley-Davidson Motorcycles');
INSERT INTO stockNames VALUES ('0013', 'INTC',	'Intel Corporation');
INSERT INTO stockNames VALUES ('0014', 'MMM',	'3M Company');
INSERT INTO stockNames VALUES ('0015', 'NDLS',	'Noodles & Company');
INSERT INTO stockNames VALUES ('0016', 'PZZA',	'Papa John’s International, Inc.');
INSERT INTO stockNames VALUES ('0017', 'RRGB',	'Red Robin Gourmet Burgers, Inc.');
INSERT INTO stockNames VALUES ('0018', 'SFLY',	'Shutterfly, Inc.');
INSERT INTO stockNames VALUES ('0019', 'SNAP',	'Snap Inc.');
INSERT INTO stockNames VALUES ('0020', 'TRIP',	'TripAdvisor, Inc.');
INSERT INTO stockNames VALUES ('0021', 'WEN',	'Wendy’s Company (The)');
INSERT INTO stockNames VALUES ('0022', 'Z',	'Zillow Group, Inc. ');
    
CREATE TABLE portfolio
(	portfolioid	CHAR(4),
	stockabrv		VARCHAR(5) NOT NULL,
	stockvol		NUMERIC(20,2) NOT NULL,
	stockhigh		NUMERIC(20,2) NOT NULL,
    stocklow		NUMERIC(20,2) NOT NULL,
    stockopen 	NUMERIC(20,2) NOT NULL,
    stockclose 	NUMERIC(20,2),
 	stockupdate	TIMESTAMP NOT NULL,
    numberofstocks NUMERIC(100),
    PRIMARY KEY (portfolioid),
    FOREIGN KEY (stockabrv) REFERENCES stockNames(stockabrev) );
