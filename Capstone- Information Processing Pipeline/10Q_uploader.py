#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Nov 18 10:15:06 2018

@author: beidan

This part is to upload the 10Q reports from local to API cloud.
"""


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

# In[2]:Configure API host and key
configuration = nucleus_client.Configuration()
configuration.host = 'http://api.sumup.ai:5000'
configuration.api_key['x-api-key'] = 'cQsxPf0AC9BEqLffeLZO1g'


# In[3]: Create dataset APIs
# # Dataset APIs

# ## Create API instance
api_instance = nucleus_client.DatasetsApi(nucleus_client.ApiClient(configuration))


# In[3.1]: Create dataset APIs
# ## Append file from local drive to dataset

all_dir = "/Users/beidan/nucleus-sdk/mdaresults/"
for file in os.listdir(all_dir): 
    mydir = "/Users/beidan/nucleus-sdk/mdaresults/"
    if (file[0].isdigit()):
        date = file[0:6] 
        mydir = mydir + date + "/"
        dataset = 'Top100N_test2_10Q_%s'%date
        
        i = 0
        for file in os.listdir(mydir): 
            file2 = file[:-4]
            file3 = file2.split("-")
            time = file3[2]+"/"+file3[3]+"/"+file3[1]
            metadata = {"time": "%s"%time}
                       
            try:
               api_instance.post_upload_file(mydir+file, dataset,metadata=metadata)
               print("success!")
                   
            except ApiException as e:
               print("Exception when calling DatasetsApi->post_upload_file: %s\n" % e)
        
# In[4]:# List available datasets

try:
    api_response = api_instance.get_list_datasets()
    pprint(api_response)
except ApiException as e:
    print("Exception when calling DatasetsApi->get_list_datasets: %s\n" % e)

# ## Get dataset information
dataset = 'dataset_final_10K_00' # str | Dataset name.
query = '' # str | Fulltext query, using mysql MATCH boolean query format. (optional)
metadata_selection = '' # str | json object of {\"metadata_field\":[\"selected_values\"]} (optional)
time_period = '' # str | Time period selection (optional)

try:
    api_response = api_instance.get_dataset_info(dataset, 
                                                 query=query, 
                                                 metadata_selection=metadata_selection, 
                                                 time_period=time_period)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling DatasetsApi->get_dataset_info: %s\n" % e)
    
    # In[10]: delete datasets


dataset = 'dataset_name' 
payload = nucleus_client.Deletedatasetmodel(dataset=dataset) # Deletedatasetmodel | 

try:
    api_response = api_instance.post_delete_dataset(payload)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling DatasetsApi->post_delete_dataset: %s\n" % e)
    
# List datasets again to check if the specified dataset has been deleted
try:
    api_response = api_instance.get_list_datasets()
    pprint(api_response)
except ApiException as e:
    print("Exception when calling DatasetsApi->get_list_datasets: %s\n" % e)

