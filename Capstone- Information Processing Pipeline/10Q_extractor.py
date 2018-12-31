#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Nov 17 09:30:56 2018

@author: beidan

This file is for extracting certain sessions (e.g. ITEM 2A, ITEM 1.A only) from orginial files
see part 4-6


"""
# In[0]: Import all the classes
from __future__ import print_function
import csv, json
import time
import nucleus_client
from nucleus_client.rest import ApiException
from pprint import pprint
import errno
from json import JSONEncoder
import ast
import os
import re
import itertools

import unicodedata
from collections import namedtuple
from glob import glob

import requests
from bs4 import BeautifulSoup
from docopt import docopt
from tqdm import tqdm

# In[1]: Define some useful classes
# Class Encoder
class MyEncoder(JSONEncoder):
    def default(self,o):
        return o.__dict__

def cleanhtml(raw_html):
  cleanr = re.compile('<.*?>')
  cleantext = re.sub(cleanr, '', raw_html)
  return cleantext

def cleantxt(first_clean):
    second_clean = first_clean.replace('\n','')
    third_clean = second_clean.replace('"`@','')
    return third_clean

# In[2]: Let's try to clean up the txt file a little bit

all_dir = "/Users/beidan/nucleus-sdk/data/"
for file in os.listdir(all_dir): 
    mydir = "/Users/beidan/nucleus-sdk/data/"
    if (file[0].isdigit()):
        year = file[0:4] 
        quarter = file[4:6]
        mydir = mydir + year + quarter + "/"
        
        for txtfile in os.listdir(mydir):
            if txtfile.endswith(".txt"):
              with open(mydir+txtfile, 'r') as myfile:
                   rawfile=myfile.read()
            
     
                   cleantext = BeautifulSoup(rawfile, "lxml").text
                   cleantext = cleanhtml(cleantext)
                   cleantext = cleantxt(cleantext)
                   
                   
                   text_file = open(mydir+txtfile, "w")
                   text_file.write(cleantext)
                   print("success!")
                   text_file.close()
# In[3]: Test for cleaning files and extracting information

with open("/Users/beidan/nucleus-sdk/data/2018Q2/100548-2018-08-07.txt", 'r') as myfile:
                   rawfile=myfile.read()
            
     
                   cleantext = BeautifulSoup(rawfile, "lxml").text
#                   cleantext = cleanhtml(cleantext)
#                   cleantext = cleantxt(cleantext)
                   
                   
                   text_file = open("/Users/beidan/nucleus-sdk/data/2018Q2/100548-2018-08-07.txt", "w")
                   text_file.write(cleantext)
                   print("success!")
                   text_file.close()
                   
            
# In[5]: Extracting information for MDM only
def extract_mda(form10k_dir):
    form10k_dir = "/Users/beidan/Desktop/capstonecace/18/"
    assert os.path.exists(form10k_dir)
    mda_dir = "/Users/beidan/Desktop/capstonecace/mda_test/"
    if not os.path.exists(mda_dir):
        os.makedirs(mda_dir)

    for form10k_file in tqdm(sorted(glob(os.path.join(form10k_dir, "*.txt")))):
        print("extracting mda from form10k file {}".format(form10k_file))

        # Read form 10k
        with open(form10k_file, 'r') as fin:
            text = fin.read()

        # Normalize
        text = normalize_text(text)

        # Find MDA section
        mda, end = parse_mda(text)
        # Parse second time if first parse results in index
        if mda and len(mda.encode('utf-8')) < 1000:
            mda, _ = parse_mda(text, start=end)

        if mda:
            filename = os.path.basename(form10k_file)
            name, ext = os.path.splitext(filename)
            mda_path = os.path.join(mda_dir, name + ".txt")
            print("writing mda to {}".format(mda_path))
            with open(mda_path, 'w') as fout:
                fout.write(mda)
        else:
            print("extract mda failed - {}".format(form10k_file))


def normalize_text(text):
    """Nomralize Text
    """
    text = unicodedata.normalize("NFKD", text)  # Normalize
    text = '\n'.join(
        text.splitlines())  # Let python take care of unicode break lines
    # Convert to upper
    text = text.upper()  # Convert to upper
    # Take care of breaklines & whitespaces combinations due to beautifulsoup parsing
    text = re.sub(r'[ ]+\n', '\n', text)
    text = re.sub(r'\n[ ]+', '\n', text)
    text = re.sub(r'\n+', '\n', text)
    # To find MDA section, reformat item headers
    text = text.replace('\n.\n', '.\n')  # Move Period to beginning
    text = text.replace('\nI\nTEM', '\nITEM')
    text = text.replace('\nITEM\n', '\nITEM ')
    text = text.replace('\nITEM  ', '\nITEM ')
    text = text.replace(':\n', '.\n')
    # Math symbols for clearer looks
    text = text.replace('$\n', '$')
    text = text.replace('\n%', '%')
    # Reformat
    text = text.replace('\n', '\n\n')  # Reformat by additional breakline
    return text
def parse_mda(text, start=0):
    debug = False
    """Parse normalized text 
    """
    mda = ""
    end = 0
    """
        Parsing Rules
    """

    # Define start & end signal for parsing
    item2_begins = [
        '\nITEM 2.', '\nITEM 2 –', '\nITEM 2:', '\nITEM 2 ', '\nITEM 2\n'
    ]
    item2_ends = ['\nITEM 3']
    if start != 0:
        item2_ends.append('\nITEM 2')  # Case: ITEM 7A does not exist
    item3_begins = ['\nITEM 3']
    """
        Parsing code section
    """
    text = text[start:]

    # Get begin
    for item2 in item2_begins:
        begin = text.find(item2)
        if debug:
            print(item2, begin)
        if begin != -1:
            break

    if begin != -1:  # Begin found
        for item3 in item2_ends:
            end = text.find(item3, begin + 1)
            if debug:
                print(item1A, end)
            if end != -1:
                break

        if end == -1:  # ITEM 7A does not exist
            for item3 in item3_begins:
                end = text.find(item3, begin + 1)
                if debug:
                    print(item3, end)
                if end != -1:
                    break

        # Get MDA
        if end > begin:
            mda = text[begin:end].strip()
        else:
            end = 0

    return mda, end

# In[4]:Generalize parse_mda
"Parse consecutive items"
def parse_session(text,begin_item,end_item,next_end,start=0):
    debug = False
    """Parse normalized text 
    """

    mda = ""
    end = 0
    """
        Parsing Rules
    """

    # Define start & end signal for parsing
    item_begins = [
        ('\n%s.')%begin_item, ('\n%s –')%begin_item, ('\n%s:')%begin_item, ('\n%s ')%begin_item, ('\n%s\n')%begin_item
    ]
    item_ends = ['\n%s'%end_item]
#    if start != 0:
#        item_ends.append('\n%s'%begin_item)  # Case: ITEM 7A does not exist
    item_next_ends = ['\n%s'%next_end]
    """
        Parsing code section
    """
    text = text[start:]

    # Get begin
    for item in item_begins:
        begin = text.find(item)
        if debug:
            print(item, begin)
        if begin != -1:
            break

    if begin != -1:  # Begin found
        for item_next in item_ends:
            end = text.find(item_next, begin + 1)
            if debug:
                print(item_next, end)
            if end != -1:
                break

        if end == -1:  # ITEM 7A does not exist
            for item_next in item_next_ends:
                end = text.find(item_next, begin + 1)
                if debug:
                    print(item_next, end)
                if end != -1:
                    break

        # Get MDA
        if end > begin:
            mda = text[begin:end].strip()
        else:
            end = 0

    return mda, end

# In[5]: Generalize model for extracting sessions
def extract_session(form10k_dir,mda_dir):
    begin_item = "ITEM 2"
    end_item = "ITEM 6"
    next_end = "Signatures"
#    form10k_dir = "/Users/beidan/nucleus-sdk/data/" --input value
    assert os.path.exists(form10k_dir)
#    mda_dir = "/Users/beidan/nucleus-sdk/mdaresults/" --input value
    if not os.path.exists(mda_dir):
        os.makedirs(mda_dir)

    for form10k_file in tqdm(sorted(glob(os.path.join(form10k_dir, "*.txt")))):
        print("extracting mda from form10k file {}".format(form10k_file))

        # Read form 10k
        with open(form10k_file, 'r') as fin:
            text = fin.read()

        # Normalize
        text = normalize_text(text)

        # Find MDA section
        mda, end = parse_session(text,begin_item,end_item,next_end,start=0)
        # Parse second time if first parse results in index
        if mda and len(mda.encode('utf-8')) < 1000:
            mda, _ = parse_session(text, begin_item,end_item,next_end,start=end)

        if mda:
            filename = os.path.basename(form10k_file)
            name, ext = os.path.splitext(filename)
            mda_path = os.path.join(mda_dir, name + ".txt")
            print("writing mda to {}".format(mda_path))
            with open(mda_path, 'w') as fout:
                fout.write(mda)
        else:
            print("extract mda failed - {}".format(form10k_file))

# In[6]: Finally, extract all the files for ITEM 2 -- ITEM 6 only !!!!!

mydir = "/Users/beidan/nucleus-sdk/data/"
mdadir = "/Users/beidan/nucleus-sdk/mdaresults/"

for file in os.listdir(mydir): 
    
    if (file[0].isdigit()):
        date = file[0:6] 
#        years.append(year)
        inputpath = mydir + date
        mdapath = mdadir + date
        extract_session(inputpath,mdapath)
        
             