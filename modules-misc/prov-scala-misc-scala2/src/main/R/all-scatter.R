
pdf("outputs/scatter.pdf")

smart_w <- read.table("smartshare/archive_28483/outputs/toscatter.csv", sep=",")
smart_w2 <- smart_w[order(smart_w$V4),]
smart_w2$V7 <- 1:nrow(smart_w2)
smart_w2$V8 <- 1
smart_w2$V9 <- "smart"



smart_ww <- read.table("smartshare/archive_28483/outputs/toscatter2.csv", sep=",")
smart_ww2 <- smart_ww[order(smart_ww$V4),]
smart_ww2$V7 <- 1:nrow(smart_ww2)
smart_ww2$V8 <- 2
smart_ww2$V9 <- "smart"


food_w <- read.table("foodprovenance/archive_20160303/outputs/toscatter.csv", sep=",")
food_w2 <- food_w[order(food_w$V4),]
food_w2$V7 <- 1:nrow(food_w2)
food_w2$V8 <- 1
food_w2$V9 <- "food"



food_ww <- read.table("foodprovenance/archive_20160303/outputs/toscatter2.csv", sep=",")
food_ww2 <- food_ww[order(food_ww$V4),]
food_ww2$V7 <- 1:nrow(food_ww2)
food_ww2$V8 <- 2
food_ww2$V9 <- "food"


ebook_w <- read.table("ebook/20160308_big/outputs/toscatter.csv", sep=",")
ebook_w2 <- ebook_w[order(ebook_w$V4),]
ebook_w2$V7 <- 1:nrow(ebook_w2)
ebook_w2$V8 <- 1
ebook_w2$V9 <- "ebook"



ebook_ww <- read.table("ebook/20160308_big/outputs/toscatter2.csv", sep=",")
ebook_ww2 <- ebook_ww[order(ebook_ww$V4),]
ebook_ww2$V7 <- 1:nrow(ebook_ww2)
ebook_ww2$V8 <- 2
ebook_ww2$V9 <- "ebook"


colors=c("darksalmon","red", "cadetblue1","blue","darkseagreen","green4","orangered","black","brown")
mydots=c(1,2,3,4,5,6,7,8)

applications=c(1,2,3,4)
names(applications)=c("smart", "food", "ebook", "picaso")
	 

#xx<-rbind(smart_w2,smart_ww2)
xx<-rbind(smart_w2,smart_ww2,food_w2,food_ww2,ebook_w2,ebook_ww2)

######################################################################
# scatter plot

plot(xx$V6,xx$V4,axes=TRUE, xlab="Expanded Size", ylab="Binding Size", col=colors[2*applications[xx$V9]+xx$V8-2],cex=0.7,
             pch=mydots[2*applications[xx$V9]+xx$V8-2],
#	      xlim=c(0,7000),ylim=c(0,7000)
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
