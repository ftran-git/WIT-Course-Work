# IMPORT DATA (COMPLETE, DO NOT RUN AGAIN)
# NSDUH_2014 <- read_sav("NSDUH_2014.SAV")
# View(NSDUH_2014)
# 
# subset = NSDUH_2014[,c("CIG30AV","CATAG7")] # subset with age (coded) and avg # cigarettes smoked per day (coded)
# 
# filter = subset$CIG30AV<8
# subsetFiltered = subset[filter,] #filters out survey respondents that didn't smoke cigarettes or choose not to answer
# write.csv(subsetFiltered, "subFiltered.csv") #creates csv file of the filtered subset


setwd("C:/Users/ricej3/Desktop/Probability & Statistics/R-Code Workshops/Final Project")
library(haven)
myDataSubset = read.csv("subFiltered.csv") #Reads in our data
attach(myDataSubset) #Allows us to use Header Variable Names


# 1A: mean, median, mode, variance, and std. dev. of avg. cigarettes smoked per day
meanCigs <- mean(myDataSubset$CIG30AV)        #3.4428
medianCigs <- median(myDataSubset$CIG30AV)    #3
varCigs <- var(myDataSubset$CIG30AV)          #1.7910
stdDevCigs <- sd(myDataSubset$CIG30AV)        #1.3383

# subset of cigarettes smoked data to be used for mode
myDataSubset11 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '1')
myDataSubset12 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '2')
myDataSubset13 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '3')
myDataSubset14 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '4')
myDataSubset15 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '5')
myDataSubset16 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '6')
myDataSubset17 <- subset(myDataSubset$CATAG7, myDataSubset$CIG30AV == '7')

modeCigsVEC <- myDataSubset11
if(length(myDataSubset12) > length(myDataSubset11)) modeCigsVEC <- myDataSubset12
if(length(myDataSubset13) > length(myDataSubset12)) modeCigsVEC <- myDataSubset13
if(length(myDataSubset14) > length(myDataSubset13)) modeCigsVEC <- myDataSubset14
if(length(myDataSubset15) > length(myDataSubset14)) modeCigsVEC <- myDataSubset15
if(length(myDataSubset16) > length(myDataSubset15)) modeCigsVEC <- myDataSubset16
if(length(myDataSubset17) > length(myDataSubset16)) modeCigsVEC <- myDataSubset17
if(length(myDataSubset11) == length(modeCigsVEC)) print("1")
if(length(myDataSubset12) == length(modeCigsVEC)) print("2")
if(length(myDataSubset13) == length(modeCigsVEC)) print("3")  # this one actually executes; mode = 3
if(length(myDataSubset14) == length(modeCigsVEC)) print("4")
if(length(myDataSubset15) == length(modeCigsVEC)) print("5")
if(length(myDataSubset16) == length(modeCigsVEC)) print("6")
if(length(myDataSubset17) == length(modeCigsVEC)) print("7")


# 1B: 10-bin histogram (and 7-bin histogram)
hist(myDataSubset$CIG30AV, breaks = 9)
hist(myDataSubset$CIG30AV, xlim = c(0,8), breaks = c(0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5))


# 1C: boxplot
# subset for each age range
myDataSubset1 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '1')
myDataSubset2 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '2')
myDataSubset3 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '3')
myDataSubset4 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '4')
myDataSubset5 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '5')
myDataSubset6 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '6')
myDataSubset7 <- subset(myDataSubset$CIG30AV, myDataSubset$CATAG7 == '7')

boxplot(myDataSubset1, myDataSubset2, myDataSubset3, myDataSubset4,
        myDataSubset5, myDataSubset6, myDataSubset7)


# 1D: extra graph
library(ggplot2)

# see magnitude of each of 49 subgroups
ggplot(myDataSubset, aes(x=CATAG7, y=CIG30AV)) + geom_count()
# reference: https://stulp.gmw.rug.nl/ggplotworkshop/twodiscretevariables.html

# for each age, see which percentage voted for each option
ggplot(myDataSubset, aes(x=factor(CATAG7), fill=factor(CIG30AV))) + geom_bar(position="fill")
# reference: https://www.r-bloggers.com/2012/12/basics-of-histograms/
