# MyMainAchievement

Hello! Welcome to my Github!

My name is Beidan(Sophia), I am currently a master of Finance student at UCSD, but I've made up my mind to study for another master's degree for comuputer science. I know it seems a bit impetus, but I just love computer programming so much that I couldn't let it go.

Starting from nothing, I can now proficiently use Java and Python to build APIs and UIs to do web crawling, information extracting, web development, data analysis & machine learning. But, my education background is not technical at all, I've spent my every second in building up my skills to strength my resume.

Knowing how I am crazy about computer science and how valuable the opportunities for me, I am going to exert all my efforts into it, either study or jobs.

If you are interested in me, please contact me at huangbeidan@gmail.com.

## Research Project- Web Crawling and Knowledge Graph Construction
### Function 1: Spider/src/com/example/CSEOneStep.java
1. Read rawurl.txt seed file and generate requests
2. Use Google Custom Search to get the most updated content of seed websites (e.g. 1 day);
3. Extract urls elments from the acquired JSON file and delete the unwanted urls(ads,emails,surveys,etc.)
4. Nutch crawl result and Solr parser the index
5. Clean up the JSON file and convert it into CSV for topic models

Output 1 - json22csv.csv (Cleaned up results after Nutch and Solr, with url, content displayed)
[![image](https://github.com/huangbeidan/Spider/blob/master/assets/output33.png)](#capture)

### Function 2: src/com/news/NEWS_ONEStep
1. Filter the original result.txt (1st url list) to delete unwanted links.
2. Pull all the news from the filtered-url-list and save into txt file (in html format)
3. Clean up the allnews.txt file (delete html tags) and generate clean_10FactNews.txt file
4. Get rid of all stop words and stem them! This will generate allkeywords_10Facts.txt file
5. Step five: Count top N words of each news piece and this will generate TopNwords_10Facts.txt file

(You can find all the outputs file in files named by dates, e.g. 2018-11-13)

Output 2 - TopNwords_10Facts.txt 
[![image](https://github.com/huangbeidan/SpiderNews/blob/master/assets/output11.png)](#capture)

### Function 3: src/com/corenlp/dict4.java
1. Read all news from "10Knewsarticles.csv"
2. Use Stanford NLP to do POST TAGING and analyze the structure
3. Keep Proper Nouns phrases and the original sentences while removing all stop words and stemming them
4. Write all the extracted and filtered NNPs to local disk file 

Output 3 - dictionary.csv 
[![image](https://github.com/huangbeidan/Spidernews/blob/master/assets/output22.png)](#capture)

### Function 4: src/graph/PGraph.java
1. Construct the TreeMap by reading from csv: Vertex + Sentence1/Sentence2/...
2. Inverted index to get string keys in an array
3. Build the graph by connecting first vertex on each

Output 4 - Graph result - each number represents a particular NNPs
[![image](https://github.com/huangbeidan/Spidernews/blob/master/assets/output33.png)](#capture)


## Web Development Project- Content Management System Using PHP
### Main Functions: 
1. User login/logout/sign up & Password encrypting
2. View all stocks pools/ Stocked selected by user IDs/ Upload stocks from file / Edit & Delete stocks via bulk options
3. Dynamic stock charts 
4. View, add or delete news posts & comments that are related by foreign keys in the database

## Content Management System using PHP - Overview
[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/admin.png)](#capture)

## Content Management System using PHP - Main Page
Please login using the guest accout:

http://wealth-engine.com/we

Username: guest
Password: 123456

## Java Project- Bear Map
This is a web mapping application of berkeley.

I developed the backend of it using Java, Apache Maven, AWS.

This application can handle features such as query autocomplete responses, place searching, map displaying, zooming in/out, and finding the shortest path between to user-inputted points.

[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/bearmap.gif)](#capture)

## Machine Learning Project - Open AI
[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/openai.gif)](#capture)

## Web Development Project - Todolist
[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/todolist.gif)](#capture)
