#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Nov 15 18:33:20 2018

@author: beidan
"""

# In[0]: Generate the list of index files archived in EDGAR since start_year (earliest: 1993) until the most recent quarter
import datetime
 
# Please download index files chunk by chunk. For example, please first download index files during 1993–2000, then
# download index files during 2001–2005 by changing the following two lines repeatedly, and so on. If you need index
# files up to the most recent year and quarter, comment out the following three lines, remove the comment sign at
# the starting of the next three lines, and define the start_year that immediately follows the ending year of the
# previous chunk.
 
start_year = 2009       # change start_year and end_year to re-define the chunk
current_year = 2018    # change start_year and end_year to re-define the chunk
current_quarter = 4     # do not change this line
 
years = list(range(start_year, current_year))
quarters = ['QTR1', 'QTR2', 'QTR3', 'QTR4']
history = [(y, q) for y in years for q in quarters]
for i in range(1, current_quarter + 1):
    history.append((current_year, 'QTR%d' % i))
urls = ['https://www.sec.gov/Archives/edgar/full-index/%d/%s/master.idx' % (x[0], x[1]) for x in history]
urls.sort()
 
# In[1]: Download index files and write content into SQLite
import sqlite3
import requests
 
con = sqlite3.connect('/Users/beidan/edgar_idx.db')
cur = con.cursor()
cur.execute('DROP TABLE IF EXISTS idx')
cur.execute('CREATE TABLE idx (cik TEXT, conm TEXT, type TEXT, date TEXT, path TEXT)')
 
for url in urls:
    lines = requests.get(url).content.decode("utf-8", "ignore").splitlines()
    records = [tuple(line.split('|')) for line in lines[11:]]
    cur.executemany('INSERT INTO idx VALUES (?, ?, ?, ?, ?)', records)
    print(url, 'downloaded and wrote to SQLite')
con.commit()
con.close()
 
## Clean-up the database to hold only 10Q
conn = sqlite3.connect('/Users/beidan/edgar_idx.db')
cursor = conn.cursor()
 
sql = """
DELETE FROM idx
WHERE type != '10-Q'
"""
cursor.execute(sql)
conn.commit()
conn.close()

# In[2]: Sort the database and return the company with a full 4-year 10Q report
conn = sqlite3.connect('/Users/beidan/edgar_idx.db')
cursor = conn.cursor()
 
sql = """
create table idx2(cik TEXT, conm TEXT, type TEXT, date TEXT, path TEXT);
INSERT INTO idx2
SELECT * FROM  idx 
where conm in (
   select conm from idx
   group by conm
   having count(distinct date) = 30
   )
order by cis
"""
cursor.execute(sql)
conn.commit()
conn.close()

    
# In[3]: Write to csv file , it's not an optimal way, see part [5]
import csv
import requests
import re
import os

def Q_generator(filename):
    if int(filename.split("-")[1]) <=5 :
        Q = 1
    elif int(filename.split("-")[1]) <=8:
        Q = 2
    else:
        Q = 3
    return Q

i=0
with open('/Users/beidan/idx3_clean.csv', newline='') as csvfile:
    reader = csv.reader(csvfile, delimiter=',')
    headers = next(reader, None)
  
    for line in reader:
       if i<3000:              
            fn1 = line[0]
            fn2 = re.sub(r'[/\\]', '', line[1])
            fn3 = re.sub(r'[/\\]', '', line[2])
            fn4 = line[3]
            fn5 = re.sub('/','-',fn4)
            year = fn5.split("-")[0]
        
            saveas = '-'.join([fn1, fn5])+".txt"
            # Reorganize to rename the output filename.
            url = 'https://www.sec.gov/Archives/' + line[4].strip()
            Q = Q_generator(fn5)
            filename_dir = year+"Q"+str(Q)
            output_dir = "/Users/beidan/nucleus-sdk/data/%s/"%filename_dir
                 
                 
            if not os.path.exists(output_dir):
               try:
                   os.makedirs(output_dir)
               except OSError as exception:
                   pass
                          
            with open(output_dir+saveas, 'wb') as f:
                f.write(requests.get('%s' % url).content)
                print(url, 'downloaded and wrote to text file')
            i+=1
            


# In[5] Better way- filter before download
# With the selected index, we are good to download
            
import itertools
import csv
import re
import os

import unicodedata
from collections import namedtuple
from glob import glob

import requests
from bs4 import BeautifulSoup
from docopt import docopt
from tqdm import tqdm

def Q_generator(filename):
    if int(filename.split("-")[1]) <=5 :
        Q = 1
    elif int(filename.split("-")[1]) <=8:
        Q = 2
    else:
        Q = 3
    return Q
       
            
i=0
with open('/Users/beidan/idx3_clean.csv', 'r') as fin:  
     reader = csv.reader(fin, delimiter=',', quotechar='\"', quoting=csv.QUOTE_ALL)
     headers = next(reader, None)
     for line in reader:        
        if i<30:
            
           fn1 = line[0]
           fn2 = re.sub(r'[/\\]', '', line[1])
           fn3 = re.sub(r'[/\\]', '', line[2])
           fn4 = line[3]
           fn5 = re.sub('/','-',fn4)
           year = fn5.split("-")[0]
           url = 'https://www.sec.gov/Archives/' + line[4].strip()
           print('request 10k html - {}'.format(url))
        
           saveas = '-'.join([fn1, fn5])+".txt"
           # Reorganize to rename the output filename.
           url = 'https://www.sec.gov/Archives/' + line[4].strip()
           Q = Q_generator(fn5)
           filename_dir = year+"Q"+str(Q)
           output_dir = "/Users/beidan/nucleus-sdk/data/%s/"%filename_dir
             
           if not os.path.exists(output_dir):

               os.makedirs(output_dir)

           try:              
               res = requests.get(url)
               soup = BeautifulSoup(res.content, "html.parser")
               text = soup.get_text("\n")
                          
               with open(output_dir+saveas, 'w') as fout:
                   
                   fout.write(text)
           except Exception as e:
                  print("download 10k failed - {} - {}".format(url, e))  
           i+=1

            

                   
        
