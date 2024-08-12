# How to run the log parser

## Step 1: Download the java project locally
* Install `git`, if not already present.
* Clone the repository using the command `git clone git@github.com:sharvari-kapadia/illumio-tech-assessment.git`
* Go to the root folder of the git repository.

## Step 2: Add input files under `resources/input` folder
* Please rename your Flow log record to `flow_log_record.log` and place it under `illumio-tech-assessment/src/main/resources/input`
* Please rename your Lookup table to `lookup_table.csv` and place it under `illumio-tech-assessment/src/main/resources/input`

## Step 3: Ensure Java is installed
* This Java project is written using Java 21. It should ideally work for any version of java above Java 8. Make sure java is installed by running `java --version`

## Step 4: Compile and run the program
* Open a command terminal, go to the repository root `illumio-tech-assessment` and run the following commands.
```
javac -d ./out $(find ./src/main -type f -name "*.java" 2> /dev/null)
java -cp ./out src/main/java/org/illumio/Main.java

Note: If using a system other than MAC or Linux, kindly substitute appropriate command for `find`.
```

* Alternatively, if you are using an IDE, you can right-click on `Main.java` class and run the program directly from the IDE. It will generate the same result.

# Assumptions

## Protocol mappings
* protocol id to protocol name (eg: 6 --> tcp, 17 --> udp) comes from file called protocol_numbers.csv. This file is downloaded from [protocol mappings](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml) as written under description of `protocol` field at AWS [flow-log-records](https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html) page.
* The protocol mappings file .csv file i.e. it has comma separated values.
* Blank spaces will be ignored.
* The first line of the protocol file is a header for column names.

## Lookup table
* The lookup table is a .csv file i.e. it has comma separated values.
* Blank spaces will be ignored.
* The first line of the protocol file is a header for column names.

## Flow log record file 
* Based on the [AWS flow log records documentation](https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html), for every line of the flow record, the 7th column is `dstport` and 8th column is the `protocol`.
* The flow log will not be custom flow log and it will always have `dstport` and `protocol` at the specified positions.

# Testing
* Ran some unit tests to ensure the individual methods are behaving as expected.
* Ran the program to generate the two output files and manually verified if the results are expected per the data set provided in the `src/main/resources/input` folder.