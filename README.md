# Experiments in Authorship-Link Ranking and Complete Author Clustering.
## Submission to PAN 2016.

## Task description
Given a document collection, the task is to group documents written by the same
author, so that each cluster corresponds to a different author. This task can also
be viewed as one of establishing authorship links between documents. We use
a combination of classification and agglomerative clustering with a rich set of
features such as average sentence length, function words ratio, type-token ratio
and part of speech tags.

## Implementation details
The task is implemented using Java, Maven and Eclipse IDE.
The execution file is located in package main/Main.java.

### Dependencies
Maven dependencies are described in the pom.xml file in the root directory. 
There are other dependencies located in the dependencies folder in the root directory. They include:
- Postag helpers for Greek language.
- List of stop words for Dutch, English and Greek.
- List of punctuation marks
- Stanford classifier library

## Input format
For this task we are given several different subproblems. 
Each subproblem (input folder) consists of up to 100 documents. 
All of them are singleauthored, in the same language, and belong to the same genre: 
the language and the genre are given. 
The topic and the length of the documents vary, and the number of
distinct authors whose documents are included in the collection is unknown.

Example json file describing the input folders.
```
[
	{"language": "en",	"genre": "articles",	"folder": "problem001"},
	{"language": "en",	"genre": "articles",	"folder": "problem002"},
	{"language": "en",	"genre": "articles",	"folder": "problem003"},
	{"language": "en",	"genre": "reviews",		"folder": "problem004"},
	{"language": "en",	"genre": "reviews",		"folder": "problem005"},
	{"language": "en",	"genre": "reviews",		"folder": "problem006"},
	{"language": "nl",	"genre": "articles",	"folder": "problem007"},
	{"language": "nl",	"genre": "articles",	"folder": "problem008"},
	{"language": "nl",	"genre": "articles",	"folder": "problem009"},
	{"language": "nl",	"genre": "reviews",		"folder": "problem010"},
	{"language": "nl",	"genre": "reviews",		"folder": "problem011"},
	{"language": "nl",	"genre": "reviews",		"folder": "problem012"},
	{"language": "gr",	"genre": "articles",	"folder": "problem013"},
	{"language": "gr",	"genre": "articles",	"folder": "problem014"},
	{"language": "gr",	"genre": "articles",	"folder": "problem015"},
	{"language": "gr",	"genre": "reviews",		"folder": "problem016"},
	{"language": "gr",	"genre": "reviews",		"folder": "problem017"},
	{"language": "gr",	"genre": "reviews",		"folder": "problem018"}
]
```


## Output format
The application should provide two outputs for each input folder:

– Complete author clustering result: each cluster should contain all documents
found in the collection by a specific author. The clusters should be non-overlapping,
i.e. each document should belong to exactly one cluster.

Example clustering.json:
```
[
  [
    {
      "document": "document0001.txt"
    },
    {
      "document": "document0027.txt"
    },
    {
      "document": "document0007.txt"
    }
  ],
  [
    {
      "document": "document0020.txt"
    }
  ],
	...
	...
]
```

- Authorship-link ranking result: a list of document pairs ranked according to a
real-valued score in [0,1], where higher values denote higher confidence that the
pair of documents are written by the same author.

Example ranking.json:
```
[
  {
    "document1": "document0001.txt",
    "document2": "document0027.txt",
    "score": 1.0
  },
  {
    "document1": "document0003.txt",
    "document2": "document0037.txt",
    "score": 1.0
  },
	...
	...
]
```

## Configuration

### Local run
local.txt indicates whether the run is on local machine or on deployed environment.
If it is present the application uses the generated postag distributions.
The purpose is not to calculate them every time as it takes a lot of time.
If you want to use different input than the train one you should delete the ./postag folder
and it will be recreated for the new input. 

### Train mode
If the train folder is present the system knows you are not running the application in train mode.
Therefore in the deployed environment the train folder should be deployed or a train run should be made.
If the application is run in train mode it does a few more things:
- It generates train data and creates the missing train folder
- It calculates the error against the train data.

## Starting the application
To run from console use the following command: 
mvn exec:java -Dexec.args="-i $inputDataset -o $outputDir"

Both parameters are optional.
$inputDataset is the path to input folder. It is set to "./_DATA/dataset" by default.
$outputDir is the path to output folder. It is set to "./_DATA/output" by default.
