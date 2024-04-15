-- =====================================================
-- File Name:    insert_data.sql
-- Instructor:   Nguyen Thai
-- Student:      Jadon Watson
-- Date:         4/3/2022
-- Description:  To insert all data for tables created by my partner fabio:
--                  LittleEataly_T
--                  Supplier_T
--                  Employee_T
--                  Hourly_Employee_T
--                  Salaried_Employee_T
--                  Customer_T
--                  RewardsProgram_T
--                  Order_T
--                  Renovation_T
--                  Equipment_T
--                  Expense_T
--                  Revenue_T
--                  Pasta_T
--                  Salad_T
--                  Sandwich_T
--                  Appetizer_T
--                  Dessert_T
--                  NonAlcoholicDrink_T
--                  AlcoholicDrink_T
-- =====================================================
--Insert Data for LittleEataly_T Table
INSERT INTO LittleEataly_T VALUES (00000000001, '25 High St.','Boston', 'MA', '02110', '6172222222', 'Yes');
INSERT INTO LittleEataly_T VALUES (00000000002, '112 Sulphur Springs St.','Boston', 'MA', '02110', '6175550120', 'Yes');
INSERT INTO LittleEataly_T VALUES (00000000003, '10 Heather St.','Boston', 'MA', '02110', '6175550149', 'Yes');
INSERT INTO LittleEataly_T VALUES (00000000004, '5 Williams Dr.','Boston', 'MA', '02110', '6175550149', 'No');
INSERT INTO LittleEataly_T VALUES (00000000005, '67 Argyle St.','Boston', 'MA', '02110', '6174569654', 'Yes');

--Insert Data for Supplier_T Table
INSERT INTO Supplier_T VALUES (00000000001, 1, 'Allie Meats', '94 Beech St.', 'Quincy', 'MA', '02170','6175450136');
INSERT INTO Supplier_T VALUES (00000000002, 2, 'Ryans Produce', '40 Primrose Road','Brooklyn', 'NY', '11238','2122889543');
INSERT INTO Supplier_T VALUES (00000000003, 3, 'Family Wine and Spirites', '563 Bills Ave.', 'Quincy', 'MA', '02170','6178504936');
INSERT INTO Supplier_T VALUES (00000000004, 4, 'Sally Bakery', '104 Rich Rd.','Boston', 'MA', '02115','6179954305');
INSERT INTO Supplier_T VALUES (00000000005, 5, 'Lysco Corp', '66 Winter St.', 'Plympton','MA', '02367','7814222300');

--Insert Data for Employee_T Table
--H means hourly and S means Salary
--location(Little Eataly) 1
INSERT INTO Employee_T VALUES (00000000001, 1, 'Noelle Martinez', 'General Manager','H', '6174170366', 40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000002, 1, 'Khloe West', 'Head Chef','S', '6174512064',40,'Wehealth','None','Family Retirement');
INSERT INTO Employee_T VALUES (00000000003, 1, 'Chloe Wilson', 'Beverage Manager','S', '6171531932', 40, 'Uptom','Maker Life Insureance','None');
INSERT INTO Employee_T VALUES (00000000004, 1, 'Kaylee West', 'cook','S', '6172177497',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000005, 1, 'Calvin Marley', 'Chef','S', '6171587855',40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000006, 1, 'Harmony Johnson', 'Chef','S', '6175213810',40,'Bizer','Future Life','Good Years');
INSERT INTO Employee_T VALUES (00000000007, 1, 'Juniper Clark', 'Cashier','H', '6172799997',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000008, 1, 'Hazel Jackson', 'Cashier','H', '6171167647',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000009, 1, 'Layla Solace', 'Runner','H', '6177154842',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000010, 1, 'Reagan Sharpe', 'Runner','H', '6171162811',40,'Wehealth','Maker Life Insureance','Good Years');
--location(Little Eataly) 2
INSERT INTO Employee_T VALUES (00000000011, 2, 'Vincent Davis', 'General Manager', 'H', '6171068704', 40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000012, 2, 'Kobe Jones', 'Head Chef', 'S', '6178114634',40,'Wehealth','None','Family Retirement');
INSERT INTO Employee_T VALUES (00000000013, 2, 'Mason May', 'Beverage Manager','S', '6175873601', 40, 'Uptom','Maker Life Insureance','None');
INSERT INTO Employee_T VALUES (00000000014, 2, 'Nora Melenia', 'cook','S', '6173689287',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000015, 2, 'Kaylee Bolt', 'Chef','S', '6175716951',40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000016, 2, 'Bryce Harris', 'Chef', 'S', '6171267077',40,'Bizer','Future Life','Good Years');
INSERT INTO Employee_T VALUES (00000000017, 2, 'Beckett Hernandez', 'Cashier','H', '6172241450',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000018, 2, 'Catalina Bardot', 'Cashier','H', '6178381993',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000019, 2, 'Ruby Baker', 'Runner','H', '6175264431',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000020, 2, 'Ada Daughtler', 'Runner','H', '6177421371',40,'Wehealth','Maker Life Insureance','Good Years');
--location(Little Eataly) 3
INSERT INTO Employee_T VALUES (00000000021, 3, 'Joseph Clark', 'General Manager', 'H', '6175316064', 40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000022, 3, 'Scarlett Jenkins', 'Head Chef','S', '6174125826',40,'Wehealth','None','Family Retirement');
INSERT INTO Employee_T VALUES (00000000023, 3, 'Carson Williams', 'Beverage Manager','S', '6175857883', 40, 'Uptom','Maker Life Insureance','None');
INSERT INTO Employee_T VALUES (00000000024, 3, 'Londyn Monroe', 'cook','S', '6174197507',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000025, 3, 'Kenneth Harris', 'Chef','S', '6174729928',40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000026, 3, 'Georgia Melenia', 'Chef','S', '6173618942',40,'Bizer','Future Life','Good Years');
INSERT INTO Employee_T VALUES (00000000027, 3, 'Angel May', 'Cashier', 'H', '6171683894',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000028, 3, 'Skylar Sharpe', 'Cashier','H', '6173934814',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000029, 3, 'Gianna Holly', 'Runner','H', '6174324098',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000030, 3, 'Raelynn Lee', 'Runner','H', '6179699157',40,'Wehealth','Maker Life Insureance','Good Years');
--location(Little Eataly) 4
INSERT INTO Employee_T VALUES (00000000031, 4, 'Chase Davis', 'General Manager', 'H', '6172682533', 40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000032, 4, 'Bailey King', 'Head Chef','S', '6177034932',40,'Wehealth','None','Family Retirement');
INSERT INTO Employee_T VALUES (00000000033, 4, 'Ellie Peterson', 'Beverage Manager','S', '6178090291', 40, 'Uptom','Maker Life Insureance','None');
INSERT INTO Employee_T VALUES (00000000034, 4, 'Landon Garcia', 'cook', 'S', '6171280811',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000035, 4, 'Lola Moore', 'Chef', 'S', '6176904219',40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000036, 4, 'Ariana May', 'Chef', 'S', '6176088391',40,'Bizer','Future Life','Good Years');
INSERT INTO Employee_T VALUES (00000000037, 4, 'Samantha West', 'Cashier', 'H', '6171186832',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000038, 4, 'Georgia Gonzales', 'Cashier', 'H', '6171208153',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000039, 4, 'Lucas Miller', 'Runner', 'H', '6177203147',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000040, 4, 'Valerie Moore', 'Runner', 'H', '6179305630',40,'Wehealth','Maker Life Insureance','Good Years');
--location(Little Eataly) 5
INSERT INTO Employee_T VALUES (00000000041, 5, 'Weston Holly', 'General Manager', 'H', '6178632685', 40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000042, 5, 'Lorenzo Martinez', 'Head Chef', 'S', '6177475751',40,'Wehealth','None','Family Retirement');
INSERT INTO Employee_T VALUES (00000000043, 5, 'Arthur Gonzales', 'Beverage Manager','S', '6172701279', 40, 'Uptom','Maker Life Insureance','None');
INSERT INTO Employee_T VALUES (00000000044, 5, 'Micah Scott', 'cook', 'S', '6171762918',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000045, 5, 'Caleb Jones', 'Chef', 'S', '6175074748',40,'Uptom','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000046, 5, 'Leo Tyson', 'Chef', 'S', '6174988640',40,'Bizer','Future Life','Good Years');
INSERT INTO Employee_T VALUES (00000000047, 5, 'Beau Hansley', 'Cashier', 'H', '6179609721',40,'Health of World','Greater Boston Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000048, 5, 'Zoey White', 'Cashier', 'H', '6173440873',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000049, 5, 'Jade Walker', 'Runner', 'H', '6172727963',40,'Uptom','Maker Life Insureance','Good Years');
INSERT INTO Employee_T VALUES (00000000050, 5, 'Allison Lopez', 'Runner', 'H', '6173438688',40,'Wehealth','Maker Life Insureance','Good Years');

--Employees that Work Hourly 
INSERT INTO Hourly_Employee_T VALUES (00000000001, 27 );
INSERT INTO Hourly_Employee_T VALUES (00000000007, 15.25 );
INSERT INTO Hourly_Employee_T VALUES (00000000008, 15.25 );
INSERT INTO Hourly_Employee_T VALUES (00000000009, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000010, 15.25 );
INSERT INTO Hourly_Employee_T VALUES (00000000011, 27);
INSERT INTO Hourly_Employee_T VALUES (00000000017,15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000018, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000019, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000020, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000021, 27);
INSERT INTO Hourly_Employee_T VALUES (00000000027,15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000028, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000029, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000030, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000031, 27);
INSERT INTO Hourly_Employee_T VALUES (00000000037,15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000038, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000039, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000040, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000041, 27);
INSERT INTO Hourly_Employee_T VALUES (00000000047,15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000048, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000049, 15.25);
INSERT INTO Hourly_Employee_T VALUES (00000000050, 15.25);
--Employees that Work With Salary
INSERT INTO Salaried_Employee_T VALUES (00000000002, 51308);
INSERT INTO Salaried_Employee_T VALUES (00000000003, 65662);
INSERT INTO Salaried_Employee_T VALUES (00000000004, 35000);
INSERT INTO Salaried_Employee_T VALUES (00000000005, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000006, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000012, 51308);
INSERT INTO Salaried_Employee_T VALUES (00000000013, 65662);
INSERT INTO Salaried_Employee_T VALUES (00000000014, 35000);
INSERT INTO Salaried_Employee_T VALUES (00000000015, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000016, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000022, 51308);
INSERT INTO Salaried_Employee_T VALUES (00000000023, 65662);
INSERT INTO Salaried_Employee_T VALUES (00000000024, 35000);
INSERT INTO Salaried_Employee_T VALUES (00000000025, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000026, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000032, 51308);
INSERT INTO Salaried_Employee_T VALUES (00000000033, 65662);
INSERT INTO Salaried_Employee_T VALUES (00000000034, 35000);
INSERT INTO Salaried_Employee_T VALUES (00000000035, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000036, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000042, 51308);
INSERT INTO Salaried_Employee_T VALUES (00000000043, 65662);
INSERT INTO Salaried_Employee_T VALUES (00000000044, 35000);
INSERT INTO Salaried_Employee_T VALUES (00000000045, 49000);
INSERT INTO Salaried_Employee_T VALUES (00000000046, 49000);

--Customer data insert LittleEataly 1
INSERT INTO Customer_T VALUES (00000000001,1,'Bennett Hernandez','2541557395',25,'Yes');
INSERT INTO Customer_T VALUES (00000000002,1,'Sara Ashley','4233981292',25,'No');
INSERT INTO Customer_T VALUES (00000000003,1,'Vivian Williams','6173981292',25,'No');
INSERT INTO Customer_T VALUES (00000000004,1,'Kamila Thatcher','6179226739',25,'Yes');
INSERT INTO Customer_T VALUES (00000000005,1,'Luna Baker','6179226739',25,'No');
--Customer data insert LittleEataly 2
INSERT INTO Customer_T VALUES (00000000006,2,'Bryson Tyson','9016505005',25,'Yes');
INSERT INTO Customer_T VALUES (00000000007,2,'Ivy Langley','2547289631',25,'No');
INSERT INTO Customer_T VALUES (00000000008,2,'Hudson Bolt','6178114620',25,'Yes');
INSERT INTO Customer_T VALUES (00000000009,2,'Parker Tyson','6515363370',25,'No');
INSERT INTO Customer_T VALUES (00000000010,2,'Emmanuel Cullen','4135288160',25,'Yes');
--Customer data insert LittleEataly 3
INSERT INTO Customer_T VALUES (00000000011,3,'Jeremiah Gonzalez','2125721835',25,'No');
INSERT INTO Customer_T VALUES (00000000012,3,'Jasmine Poverly','5136070716',25,'No');
INSERT INTO Customer_T VALUES (00000000013,3,'Zion Anderson','9518988967',25,'No');
INSERT INTO Customer_T VALUES (00000000014,3,'Jose Green','9854333787',25,'No');
INSERT INTO Customer_T VALUES (00000000015,3,'Finn Noel','6171991377',25,'Yes');
--Customer data insert LittleEataly 4
INSERT INTO Customer_T VALUES (00000000016,4,'Ezra Daughtler','6174919473',25,'Yes');
INSERT INTO Customer_T VALUES (00000000017,4,'Annabelle Carter','6171300046',25,'No');
INSERT INTO Customer_T VALUES (00000000018,4,'Kevin Cassidy','7193431071',25,'No');
INSERT INTO Customer_T VALUES (00000000019,4,'Richard Torres','3055888646',25,'No');
INSERT INTO Customer_T VALUES (00000000020,4,'Wesley Poverly','6178356923',25,'Yes');
--Customer data insert LittleEataly 5
INSERT INTO Customer_T VALUES (00000000021,5,'Ivan Green','6172119499',25,'Yes');
INSERT INTO Customer_T VALUES (00000000022,5,'Asher Nelson','6173289941',25,'No');
INSERT INTO Customer_T VALUES (00000000023,5,'Colton Campbell','7191778519',25,'No');
INSERT INTO Customer_T VALUES (00000000024,5,'Jude Davis','6179729431',25,'No');
INSERT INTO Customer_T VALUES (00000000025,5,'Bennett Marley','6173986691',25,'Yes');
--data insert for Rewards Program and what customers are in Rewards Programs
INSERT INTO RewardsProgram_T VALUES (0000000001,1,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000002,2,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000003,3,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000004,4,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000005,5,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000006,6,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000007,7,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000008,8,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000009,9,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000010,10,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000011,11,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000012,12,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000013,13,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000014,14,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000015,15,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000016,16,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000017,17,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000018,18,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000019,19,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000020,20,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000021,21,'Yes','Yes');
INSERT INTO RewardsProgram_T VALUES (0000000022,22,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000023,23,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000024,24,'No','No');
INSERT INTO RewardsProgram_T VALUES (0000000025,25,'Yes','Yes');
--insert data for Order table 
INSERT INTO Order_T VALUES (0000000001,1,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000002,2,'Yes','No');
INSERT INTO Order_T VALUES (0000000003,3,'Yes','No');
INSERT INTO Order_T VALUES (0000000004,4,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000005,5,'No','Yes');
INSERT INTO Order_T VALUES (0000000006,6,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000007,7,'Yes','No');
INSERT INTO Order_T VALUES (0000000008,8,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000009,9,'No','No');
INSERT INTO Order_T VALUES (0000000010,10,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000011,11,'No','No');
INSERT INTO Order_T VALUES (0000000012,12,'No','Yes');
INSERT INTO Order_T VALUES (0000000013,13,'Yes','No');
INSERT INTO Order_T VALUES (0000000014,14,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000015,15,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000016,16,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000017,17,'No','Yes');
INSERT INTO Order_T VALUES (0000000018,18,'Yes','No');
INSERT INTO Order_T VALUES (0000000019,19,'Yes','No');
INSERT INTO Order_T VALUES (0000000020,20,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000021,21,'Yes','Yes');
INSERT INTO Order_T VALUES (0000000022,22,'Yes','No');
INSERT INTO Order_T VALUES (0000000023,23,'Yes','No');
INSERT INTO Order_T VALUES (0000000024,24,'No','Yes');
INSERT INTO Order_T VALUES (0000000025,25,'Yes','Yes');
--insert data for Renovation at LittleEatly locations
INSERT INTO Renovation_T VALUES (0000000001,1,'1 Table',0);
INSERT INTO Renovation_T VALUES (0000000002,2,'1 Table',0);
INSERT INTO Renovation_T VALUES (0000000003,3,'Private Wine Seller',55);
INSERT INTO Renovation_T VALUES (0000000004,4,'2 Table',0);
INSERT INTO Renovation_T VALUES (0000000005,5,'1 Table',0);
--insert data for Equipment
--Little Eately 1
INSERT INTO Equipment_T VALUES (0000000001,1,'Dough Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000002,1,'Commercial Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000003,1,'Commercial CanOpener',4,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000004,1,'Commercial Processor',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000005,1,'Commercial Ovens',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000006,1,'Refrigerated Tables',1,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000007,1,'Commercial Slicers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000008,1,'Reach Refrigerators',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000009,1,'Commercial Fryers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000010,1,'Pizza Boxes in Bulk',300,100,'Yes');
INSERT INTO Equipment_T VALUES (0000000011,1,'Take-Out Containers',300,125,'Yes');
INSERT INTO Equipment_T VALUES (0000000012,1,'Washable Plates',50,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000013,1,'Storage Containers',300,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000014,1,'Frying Pans',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000015,1,'Pasta Cookers',6,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000016,1,'Sause Pots',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000017,1,'Kitchen Spoons',10,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000018,1,'Saute Pans',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000019,1,'Cookware',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000020,1,'Eating Utensil set',50,0,'Yes');
--Little Eately 2
INSERT INTO Equipment_T VALUES (0000000021,2,'Dough Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000022,2,'Commercial Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000023,2,'Commercial CanOpener',4,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000024,2,'CommercialProcessors',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000025,2,'Commercial Ovens',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000026,2,'Refrigerated Tables',1,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000027,2,'Commercial Slicers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000028,2,'Reach Refrigerators',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000029,2,'Commercial Fryers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000030,2,'Pizza Boxes in Bulk',300,100,'Yes');
INSERT INTO Equipment_T VALUES (0000000031,2,'Take-Out Containers',300,125,'Yes');
INSERT INTO Equipment_T VALUES (0000000032,2,'Washable Plates',50,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000033,2,'Storage Containers',300,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000034,2,'Frying Pans',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000035,2,'Pasta Cookers',6,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000036,2,'Sause Pots',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000037,2,'Kitchen Spoons',10,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000038,2,'Saute Pans',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000039,2,'Cookware',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000040,2,'Eating Utensil set',50,0,'Yes');
--Little Eately 3
INSERT INTO Equipment_T VALUES (0000000041,3,'Dough Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000042,3,'Commercial Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000043,3,'Commercial CanOpener',4,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000044,3,'CommercialProcessors',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000045,3,'Commercial Ovens',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000046,3,'Refrigerated Tables',1,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000047,3,'Commercial Slicers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000048,3,'Reach Refrigerators',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000049,3,'Commercial Fryers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000050,3,'Pizza Boxes in Bulk',300,100,'Yes');
INSERT INTO Equipment_T VALUES (0000000051,3,'Take-Out Containers',300,125,'Yes');
INSERT INTO Equipment_T VALUES (0000000052,3,'Washable Plates',50,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000053,3,'Storage Containers',300,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000054,3,'Frying Pans',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000055,3,'Pasta Cookers',6,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000056,3,'Sause Pots',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000057,3,'Kitchen Spoons',10,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000058,3,'Saute Pans',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000059,3,'Cookware',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000060,3,'Eating Utensil set',50,0,'Yes');
--Little Eately 4
INSERT INTO Equipment_T VALUES (0000000061,4,'Dough Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000062,4,'Commercial Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000063,4,'Commercial CanOpener',4,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000064,4,'CommercialProcessors',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000065,4,'Commercial Ovens',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000066,4,'Refrigerated Tables',1,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000067,4,'Commercial Slicers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000068,4,'Reach Refrigerators',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000069,4,'Commercial Fryers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000070,4,'Pizza Boxes in Bulk',300,100,'Yes');
INSERT INTO Equipment_T VALUES (0000000071,4,'Take-Out Containers',300,125,'Yes');
INSERT INTO Equipment_T VALUES (0000000072,4,'Washable Plates',50,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000073,4,'Storage Containers',300,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000074,4,'Frying Pans',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000075,4,'Pasta Cookers',6,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000076,4,'Sause Pots',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000077,4,'Kitchen Spoons',10,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000078,4,'Saute Pans',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000079,4,'Cookware',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000080,4,'Eating Utensil set',50,0,'Yes');
--Little Eately 5
INSERT INTO Equipment_T VALUES (0000000081,5,'Dough Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000082,5,'Commercial Mixers',3,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000083,5,'Commercial CanOpener',4,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000084,5,'CommercialProcessors',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000085,5,'Commercial Ovens',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000086,5,'Refrigerated Tables',1,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000087,5,'Commercial Slicers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000088,5,'Reach Refrigerators',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000089,5,'Commercial Fryers',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000090,5,'Pizza Boxes in Bulk',300,100,'Yes');
INSERT INTO Equipment_T VALUES (0000000091,5,'Take-Out Containers',300,125,'Yes');
INSERT INTO Equipment_T VALUES (0000000092,5,'Washable Plates',50,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000093,5,'Storage Containers',300,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000094,5,'Frying Pans',2,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000095,5,'Pasta Cookers',6,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000096,5,'Sause Pots',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000097,5,'Kitchen Spoons',10,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000098,5,'Saute Pans',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000099,5,'Cookware',5,0,'Yes');
INSERT INTO Equipment_T VALUES (0000000100,5,'Eating Utensil set',50,0,'Yes');
--Expenses for each Little Eataly Monthly
--all this is estments from the avarage in boston for restrants 
INSERT INTO Expense_T VALUES (0000000001,1,33600,15000,33600,33600,666,100,30,175,1200,12500,3360);
INSERT INTO Expense_T VALUES (0000000002,2,33600,15000,33600,33600,666,100,30,175,1200,12500,3360);
INSERT INTO Expense_T VALUES (0000000003,3,33600,95000,33600,33600,666,100,30,175,1200,12500,3360);
INSERT INTO Expense_T VALUES (0000000004,4,33600,15000,33600,33600,666,100,30,175,1200,12500,3360);
INSERT INTO Expense_T VALUES (0000000005,5,33600,15000,33600,33600,666,100,30,175,1200,12500,3360);
--Revenue of each Little Eataly Location
INSERT INTO Revenue_T VALUES(0000000001,1,208831);
INSERT INTO Revenue_T VALUES(0000000002,2,206881);
INSERT INTO Revenue_T VALUES(0000000003,3,238831);
INSERT INTO Revenue_T VALUES(0000000004,4,210438);
INSERT INTO Revenue_T VALUES(0000000005,5,210031);
--Pasta
--Pasta data insert LittleEataly 1
INSERT INTO Pasta_T VALUES(0000000001,1,1,'Bolognese','Beef','Mushroom','Extra Truffles','Fettucine',25);
INSERT INTO Pasta_T VALUES(0000000002,1,2,'Alfredo','Shrimp','Onion','None','Linguine',17);
INSERT INTO Pasta_T VALUES(0000000003,1,3,'Carbonara','Chicken','Broccoli','None','Fettucine',17);
INSERT INTO Pasta_T VALUES(0000000004,1,4,'Pesto','Beef','Mushroom','Extra Truffles','Fettucine',25);
INSERT INTO Pasta_T VALUES(0000000005,1,5,'Marinara','Shrimp','Broccoli','None','Penne',17);
--Pasta data insert LittleEataly 2
INSERT INTO Pasta_T VALUES(0000000006,2,6,'Alfredo','Chicken','Broccoli','None','Spaghetti',17);
INSERT INTO Pasta_T VALUES(0000000007,2,7,'Ragu','Beef','Onion','None','Penne',17);
INSERT INTO Pasta_T VALUES(0000000009,2,8,'Alfredo','Chicken','Broccoli','None','Spaghetti',17);
INSERT INTO Pasta_T VALUES(0000000010,2,9,'Pesto','Beef','Mushroom','None','Fettucine',17);
--Pasta data insert LittleEataly 3
INSERT INTO Pasta_T VALUES(0000000011,3,13,'Pesto','Chicken','Onion','None','Penne',17);
INSERT INTO Pasta_T VALUES(0000000012,3,15,'Alfredo','Chicken','Broccoli','None','Spaghetti',17);
--Pasta data insert LittleEataly 4
INSERT INTO Pasta_T VALUES(0000000013,4,17,'Carbonara','Beef','Onion','Extra Cheese','Linguine',20);
--Pasta data insert LittleEataly 5
INSERT INTO Pasta_T VALUES(0000000014,5,23,'Alfredo','Chicken','Broccoli','None','Spaghetti',17);
--Salad
--Salad data insert LittleEataly 1
INSERT INTO Salad_T VALUES(0000000001,1,4,'No','Yes','Yes','Caesar',15);
--Salad data insert LittleEataly 2
INSERT INTO Salad_T VALUES(0000000002,2,5,'No','Yes','Yes','Ranch',15);
--Salad data insert LittleEataly 3
INSERT INTO Salad_T VALUES(0000000003,3,13,'No','Yes','No','Caesar',15);
--Salad data insert LittleEataly 4
INSERT INTO Salad_T VALUES(0000000004,4,17,'Yes','Yes','Yes','Ranch',15);
--Salad data insert LittleEataly 5
INSERT INTO Salad_T VALUES(0000000005,5,24,'No','Yes','No','Italian',15);
--Sandwich
--Sandwich data insert LittleEataly 1
INSERT INTO Sandwich_T VALUES(0000000001,1,2,'Turkey','American','Mustard','Yes','No','No',15);
--Sandwich data insert LittleEataly 2
INSERT INTO Sandwich_T VALUES(0000000002,2,8,'Ham','Cheddar','Mayonnaise','Yes','No','No',15);
--Sandwich data insert LittleEataly 3
INSERT INTO Sandwich_T VALUES(0000000003,3,15,'Beef','American','Mustard','Yes','No','Yes',15);
--Sandwich data insert LittleEataly 4
INSERT INTO Sandwich_T VALUES(0000000004,4,17,'Ham','Cheddar','Mayonnaise','Yes','Yes','No',15);
--Sandwich data insert LittleEataly 5
INSERT INTO Sandwich_T VALUES(0000000005,5,21,'Beef','Swiss','Mayonnaise','No','Yes','Yes',15);
--Appetizer
--Appetizer data insert LittleEataly 1
INSERT INTO Appetizer_T VALUES(0000000001,1,4,'Soup of the day',8);
--Appetizer data insert LittleEataly 2
INSERT INTO Appetizer_T VALUES(0000000002,2,5,'Bread Sticks',2);
--Appetizer data insert LittleEataly 3
INSERT INTO Appetizer_T VALUES(0000000003,3,14,'Large Fries',7);
--Appetizer data insert LittleEataly 4
INSERT INTO Appetizer_T VALUES(0000000004,4,18,'Small Fries',5);
--Appetizer data insert LittleEataly 5
INSERT INTO Appetizer_T VALUES(0000000005,5,23,'Calanari',9);
--Dessert
--Dessert data insert LittleEataly 1
INSERT INTO Dessert_T VALUES(0000000001,1,3,'Medium','Chocolate','Yes','No',12);
--Dessert data insert LittleEataly 2
INSERT INTO Dessert_T VALUES(0000000002,1,9,'Large','Vanilla','Yes','Yes',14);
--Dessert data insert LittleEataly 3
INSERT INTO Dessert_T VALUES(0000000003,1,12,'Medium','Strawberry','No','Yes',12);
--Dessert data insert LittleEataly 4
INSERT INTO Dessert_T VALUES(0000000004,1,16,'Small','Vanilla','Yes','No',10);
--Dessert data insert LittleEataly 5
INSERT INTO Dessert_T VALUES(0000000005,1,24,'Medium','Chocolate','No','Yes',12);
--NonAlcoholicDrink
--NonAlcoholicDrink data insert LittleEataly 1
INSERT INTO NonAlcoholicDrink_T VALUES(0000000001,1,5,'Lemonade','Happy Lemon',4);
--NonAlcoholicDrink data insert LittleEataly 2
INSERT INTO NonAlcoholicDrink_T VALUES(0000000002,2,10,'Water','Besoni',2);
--NonAlcoholicDrink data insert LittleEataly 3
INSERT INTO NonAlcoholicDrink_T VALUES(0000000003,3,13,'Moke','Moko cola',4);
--NonAlcoholicDrink data insert LittleEataly 4
INSERT INTO NonAlcoholicDrink_T VALUES(0000000004,4,17,'Tape water','From the sink',0);
--NonAlcoholicDrink data insert LittleEataly 5
INSERT INTO NonAlcoholicDrink_T VALUES(0000000005,5,24,'Zepsi','Zepsi Corp',4);
--Alcoholic Drink
--Alcoholic Drink data insert LittleEataly 1
INSERT INTO AlcoholicDrink_T VALUES(0000000001,1,4,'None','None',0);
--Alcoholic Drink data insert LittleEataly 2
INSERT INTO AlcoholicDrink_T VALUES(0000000002,2,8,'None','None',0);
--Alcoholic Drink data insert LittleEataly 3
INSERT INTO AlcoholicDrink_T VALUES(0000000003,3,14,'Lefera','Melvomo',150);
--Alcoholic Drink data insert LittleEataly 4
INSERT INTO AlcoholicDrink_T VALUES(0000000004,4,19,'Beer','Pud Light',6);
--Alcoholic Drink data insert LittleEataly 5
INSERT INTO AlcoholicDrink_T VALUES(0000000005,5,25,'None','None',0);

