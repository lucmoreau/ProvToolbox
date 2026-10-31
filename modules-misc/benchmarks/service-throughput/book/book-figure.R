# The book section's figure and summary: records/s of each way to store the same records (fs family), mean of the
# repeated runs (LABEL=book) with its 95 % confidence interval (Student's t on the runs' means). Run from the module:
#   Rscript book/book-figure.R
suppressPackageStartupMessages(library(ggplot2))

res <- "results"
read_all <- function(pattern) {
  files <- list.files(res, pattern = pattern, full.names = TRUE)
  do.call(rbind, lapply(files, function(f) read.csv(f, stringsAsFactors = FALSE)))
}
http   <- read_all("^[0-9]{4}-[0-9]{2}-[0-9]{2}-book\\.csv$")
direct <- read_all("^[0-9]{4}-[0-9]{2}-[0-9]{2}-direct-book\\.csv$")
jsonl  <- read_all("^[0-9]{4}-[0-9]{2}-[0-9]{2}-jsonl-book\\.csv$")
stopifnot(all(http$status == "ok"), all(direct$status == "ok"), all(jsonl$status == "ok"))

jsonl$option <- ifelse(jsonl$b == 1, "JSONL, a force per record", "JSONL, a force per 100 records")
direct$option <- with(direct, ifelse(layer == "insert",
                          ifelse(jdbc_batch == 1, "Direct, insert only, JDBC batch of 100", "Direct, insert only"),
                       ifelse(key == "on" & readback == "on" & hash == "update" & relations == "inline", "Direct, full write",
                       ifelse(b == 100, "Direct, combined, 100 per transaction", "Direct, combined"))))
http$option <- ifelse(http$variant == "baseline", "HTTP, full write", "HTTP, combined")

pick <- function(d, cpu_col) data.frame(option = d$option, writers = d$n, b = d$b, records_per_s = d$records_per_s,
                                        p99_ms = d$lat_p99_ms,
                                        pg_ms_per_record = if (is.null(d$pg_cpu_ms_per_record)) NA else d$pg_cpu_ms_per_record,
                                        client_ms_per_record = d[[cpu_col]], stringsAsFactors = FALSE)
all <- rbind(pick(jsonl, "client_cpu_ms_per_record"), pick(direct, "client_cpu_ms_per_record"),
             pick(http, "svc_cpu_ms_per_record"))

ci <- function(x) {
  n <- length(x); m <- mean(x); s <- sd(x); h <- qt(0.975, n - 1) * s / sqrt(n)
  c(n = n, mean = m, sd = s, lo = m - h, hi = m + h, half = h)
}
keys <- unique(all[, c("option", "writers", "b")])
summary <- do.call(rbind, lapply(seq_len(nrow(keys)), function(i) {
  k <- keys[i, ]
  d <- all[all$option == k$option & all$writers == k$writers & all$b == k$b, ]
  r <- ci(d$records_per_s); p <- ci(d$p99_ms)
  data.frame(option = k$option, writers = k$writers, records_per_transaction = k$b, runs = r[["n"]],
             records_per_s = round(r[["mean"]], 1), ci95_low = round(r[["lo"]], 1), ci95_high = round(r[["hi"]], 1),
             ci95_half_pct = round(100 * r[["half"]] / r[["mean"]], 1),
             p99_ms = round(p[["mean"]], 2), p99_ci95_half = round(p[["half"]], 2),
             pg_ms_per_record = round(mean(d$pg_ms_per_record), 3),
             client_or_service_ms_per_record = round(mean(d$client_ms_per_record), 3))
}))
order <- c("JSONL, a force per record", "JSONL, a force per 100 records", "Direct, insert only",
           "Direct, insert only, JDBC batch of 100", "Direct, full write", "Direct, combined",
           "Direct, combined, 100 per transaction", "HTTP, full write", "HTTP, combined")
summary <- summary[order(match(summary$option, order), summary$writers), ]
write.csv(summary, "book/book-summary.csv", row.names = FALSE)
print(summary, row.names = FALSE)

s <- summary
s$option <- factor(s$option, levels = rev(order))
s$series <- factor(ifelse(s$writers == 1, "1 writer", "16 writers"), levels = c("1 writer", "16 writers"))
p <- ggplot(s, aes(x = records_per_s, y = option, colour = series, shape = series)) +
  geom_errorbarh(aes(xmin = ci95_low, xmax = ci95_high), height = 0.35, linewidth = 0.6,
                 position = position_dodge(width = 0.55)) +
  geom_point(size = 2.6, position = position_dodge(width = 0.55)) +
  scale_x_log10(breaks = c(100, 300, 1000, 3000, 10000, 30000, 100000),
                labels = c("100", "300", "1,000", "3,000", "10,000", "30,000", "100,000"),
                limits = c(150, 150000)) +
  scale_colour_manual(values = c("1 writer" = "#2a78d6", "16 writers" = "#eb6834"), name = NULL) +
  scale_shape_manual(values = c("1 writer" = 16, "16 writers" = 17), name = NULL) +
  labs(x = "template records per second (log scale), mean of 5 runs with 95 % confidence interval", y = NULL) +
  theme_minimal(base_size = 10) +
  theme(legend.position = "top", legend.justification = "left",
        panel.grid.major.y = element_blank(), panel.grid.minor = element_blank(),
        panel.grid.major.x = element_line(colour = "#e4e3df", linewidth = 0.3),
        axis.text = element_text(colour = "#3d3d3a"), axis.title.x = element_text(colour = "#5f5e5a", size = 8.5),
        plot.margin = margin(4, 14, 4, 4))
ggsave("book/benchmark-throughput.pdf", p, width = 6.5, height = 3.8)
ggsave("book/benchmark-throughput.png", p, width = 6.5, height = 3.8, dpi = 200, bg = "white")
cat("written: book/book-summary.csv, book/benchmark-throughput.pdf, book/benchmark-throughput.png\n")
