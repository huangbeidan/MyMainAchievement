#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Nov 30 10:03:50 2018

@author: beidan



This part is to combine the topic exposure csv, topic summary csv, so that we could get doc id
for each topic exposure
"""

import pandas as pd
import numpy as np

#read data
exp=pd.read_csv("/Users/beidan/nucleus-sdk/teamxi_sumup/11_25_topic_exposure.csv")
ids=pd.read_csv("/Users/beidan/nucleus-sdk/teamxi_sumup/1129_sourceid.csv")


# data transformation
df1=pd.DataFrame()
for j in range(0, len(exp)):
    df2=pd.DataFrame()
    df3=pd.DataFrame()
    df4=pd.DataFrame()
    date = exp.iloc[j,5]
    topic = exp.iloc[j,4]
    array_id = exp.iloc[j,0].split(',')
    array_id = list(map(int, array_id))
    array_exp = exp.iloc[j,1].split(',')
    time_length = len(array_exp)
    df2 = pd.DataFrame([i for i in array_id], columns=['document_id'])
    df2['exposure'] = pd.DataFrame([i for i in array_exp])
    df2['date'] = pd.DataFrame([date]*time_length)
    df2['topic'] = pd.DataFrame([topic]*time_length)
    
    df3 = ids.loc[ids['date'] == date]
    df4 = pd.merge(df2,df3,on='document_id',how='left')
    
    df1 = pd.concat([df1,df4],ignore_index=True,sort='False')

# Write results to csv files
writer = pd.ExcelWriter('1129_allcombined.xlsx')
df1.to_excel(writer,'Sheet1')
writer.save()