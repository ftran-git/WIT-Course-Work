library(haven)
NSDUH_2014 <- read_sav("NSDUH_2014.SAV")
View(NSDUH_2014)

subset = NSDUH_2014[,c("CIG30AV","CATAG7")] #create subset of just our x(age (coded)) and y(avg #cigs smoked per day(coded))

filter = subset$CIG30AV<8
subsetFiltered = subset[filter,] #filters out survey respondents that didn't smoke cigarettes or choose not to answer
write.csv(subsetFiltered, "subFiltered.csv") #creates csv file of the filtered subset

myDataSubset = read.csv("subFiltered.csv") #Reads in our data
attach(myDataSubset) #Allows us to use Header Variable Names

#CATAG7 = age
#CIG30AV = average cig per day

#Dependent Y Code

nry_subset <- nrow(myDataSubset)
meany_subset <- mean(CIG30AV)
sdy_subset <- sd(CIG30AV)
dfy_subset <- (nry_subset-1)

#Part 1: Margin of Error for (Y) variable (Avg. No. of Cigs. Used per Day)

ty_critical_val_95 <- qt((1-(0.05/2)),dfy_subset)
ty_margin_error_95 <- ty_critical_val_95*(sdy_subset/(sqrt(nry_subset)))

#Part 2: Confidence Interval for (Y) variable

ty_lb_95 <- meany_subset - ty_margin_error_95
ty_ub_95 <- meany_subset + ty_margin_error_95

#Part 3: Interpretation

cat("The confidence interval shows that we are 95% 
    certain that the true population mean for the average number 
    of cigarettes smoked per day 
    would be between: (", (ty_lb_95), ", ", (ty_ub_95), ")")

#Independent X Code

nrx_subset <- nrow(myDataSubset)
meanx_subset <- mean(CATAG7)
sdx_subset <- sd(CATAG7)
dfx_subset <- (nrx_subset-1)

#Part 1: Margin of Error for (X) variable (Age)

tx_critical_val_95 <- qt((1-(0.05/2)),dfx_subset)
tx_margin_error_95 <- tx_critical_val_95*(sdx_subset/(sqrt(nrx_subset)))

#Part 2: Confidence Interval for (X) variable

tx_lb_95 <- meanx_subset - tx_margin_error_95
tx_ub_95 <- meanx_subset + tx_margin_error_95

#Part 3: Interpretation

cat("The confidence interval shows that we are 95% 
    certain that the true population mean for the average age 
    would be between: (", (tx_lb_95), ", ", (tx_ub_95), ")")










