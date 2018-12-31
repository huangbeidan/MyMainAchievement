#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Nov 18 11:04:47 2018

@author: beidan
"""



# In[0]: Introduction
"""
Dataset: Top100N_10Q_2009Q1 --- Top100N_10Q_2018Q3
"""

# In[1]: Import necessary classes
from __future__ import print_function
import csv, json
import time
import nucleus_client
from nucleus_client.rest import ApiException
from pprint import pprint
import errno
from json import JSONEncoder
import ast
from bs4 import BeautifulSoup
import os
import re

# In[2]: Define diy-classes here
class MyEncoder(JSONEncoder):
    def default(self,o):
        return o.__dict__


# In[3]: Configuration
"Use your api key here "

configuration = nucleus_client.Configuration()
#configuration.host = 'https://7h4tcw9nej.execute-api.us-west-2.amazonaws.com/beta'
configuration.host = 'http://api.sumup.ai:5000'
configuration.api_key['x-api-key'] = 'cQsxPf0AC9BEqLffeLZO1g'



# In[4]:
# # Topic APIs

# ## Create API Instance

api_instance = nucleus_client.TopicsApi(nucleus_client.ApiClient(configuration))


# In[5]:Get list of topics from dataset (topic_exposure are there)

mydir = "/Users/beidan/Library/Python/2.7/lib/python/site-packages/SECEdgar-0.1.3-py2.7.egg/SEC-Edgar-Data/"
dates = list()
dict_all = list()

"""dataset name generator"""
i=2009
while i<2011:
    j =1
    while j<4:
        date = str(i)+"Q"+str(j)
        dates.append(date)
        j+=1
    i+=1
        

with open('topic_exposure.csv', 'w') as csvfile:
    f = csv.writer(csvfile,delimiter=',') 
    f.writerow(["_doc_id","_doc_topic_exposure","keywords_weight", "_strength","_topic","date"])
    for date in dates:
     
        dataset = 'Top100N_10Q_%s'%date
        query = ''
        custom_stop_words = ["real","hillary"] # ERRORUNKNOWN | List of stop words. (optional)
        num_topics = 50 # int | Number of topics to be extracted from the dataset. (optional) (default to 8)
        metadata_selection ="" # str | json object of {\"metadata_field\":[\"selected_values\"]} (optional)
        time_period =""# str | Time period selection (optional)
        
        
        try:
           api_response = api_instance.get_topic_api(dataset, 
                                                     query=query, 
                                                     custom_stop_words=custom_stop_words, 
                                                     num_topics=num_topics, 
                                                     metadata_selection=metadata_selection,
                                                     time_period=time_period)         
           results = api_response.results
           
        except ApiException as e:
           print("Exception when calling TopicsApi->get_topic_api: %s\n" % e)
           
           #save the response
        for res in api_response.results:
#       print('Topic', i, 'keywords:')
#       print('    Keywords:', res.topic)
          keywords_weight_str = ",".join(str(x) for x in res.keywords_weight)
#       print('    Keyword weights:', keywords_weight_str)
#       print('    Strength:', res.strength)
          doc_topic_exposure_sel = []  # list of non-zero doc_topic_exposure
          doc_id_sel = []        # list of doc ids matching doc_topic_exposure_sel
          for j in range(len(res.doc_topic_exposure)):
             doc_topic_exp = float(res.doc_topic_exposure[j])
             if doc_topic_exp != 0:
                doc_topic_exposure_sel.append(doc_topic_exp)
                doc_id_sel.append(res.doc_id[j])
    
          doc_id_sel_str = ','.join(str(x) for x in doc_id_sel)
          doc_topic_exposure_sel_str = ','.join(str(x) for x in doc_topic_exposure_sel)
          f.writerow([doc_id_sel_str,doc_topic_exposure_sel_str,keywords_weight_str,res._strength,res._topic,date])
          print("success!")


 
 
# In[6]:# Get topic sentiment
 
mydir = "/Users/beidan/Library/Python/2.7/lib/python/site-packages/SECEdgar-0.1.3-py2.7.egg/SEC-Edgar-Data/"
years = list()
dict_all = list()

for file in os.listdir(mydir): 
    
    if (file[0].isdigit()):
        year = file[0:2] 
        years.append(year)
        dataset = 'dataset_final_10K_%s'%year
        query = 'patent'
        custom_stop_words = ["real","hillary"] # ERRORUNKNOWN | List of stop words. (optional)
        num_topics = 20 # int | Number of topics to be extracted from the dataset. (optional) (default to 8)
        metadata_selection ="" # str | json object of {\"metadata_field\":[\"selected_values\"]} (optional)
        time_period =""# str | Time period selection (optional)
        
        try:
           api_response = api_instance.get_topic_sentiment_api(dataset, 
                                                               query=query, 
                                                               custom_stop_words=custom_stop_words, 
                                                               num_topics=num_topics, 
                                                               )
    
           ## try to save the response
           data = MyEncoder().encode(api_response)
           json_acceptable_string = data.replace("'", "\"")
           d = json.loads(json_acceptable_string)
           dict_all.append(d)
           pprint(api_response)
        except ApiException as e:
               print("Exception when calling TopicsApi->get_topic_api: %s\n" % e)
               

#data = json.dumps(MyEncoder().encode(api_response))
with open('topic_sentiment_score.csv', 'w') as csvfile:
    f = csv.writer(csvfile,delimiter=',') 
    f.writerow(["Topic", "Sentiment", "Strength","year"])    
    j=0       
    for d in dict_all:
        year = years[j]
        for line in d['_results']:           
           f.writerow([line['_topic'],line['_sentiment'],line['_strength'],year])
           print("success!")
        j+=1
               
               
# In[7]:Get topic summary (sourceid from there)
api_instance_topic = nucleus_client.TopicsApi(nucleus_client.ApiClient(configuration))
dates = list()

"""dataset name generator"""
i=2009
while i<2010:
    j =1
    while j<4:
        date = str(i)+"Q"+str(j)
        dates.append(date)
        j+=1
    i+=1
with open('11272_topic_summary.csv', 'w') as csvfile:
    f = csv.writer(csvfile,delimiter=',') 
    f.writerow(["topic","document_id","title","sentences","author","source","time","date"])
    for date in dates:
     
        dataset = 'Top100N_10Q_%s'%date
        query = ''
        custom_stop_words = ["real","hillary"] # ERRORUNKNOWN | List of stop words. (optional)
        num_topics = 50 # int | Number of topics to be extracted from the dataset. (optional) (default to 8)
        num_keywords = 8 # int | Number of keywords per topic that is extracted from the dataset. (optional) (default to 8)
        summary_length = 100 # int | The maximum number of bullet points a user wants to see in each topic summary. (optional) (default to 6)
        context_amount = 0 # int | The number of sentences surrounding key summary sentences in the documents that they come from. (optional) (default to 0)
        num_docs = 100 # int | The maximum number of key documents to use for summarization. (optional) (default to 20)
        excluded_docs = '' # str | List of document IDs that should be excluded from the analysis. Example, \"docid1, docid2, ..., docidN\"  (optional)
        metadata_selection = ""
        try:
           api_response = api_instance_topic.get_topic_summary_api(dataset, 
                                                             query=query,
                                                             custom_stop_words=custom_stop_words, 
                                                             num_topics=num_topics, 
                                                             num_keywords=num_keywords, 
                                                             metadata_selection=metadata_selection,
                                                             summary_length=summary_length, 
                                                             context_amount=context_amount, 
                                                             num_docs=num_docs)           
           results = api_response.results
           
        except ApiException as e:
           print("Exception when calling TopicsApi->get_topic_api: %s\n" % e)
           
           #save the response
        for res in api_response.results:     
          documentid_sel = []  
          title_sel = []
          sentences_sel = []
          author_sel = []
          source_sel = []
          time_sel = []
               # list of doc ids matching doc_topic_exposure_sel
          for j in range(len(res.summary)):
              documentid_sel.append(res.summary[j].sourceid)
              title_sel.append(res.summary[j].title)
              sentences_sel.append(res.summary[j].sentences)
              author_sel.append(res.summary[j].attribute.author)
              source_sel.append(res.summary[j].attribute.source)
              time_sel.append(datetime.datetime.fromtimestamp(float(res.summary[j].attribute.time)))
              
          documentid_sel_str = ','.join(str(x) for x in documentid_sel)
          title_sel_str = ','.join(str(x) for x in title_sel)
          sentences_sel_str = ','.join(str(x) for x in sentences_sel)
          author_sel_str = ','.join(str(x) for x in author_sel)
          source_sel_str = ','.join(str(x) for x in source_sel)
          time_sel_str = ','.join(str(x) for x in time_sel)
          f.writerow([res._topic,documentid_sel_str,title_sel_str,sentences_sel_str,author_sel_str,source_sel_str,time_sel_str,date])
          print("success!")
          
# In[8]: Get Document information
api_instance_doc = nucleus_client.DocumentsApi(nucleus_client.ApiClient(configuration))          

dates = list()

"""dataset name generator"""
i=2009
while i<2019:
    j =1
    while j<4:
        date = str(i)+"Q"+str(j)
        dates.append(date)
        j+=1
    i+=1

"""document id generator"""
i = 1
doc_ids = list()
while i <120:
    doc_ids.append(str(i))
    i+=1

doc_titles=['']
with open('1129_sourceid.csv', 'w') as csvfile:
    f = csv.writer(csvfile,delimiter=',') 
    f.writerow(["document_id","doc_title","date"])
    for date in dates:
    
        dataset = 'Top100N_10Q_%s'%date
        
        try:
           api_response = api_instance_doc.get_doc_info(dataset, doc_ids=doc_ids)
           results = api_response.results
           
        except ApiException as e:
           print("Exception when calling TopicsApi->get_topic_api: %s\n" % e)
           
           #save the response

        for res in api_response.results:
          f.writerow([res.sourceid,res.title,date])
          print("success!")
          
               
    