.PHONY: all clean

all: nlg-pipeline.png

nlg-pipeline.png: nlg-pipeline.mmd
	mmdc -i $< -o $@

clean:
	rm -f nlg-pipeline.png
