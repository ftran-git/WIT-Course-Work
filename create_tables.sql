-- =====================================================
-- File Name:    create_tables.sql
-- Instructor:   Nguyen Thai
-- Student:      Fabio Tran
-- Date:         03/28/2022
-- Description:  To create SQL tables:
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

CREATE TABLE LittleEataly_T (
    LittleEatalyID NUMBER(11,0) NOT NULL,
    Street VARCHAR2(30) NOT NULL,
    City VARCHAR2(20) NOT NULL,
    State CHAR(2) NOT NULL,
    ZipCode VARCHAR2(9)NOT NULL,
    PhoneNumber CHAR(10) NOT NULL,
    ParkingSpace VARCHAR2(3) CHECK(ParkingSpace IN ('Yes', 'No')) NOT NULL,
CONSTRAINT LittleEataly_PK PRIMARY KEY (LittleEatalyID)
);

CREATE TABLE Supplier_T (
    SupplierID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    SupplierName VARCHAR2(30) NOT NULL,
    Street VARCHAR2(30) NOT NULL,
    City VARCHAR2(20) NOT NULL,
    State CHAR(2) NOT NULL,
    ZipCode VARCHAR2(9)NOT NULL,
    PhoneNumber CHAR(10) NOT NULL,
CONSTRAINT Supplier_PK PRIMARY KEY (SupplierID),
CONSTRAINT Supplier_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);

CREATE TABLE Employee_T (
    EmployeeID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    EmployeeName VARCHAR2(30) NOT NULL,
    EmployeePosition VARCHAR2(20) NOT NULL,
    EmployeeType VARCHAR2(1) CHECK(EmployeeType IN ('S', 'H')) NOT NULL,
    PhoneNumber CHAR(10),
    HoursWorked NUMBER(4,2) NOT NULL,
    MedicalInsurance VARCHAR(30),
    LifeInsurance VARCHAR(30),
    RetirementPlan VARCHAR(30),
CONSTRAINT Employee_PK PRIMARY KEY (EmployeeID),
CONSTRAINT Employee_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);
    
CREATE TABLE Hourly_Employee_T (
    HEmployeeID NUMBER(11,0) NOT NULL,
    HourlyRate DECIMAL(6,2),
CONSTRAINT Hourly_Employee_PK PRIMARY KEY (HEmployeeID),
CONSTRAINT Hourly_Employee_FK FOREIGN KEY (HEmployeeID) REFERENCES Employee_T (EmployeeID)
);
    
CREATE TABLE Salaried_Employee_T (
    SEmployeeID NUMBER(11,0) NOT NULL,
    Salary NUMBER(7,0),
CONSTRAINT Salaried_Employee_PK PRIMARY KEY (SEmployeeID),
CONSTRAINT Salaried_Employee_FK FOREIGN KEY (SEmployeeID) REFERENCES Employee_T (EmployeeID)
);

CREATE TABLE Customer_T (
    CustomerNumber NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    CustomerName VARCHAR2(30),
    PhoneNumber CHAR(10),
    TableNumber NUMBER(2,0),
    Membership VARCHAR2(3) CHECK(Membership IN ('Yes', 'No')) NOT NULL,
CONSTRAINT Customer_PK PRIMARY KEY (CustomerNumber),
CONSTRAINT Customer_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);

CREATE TABLE RewardsProgram_T (
    RewardsProgramID NUMBER(11,0) NOT NULL,
    CustomerNumber INTEGER NOT NULL,
    Coupon VARCHAR2(3) CHECK(Coupon IN ('Yes', 'No')),
    Discount VARCHAR2(3) CHECK(Discount IN ('Yes', 'No')),
CONSTRAINT RewardsProgram_PK PRIMARY KEY (RewardsProgramID),
CONSTRAINT RewardsProgram_FK FOREIGN KEY (CustomerNumber) REFERENCES Customer_T (CustomerNumber)
);

CREATE TABLE Order_T (
    OrderID NUMBER(11,0) NOT NULL,
    CustomerNumber INTEGER NOT NULL,
    Food VARCHAR2(3) CHECK(Food IN ('Yes', 'No')),
    Drink VARCHAR2(3) CHECK(Drink IN ('Yes', 'No')),
CONSTRAINT Order_PK PRIMARY KEY (OrderID),
CONSTRAINT Order_FK FOREIGN KEY (CustomerNumber) REFERENCES Customer_T (CustomerNumber)
);

CREATE TABLE Renovation_T (
    RenovationID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    RenovationType VARCHAR(20) NOT NULL,
    RenovationCost NUMBER(7,0) NOT NULL,
CONSTRAINT Renovation_PK PRIMARY KEY (RenovationID),
CONSTRAINT Renovation_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);   
    
CREATE TABLE Equipment_T (
    EquipmentID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    EquipmentType VARCHAR(20) NOT NULL,
    Quantity NUMBER(5,0) NOT NULL,
    MaintenanceCost NUMBER(7,0) NOT NULL,
    Owned VARCHAR2(3) CHECK(Owned IN ('Yes', 'No')) NOT NULL,
CONSTRAINT Equipment_PK PRIMARY KEY (EquipmentID),
CONSTRAINT Equipment_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);     

CREATE TABLE Expense_T (
    ExpenseID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    Food NUMBER(7,0) NOT NULL,
    Liquor NUMBER(7,0) NOT NULL,
    Labor NUMBER(7,0) NOT NULL,
    Electricty NUMBER(7,0) NOT NULL,
    Water NUMBER(7,0) NOT NULL,
    Internet NUMBER(7,0) NOT NULL,
    Phone NUMBER(7,0) NOT NULL,
    Insurance NUMBER(7,0) NOT NULL,
    Mortgage NUMBER(7,0) NOT NULL,
    Equipment NUMBER(7,0) NOT NULL,
    Marketing NUMBER(7,0) NOT NULL,
CONSTRAINT Expense_PK PRIMARY KEY (ExpenseID),
CONSTRAINT Expense_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);  
    
CREATE TABLE Revenue_T (
    RevenueID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OperationRevenue NUMBER(10,0) NOT NULL,
CONSTRAINT Revenue_PK PRIMARY KEY (RevenueID),
CONSTRAINT Revenue_FK FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID)
);   

CREATE TABLE Pasta_T (
    PastaID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    Sauce VARCHAR(15) CHECK(Sauce IN ('Bolognese', 'Ragu', 'Marinara', 'Pesto', 'Alfredo', 'Carbonara')),
    Meat VARCHAR(15) CHECK(Meat IN ('Beef', 'Chicken', 'Shrimp')),
    Vegetable VARCHAR(15) CHECK(Vegetable IN ('Broccoli', 'Mushroom', 'Onion')),
    SpecialIngredients VARCHAR(15),
    NoodleType VARCHAR(15) CHECK(NoodleType IN ('Fettucine', 'Spaghetti', 'Linguine', 'Penne', 'Lasagne', 'Ravioli', 'Rigatoni')) NOT NULL,
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT Pasta_PK PRIMARY KEY (PastaID),
CONSTRAINT Pasta_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT Pasta_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
);  

CREATE TABLE Salad_T (
    SaladID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    Meat VARCHAR(3) CHECK(Meat IN ('Yes', 'No')) NOT NULL,
    DriedFruit VARCHAR2(3) CHECK(DriedFruit IN ('Yes', 'No')) NOT NULL,
    WholeGrain VARCHAR2(3) CHECK(WholeGrain IN ('Yes', 'No')) NOT NULL,
    Dressing VARCHAR2(3) CHECK(Dressing IN ('Ranch', 'Italian', 'Caesar')),
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT Salad_PK PRIMARY KEY (SaladID),
CONSTRAINT Salad_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT Salad_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
); 

CREATE TABLE Sandwich_T (
    SandwichID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    Meat VARCHAR2(3) CHECK(Meat IN ('Turkey', 'Ham', 'Beef')),
    Cheese VARCHAR2(3) CHECK(Cheese IN ('American', 'Swiss', 'Cheddar')),
    Sauce VARCHAR2(3) CHECK(Sauce IN ('Mayonnaise', 'Mustard', 'Ketchup')),
    Lettuce VARCHAR2(3) CHECK(Lettuce IN ('Yes', 'No')) NOT NULL,
    Pickle VARCHAR2(3) CHECK(Pickle IN ('Yes', 'No')) NOT NULL,
    Toasted VARCHAR2(3) CHECK(Toasted IN ('Yes', 'No')) NOT NULL,
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT Sandwich_PK PRIMARY KEY (SandwichID),
CONSTRAINT Sandwich_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT Sandwich_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
); 

CREATE TABLE Appetizer_T (
    AppetizerID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    AppetizerName VARCHAR2(30) NOT NULL,
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT Appetizer_PK PRIMARY KEY (AppetizerID),
CONSTRAINT Appetizer_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT Appetizer_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
);     

CREATE TABLE Dessert_T (
    DessertID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    DessertSize VARCHAR2(3) CHECK (DessertSize IN ('Small', 'Medium', 'Large')) NOT NULL,
    Flavor VARCHAR(3) CHECK (Flavor IN ('Vanilla', 'Chocolate', 'Strawberry')) NOT NULL,
    WhippedCream VARCHAR(3) CHECK (WhippedCream IN ('Yes', 'No')) NOT NULL,
    Syrup VARCHAR(3) CHECK (Syrup IN ('Yes', 'No')) NOT NULL,
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT Dessert_PK PRIMARY KEY (DessertID),
CONSTRAINT Dessert_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT Dessert_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
);        

CREATE TABLE NonAlcoholicDrink_T (
    NonAlcoholicDrinkID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    NonAlcoholicDrinkType VARCHAR2(20),
    Brand VARCHAR2(30),
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT NonAlcoholicDrink_PK PRIMARY KEY (NonAlcoholicDrinkID),
CONSTRAINT NonAlcoholicDrink_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT NonAlcoholicDrink_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
); 

CREATE TABLE AlcoholicDrink_T (
    AlcoholicDrinkID NUMBER(11,0) NOT NULL,
    LittleEatalyID INTEGER NOT NULL,
    OrderID INTEGER NOT NULL,
    WineType VARCHAR2(20),
    Brand VARCHAR2(30),
    Price NUMBER(3,0) NOT NULL,
CONSTRAINT AlcoholicDrink_PK PRIMARY KEY (AlcoholicDrinkID),
CONSTRAINT AlcoholicDrink_FK1 FOREIGN KEY (LittleEatalyID) REFERENCES LittleEataly_T (LittleEatalyID), 
CONSTRAINT AlcoholicDrink_FK2 FOREIGN KEY (OrderID) REFERENCES Order_T (OrderID)
); 





    
    