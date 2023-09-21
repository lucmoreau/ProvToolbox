PROVMAN=../../modules-executable/target/appassembler/bin/provmanagement


t1:
	$(PROVMAN) summary --triangle true 	--features target/features%level-triangle.json 	  --infile src/test/resources/types/4-deriv.provn --outfiles target/summary%level-triangle.provn,target/summary%level-triangle.svg 	   --from 1 --to 3 -T target/summary%level-triangle-types.json
	$(PROVMAN) summary --triangle false --features target/features%level-no-triangle.json --infile src/test/resources/types/4-deriv.provn --outfiles target/summary%level-no-triangle.provn,target/summary%level-no-triangle.svg --from 1 --to 3 -T target/summary%level-no-triangle-types.json


t2:
	$(PROVMAN) summary --triangle true 	--features target/features%level-triangle.json 	  --infile src/test/resources/types/100009/100009.provn --outfiles target/summary%level-triangle.provn,target/summary%level-triangle.svg 	   --from 3 --to 3 -T target/summary%level-triangle-types.json --no-primitives  
	$(PROVMAN) summary --triangle false --features target/features%level-no-triangle.json --infile src/test/resources/types/100009/100009.provn --outfiles target/summary%level-no-triangle.provn,target/summary%level-no-triangle.svg --from 3 --to 3 -T target/summary%level-no-triangle-types.json --no-primitives  


t1r:
	$(PROVMAN) summary --triangle true 	--features target/features%level-triangle.json 	  --infile src/test/resources/types/4-deriv.provn --outfiles target/summary%level-triangle.provn,target/summary%level-triangle.svg 	   --from -3 --to 3 -T target/summary%level-triangle-types.json
	$(PROVMAN) summary --triangle false --features target/features%level-no-triangle.json --infile src/test/resources/types/4-deriv.provn --outfiles target/summary%level-no-triangle.provn,target/summary%level-no-triangle.svg --from -3 --to 3 -T target/summary%level-no-triangle-types.json
