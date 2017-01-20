# enron-email-word-count
Spark application to do word count on Enron email dataset

## Overview

This is a sample Apache Spark application which will process publicly available Enron email dataset - https://www.cs.cmu.edu/~./enron/. 
The app will produce total word counts for all email bodies (headers are excluded).
The output will only include words with a count >= 10, and list these words in order of descending count.

Apache Spark was chosen as it is a leading platform for general purpose cluster computing and data processing, with a Scala API which seems well suited for data transformations.

## Running

### Locally with subset of emails
You must have [sbt](http://www.scala-sbt.org/) and [Apache Spark](http://spark.apache.org/) installed in your environment and available in your shell.

1. Package the application. Run from the root directory of this project.

    ```sh
    $ sbt package
    ...
    Packaging /Users/kip/code/kipsigman/enron-email-word-count/target/scala-2.11/enron-email-word-count_2.11-1.0.jar ...
    ```
    
2. Use spark-submit to run your application

    ```sh
    $ spark-submit --class kipsigman.enron.EmailWordCount --master local[4] target/scala-2.11/enron-email-word-count_2.11-1.0.jar
    ...
    ```
    
3. View word counts in output file(s). These will be located in 'data/out/email-word-counts/'.

### Production with full dataset
TBD

## TODO
* Application is currently only processing a subset of emails:
  * Run with entire dataset
  * Run on cluster?
  * Memory settings
* Possibly store intermediate data, such as word counts per email file, per user, etc. Would help with quality checks.
* Email file crawler doesn't catch subfolders of users' root email folders, for example 'crandell-s/inbox/bankruptcy/'. Fix to catch all folders.
* Perhaps lowercase all words before reduce step? No requirement for this, but same word with different capitalizations will result in multiple records. 