library(haven)
NSDUH_2014 <- read_sav("NSDUH_2014.SAV")
View(NSDUH_2014)

subset = NSDUH_2014[,c("CIG30AV","CATAG7")] #create subset of just our x(age (coded)) and y(avg #cigs smoked per day(coded))

filter = subset$CIG30AV<8
subsetFiltered = subset[filter,] #filters out survey respondents that didn't smoke cigarettes or choose not to answer
write.csv(subsetFiltered, "subFiltered.csv") #creates csv file of the filtered subset

myDataSubset = read.csv("subFiltered.csv") #Reads in our data
attach(myDataSubset) #Allows us to use Header Variable Names

#Section 2 Part A: Scatterplot with labels
plot(CATAG7,CIG30AV, main="Scatterplot", xlab="Age (coded)", ylab="Avg Number of Cigarettes Smoked Per Day (coded)")

#Section 2 Part B: Drawing trend line for scatterplot
LinearModel_Age_avgCigs = lm(CATAG7~CIG30AV)S
summary(LinearModel_Age_avgCigs)
abline(LinearModel_Age_avgCigs)

#Section 2 Part B: Equation for linear regression
#Slope: 0.338593
#Y-intercept: 4.560827
#Equation: y = 0.338593x + 4.560827 

#Section 2 Part B: Approximate y by choosing an x
#y = 0.338593*4 + 4.560827 
#y = 5.915199
#y is approximately 6 therefore a smoker at the age of between 18-20 is expected to smoke 26 to 35 cigarettes a day (1 and 1/2 packs)

#Section 2 Part B: Approximate x by choosing a y
#7 = 0.338593x + 4.560827 
#x = 7.20384946
#x is approximately 7 therefore a smoker who smokes more than 35 cigarettes (2 packs or more) per day is expected to be 35 or older

#Section 2 Part C: Calculate the correlation coefficient, R. Explain what this means for your model.
R_value = cor(CATAG7, CIG30AV)
#Interpretation: The correlation coefficient, R, is 0.344993609821406 there the relation between the two variables age and average # of cigarettes smoked per day is a weak and positive correlation.

#Section 2 Part D:  Calculate the coefficient of determination, R Squared. Explain what this means for your model
R_Squared_value = R_value*R_value
#Interpretation: The coefficient of determination, R squared, is 0.119020590817605 which means that approximately 12% of the total variation in average # of cigarettes smoked per day can be explained by the relationship between age and average # of cigarettes smoked per day, as quantified by our linear regression equation.

#Section 2 Part E:Create a Normal QQ-Plot for dependent variable. Explain what this plot means for your data.
qqnorm(CIG30AV)
qqline(CIG30AV)
#Interpretation: We are plotting a discrete random variable, but the normal distribution is for
#continuous random variables. Therefore, we have "jumps" or "steps" in our plot
#suggesting that this variable is discrete. This is not Normal and therefore violates
#the normal assumption.

