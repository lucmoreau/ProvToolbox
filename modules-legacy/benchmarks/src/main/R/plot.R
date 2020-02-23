

benchmark_file="bench1-quick.csv"
benchmark_file="bench100.csv"
benchmark_file="bench-2020-02-16-08-00.csv"
benchmark_file="bench-2020-02-16-09-31.csv"
benchmark_file="bench-2020-02-16-18-02.csv"
benchmark_file="bench-2020-02-20-22-26.csv"
benchmark_file="bench-2020-02-21-08-15.csv"
benchmark_file="bench-2020-02-22-15-29.csv"
benchmark_file="bench-2020-02-22-22-53.csv"
benchmark_file="bench-2020-02-23-07-31.csv"
benchmark_file="bench-2020-02-23-09-22.csv"


library(reshape)

setwd("/Users/lavm/IdeaProjects/ProvToolbox/modules-legacy/benchmarks/results/")

stats <- read.table(benchmark_file, sep=",", header=TRUE)

unit<- 1000 * 1000 # seconds

stats$Score <- stats$Score/unit

benchmarks <- c(1,2,3,4,5,6,7,8,9, 10)
names(benchmarks)=c("r-provjsonld","w-provjsonld","r-json", "jdeepcopy","r-provjson", "w-provjson", "r-provn", "w-provn", "sdeepcopy", "sca-read")

colors <- c("royalblue3", "lightblue", "orange4", "orange1", "chartreuse4","chartreuse1", "tomato4", "tomato1", "grey", "pink")


new_stats1  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testDeserialiseProvJsonLD" & stats$Mode=="sample"), ]
new_stats2  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testSerialiseProvJsonLD" & stats$Mode=="sample"), ]
new_stats3  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testDeserialiseJson" & stats$Mode=="sample"), ]
new_stats4  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testDeepCopyJava" & stats$Mode=="sample"), ]
new_stats5  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testDeserialisePJson" & stats$Mode=="sample"), ]
new_stats6  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testSerialisePJson" & stats$Mode=="sample"), ]
new_stats7  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testDeserialiseProvN" & stats$Mode=="sample"), ]
new_stats8  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testSerialiseProvN" & stats$Mode=="sample"), ]
new_stats9  <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testDeepCopyScala" & stats$Mode=="sample"), ]
new_stats10 <- stats[ which(stats$Benchmark=="org.openprovenance.prov.benchmarks.SerialisationBenchmarks.testReadScala" & stats$Mode=="sample"), ]


new_stats <-rbind(new_stats1,new_stats2,new_stats3,new_stats4, new_stats5, new_stats6, new_stats7, new_stats8, new_stats9, new_stats10)
new_stats$Score <- new_stats$Score * 1000 * 1000
print(new_stats)

outfile <- sub("csv", "pdf", benchmark_file)
pdf(outfile) 


######################################################################
# bar plot

plotTop <- max(new_stats$Score+ 2 * max(new_stats$Score.Error..99.9..)) * 1.1

title <- paste ("Serialisation/Deserialisation (", benchmark_file, ")", sep="")

#par(mar=c(20,4,4,2))

myplot <-barplot (new_stats$Score, xlab=title, ylab="Time in us", ylim = c(0, plotTop), col=colors, cex.names=0.6)

text(x = myplot, y = par("usr")[3] - 1, srt = 45,
     adj = 1, labels = names(benchmarks), xpd = TRUE, cex=0.7)


#myplot2 <-barplot (new_stats$Score, names.arg=names(benchmarks), xlab="Serialisation/Deserialisation", ylab="Time in us", xlim = c(0, plotTop), col=colors, horiz=TRUE, cex.names=0.5, legend=unique(new_stats$Unit))

#myplot <-barplot (stats$V5, names.arg=stats$V1, xlab="Processing Phase", ylab="Time in us", ylim = c(0, plotTop), col=c("darkblue","red","darkgreen","orange"), cex.names=0.5, legend=unique(stats$V8), beside=TRUE)

errorbar_color="red"
segments(myplot, new_stats$Score - new_stats$Score.Error..99.9.., myplot, new_stats$Score + new_stats$Score.Error..99.9.., lwd = 1.5, col=errorbar_color)
arrows(myplot, new_stats$Score - new_stats$Score.Error..99.9.., myplot, new_stats$Score + new_stats$Score.Error..99.9.., lwd = 1.5, angle = 90, code = 3, length = 0.05, col=errorbar_color)

text(myplot, y = new_stats$Score +7, label = round(new_stats$Score, digits=2), pos = 4, cex = 0.8, col = "blue")

dev.off() 