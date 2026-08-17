### Lucky Number Generator

An original application written in Java for deployment to the AWS Cloud

by Jonathan Saddler

CHANGELOG 

- 08/03 : upload testOneNumber()
- 08/04 : Fix tests. Ensure initialization is done for both the same way
- 08/05 : Prepare for addl feature: 3-digit lucky numbers
- 08/06 : Nothing to report.
- 08/07-08/08 (MISSED UPDATE) Nothing reported 
- 08/09 : "Remainder File String" method in function Single3x creates a JSON file: help keep track of previously generated numbers.
- 08/10 : "Affected digits" checks to see which s3 directories need updated
- 08/10 : Add clean_s3_directories for daily storage reset, 
- & test bulk upload to cloud storage
- 08/11 : (MISSED UPDATE) Nothing reported
- 08/12 : Update the handler for gen 4-6 to use single threads to upload files / then inputs 7-9 parallel threads. Test Perf Later
- (inputs 1-3 don't update to cloud at all for now) 
- 08/13 : Add capabilities to store numbers generated. Focus on making input numbers 4,5,6 trigger upload to S3 sequentially. Test other #s later. 
- 08/14 : Inputs 1-3 update cloud now, and should only generate numbers with a 1/2/3 that were not previously generated.
- 08/15 : Refocus on generating 2-digit numbers first then move on to 3-digit later
- & add Test files to test S3 and API Gateway Functionality (for 1x, Jayway and RestAssured) 
- 08/16 : Send error to frontend when no new numbers can be generated (inputs 1-3)

NOTES: 

testOneNumber() is an automated test that clicks one number and clicks the "Generate" button to generate a single set of lucky numbers. 
- later the goal will be to automate a test that clicks several numbers and generates several sets of numbers.

The look of this application July 25, before the challenge:
![Lucky Number Generator from July 25](https://github.com/jazad136/aws-luckynumgen-java/raw/main/images/1_2026_July25_LuckyNumGenerator_One.jpg)
