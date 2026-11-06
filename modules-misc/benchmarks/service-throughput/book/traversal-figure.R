# The book section's traversal figure and summary: latency of a backward traversal as the store grows, mean of the passes
# (one store per pass) with its 95 % confidence interval (Student's t). Run from the module:
#   Rscript book/traversal-figure.R 2026-11-06
suppressPackageStartupMessages(library(ggplot2))

campaign <- commandArgs(trailingOnly = TRUE)[1]
stopifnot(!is.na(campaign))
d <- read.csv(file.path("results", paste0(campaign, "-traversal-book.csv")), stringsAsFactors = FALSE)
stopifnot(all(d$status == "ok"))

ci <- function(x) {
  n <- length(x); m <- mean(x)
  h <- if (n > 1) qt(0.975, n - 1) * sd(x) / sqrt(n) else NA
  c(n = n, mean = m, half = h)
}
keys <- unique(d[, c("size", "k", "readers")])
summary <- do.call(rbind, lapply(seq_len(nrow(keys)), function(i) {
  key <- keys[i, ]
  x <- d[d$size == key$size & d$k == key$k & d$readers == key$readers, ]
  p50 <- ci(x$lat_p50_ms); p99 <- ci(x$lat_p99_ms); q <- ci(x$queries_per_s)
  data.frame(store_records = key$size, k = key$k, readers = key$readers, connections = x$connections_per_query[1], passes = p50[["n"]],
             p50_ms = round(p50[["mean"]], 2), p50_low = round(p50[["mean"]] - p50[["half"]], 2), p50_high = round(p50[["mean"]] + p50[["half"]], 2),
             p99_ms = round(p99[["mean"]], 2), p99_half = round(p99[["half"]], 2),
             queries_per_s = round(q[["mean"]], 1), queries_low = round(q[["mean"]] - q[["half"]], 1), queries_high = round(q[["mean"]] + q[["half"]], 1),
             pg_cpu_pct = round(mean(x$pg_cpu_pct)), us_per_connection = round(mean(x$us_per_connection), 1))
}))
summary <- summary[order(summary$readers, summary$k, summary$store_records), ]
write.csv(summary, "book/traversal-summary.csv", row.names = FALSE)
print(summary, row.names = FALSE)

s <- summary
s$lineage <- paste0(format(s$connections, big.mark = ",", trim = TRUE), " connections (k = ", format(s$k, big.mark = ",", trim = TRUE), ")")
s$lineage <- factor(s$lineage, levels = unique(s$lineage[order(s$k)]))
s$panel <- factor(ifelse(s$readers == 1, "1 reader", "16 readers"), levels = c("1 reader", "16 readers"))
p <- ggplot(s, aes(x = store_records, y = p50_ms, colour = lineage, shape = lineage)) +
  geom_line(linewidth = 0.7) +
  geom_errorbar(aes(ymin = p50_low, ymax = p50_high), width = 0.06, linewidth = 0.5) +
  geom_point(size = 2.4) +
  facet_wrap(~panel) +
  scale_x_log10(breaks = c(1e4, 1e5, 1e6), labels = c("10k", "100k", "1M")) +
  scale_y_log10(breaks = c(3, 10, 30, 100, 300, 1000), labels = c("3", "10", "30", "100", "300", "1,000")) +
  scale_colour_manual(values = c("#2a78d6", "#eb6834", "#1baf7a"), name = NULL) +
  scale_shape_manual(values = c(16, 17, 15), name = NULL) +
  labs(x = "records in the store (log scale)", y = "median latency of a query (ms, log scale)") +
  theme_minimal(base_size = 10) +
  theme(legend.position = "top", legend.justification = "left",
        panel.grid.minor = element_blank(),
        panel.grid.major = element_line(colour = "#e4e3df", linewidth = 0.3),
        axis.text = element_text(colour = "#3d3d3a"), axis.title = element_text(colour = "#5f5e5a", size = 8.5),
        strip.text = element_text(colour = "#3d3d3a", face = "bold"),
        panel.spacing = unit(14, "pt"), plot.margin = margin(4, 16, 4, 4))
ggsave("book/traversal-latency.pdf", p, width = 6.5, height = 3.4)
ggsave("book/traversal-latency.png", p, width = 6.5, height = 3.4, dpi = 200, bg = "white")
cat("written: book/traversal-summary.csv, book/traversal-latency.pdf, book/traversal-latency.png\n")
