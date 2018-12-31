# MyMainAchievement

=========

Hello! Welcome to my Github!

My name is Beidan(Sophia), I am currently a master of Finance student at UCSD, but I've made up my mind to study for another master's degree for comuputer science. I know it seems a bit impetus, but I just love computer programming so much that I couldn't let it go.

Starting from nothing, I can now proficiently use Java and Python to build APIs and UIs to do web crawling, information extracting, web development, data analysis & machine learning. But, my education background is not technical at all, I've spent my every second in building up my skills to strength my resume.

Knowing how I am crazy about computer science and how valuable the opportunities for me, I am going to exert all my efforts into it, either study or jobs.

If you are interested in me, please contact me at huangbeidan@gmail.com.

## Research Project- Web Crawling and Knowledge Graph Construction
### Function 1: src/com/news/NEWS_ONEStep
1. Filter the original result.txt (1st url list) to delete unwanted links.
2. Pull all the news from the filtered-url-list and save into txt file (in html format)
3. Clean up the allnews.txt file (delete html tags) and generate clean_10FactNews.txt file
4. Get rid of all stop words and stem them! This will generate allkeywords_10Facts.txt file
5. Step five: Count top N words of each news piece and this will generate TopNwords_10Facts.txt file

(You can find all the outputs file in files named by dates, e.g. 2018-11-13)

Output 1 - TopNwords_10Facts.txt 
[![image](https://github.com/huangbeidan/SpiderNews/blob/master/assets/output11.png)](#capture)

### Function 2: src/com/corenlp/dict4.java
1. Read all news from "10Knewsarticles.csv"
2. Use Stanford NLP to do POST TAGING and analyze the structure
3. Keep Proper Nouns phrases and the original sentences while removing all stop words and stemming them
4. Write all the extracted and filtered NNPs to local disk file 

Output 2 - dictionary.csv 
[![image](https://github.com/huangbeidan/Spidernews/blob/master/assets/output22.png)](#capture)

### Function 3: src/graph/PGraph.java
1. Construct the TreeMap by reading from csv: Vertex + Sentence1/Sentence2/...
2. Inverted index to get string keys in an array
3. Build the graph by connecting first vertex on each

Output 3 - Graph result - each number represents a particular NNPs
[![image](https://github.com/huangbeidan/Spidernews/blob/master/assets/output33.png)](#capture)


## Java Project- Bear Map
[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/bearmap.gif)](#capture)

## Machine Learning Project - Open AI
[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/openai.gif)](#capture)

## Web Development Project - Todolist
[![image](https://github.com/huangbeidan/MyMainAchievement/blob/master/asset/todolist.gif)](#capture)

