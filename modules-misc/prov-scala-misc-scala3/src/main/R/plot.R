
args<-commandArgs(TRUE)

print(args)

pdf(args[2])

stats <- read.table(args[1], sep=",")

#print(stats)

applications=c(1,2,3,4)
names(applications)=c("smart", "food", "ebook", "picaso")
	 

mydots=c(1,2,3,4,5,6,7,8)

remove_outliers <- function(x, na.rm = TRUE, ...) {
  qnt <- quantile(x, probs=c(.25, .75), na.rm = na.rm, ...)
  H <- 1.5 * IQR(x, na.rm = na.rm)
  y <- x
  y[x < (qnt[1] - H)] <- NA
  y[x > (qnt[2] + H)] <- NA
  y
}

######################################################################
# box plot

boxplot(stats$V1,stats$V2,stats$V3,stats$V4,stats$V5, stats$V6, axes=FALSE, data=stats, range=1, ylab="time (in us)", cex.lab=0.7,log="y")


# Make y axis
axis(2, cex.axis=0.7)

# Make x axis
axis(1, at=0:6, labels=FALSE, cex.axis=0.7, las=3)

# Labels on x axis 
mtext(c("read","normalize","serialize","sign","verify","serial0"), side=1,at=1:6,adj=0.5,srt = 0, pos = 1, xpd = TRUE, cex=0.6,las=0,line=1)


######################################################################
# box plot: remove outliers

boxplot(remove_outliers(stats$V1),remove_outliers(stats$V2),remove_outliers(stats$V3), axes=FALSE, data=stats, range=0, ylab="time (in us)", cex.lab=0.7)

# Make y axis
axis(2, c(0,1000,1500,2000,2500,3000,3500,4000,4500), cex.axis=0.7)

# Make x axis
axis(1, at=0:5, labels=FALSE, cex.axis=0.7, las=3)

# Labels on x axis 
mtext(c("read","normalize","serialize","sign","verify"), side=1,at=1:5,adj=0.5,srt = 0, pos = 1, xpd = TRUE, cex=0.6,las=0,line=1)


######################################################################
# scatter plot

plot(stats$V1,stats$V2,axes=TRUE, xlab="read time", ylab="normalize time", cex=0.7,
#	      xlim=c(0,7000),ylim=c(0,7000)
	      ) #, log="xy"


# Make y axis
axis(2, c(0,1000,1500,2000,2500,3000,3500,4000,4500), cex.axis=0.7)

# Make x axis
axis(1, at=0:3, labels=FALSE, cex.axis=0.7, las=3)

# Labels on x axis 
mtext(c("read","normalize","serialize"), side=1,at=1:3,adj=0.5,srt = 0, pos = 1, xpd = TRUE, cex=0.6,las=0,line=1)




quit()


abline(lm(smart_w2$V4~smart_w2$V6),   col=colors[1])  # regression line (y~x) 
abline(lm(smart_ww2$V4~smart_ww2$V6), col=colors[2])  # regression line (y~x) 

abline(lm(food_w2$V4~food_w2$V6),   col=colors[3])  # regression line (y~x) 
abline(lm(food_ww2$V4~food_ww2$V6), col=colors[4])  # regression line (y~x) 

abline(lm(ebook_w2$V4~ebook_w2$V6),   col=colors[5])  # regression line (y~x) 
abline(lm(ebook_ww2$V4~ebook_ww2$V6), col=colors[6])  # regression line (y~x) 

abline(a=0,b=1, col="gray")


# add a legend
apps=c(1,2,3,4,5,6,7,8)
names(apps)=c("smart (v1)","smart (v2)","food (v1)","food (v2)","ebook (v1)","ebook (v2)","picaso (v1)","picaso (v2)")


legend(100,20000, names(apps) , cex=0.7, col=colors[apps],lty=c(1,1),pch=mydots,title="Apps")

######################################################################
# zoom in

plot(xx$V6,xx$V4,axes=TRUE, xlab="Expanded Size", ylab="Binding Size", col=colors[2*applications[xx$V9]+xx$V8-2],cex=0.7,
             pch=mydots[2*applications[xx$V9]+xx$V8-2],
	      xlim=c(0,7000),ylim=c(0,7000)
	      ) #, log="xy"

abline(lm(smart_w2$V4~smart_w2$V6),   col=colors[1])  # regression line (y~x) 
abline(lm(smart_ww2$V4~smart_ww2$V6), col=colors[2])  # regression line (y~x) 

abline(lm(food_w2$V4~food_w2$V6),   col=colors[3])  # regression line (y~x) 
abline(lm(food_ww2$V4~food_ww2$V6), col=colors[4])  # regression line (y~x) 

abline(lm(ebook_w2$V4~ebook_w2$V6),   col=colors[5])  # regression line (y~x) 
abline(lm(ebook_ww2$V4~ebook_ww2$V6), col=colors[6])  # regression line (y~x) 

abline(a=0,b=1, col="gray")


# add a legend
apps=c(1,2,3,4,5,6,7,8)
names(apps)=c("smart (v1)","smart (v2)","food (v1)","food (v2)","ebook (v1)","ebook (v2)","picaso (v1)","picaso (v2)")


legend(100,7000, names(apps) , cex=0.7, col=colors[apps],lty=c(1,1),pch=mydots,title="Apps")


######################################################################
# show compression levels

xx$V10 <- xx$V4 / xx$V6
yy <- xx[order(xx$V10),]
yy$V11 <- 1:nrow(yy)



plot(yy$V11,yy$V10,axes=TRUE, xlab="binding", ylab="Compression", col=colors[2*applications[yy$V9]+yy$V8-2],cex=0.2,
#             pch=mydots[2*applications[yy$V9]+yy$V8-2],
	     pch="I",
#	      xlim=c(0,7000),ylim=c(0,7000)
	      ) #, log="xy"

abline(a=1,b=0, col="gray")	      
legend(5,1.32, names(apps) , cex=0.7, col=colors[apps],lty=c(1,1),pch=mydots,title="Apps")


######################################################################
# zoom in

plot(yy$V11,yy$V10,axes=TRUE, xlab="binding", ylab="Compression", col=colors[2*applications[yy$V9]+yy$V8-2],cex=0.2,
#             pch=mydots[2*applications[yy$V9]+yy$V8-2],
	     pch="I",
	      xlim=c(2000,2700),
	      ) #, log="xy"

abline(a=1,b=0, col="gray")	      
legend(2005,0.8, names(apps) , cex=0.7, col=colors[apps],lty=c(1,1),pch=mydots,title="Apps")
