#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep  5 11:09:56 2018

@author: beidan
"""

# Import the library
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import matplotlib.patches

# Import the data
# Remember the customer the x1, x2 according to your needs!
dataset = pd.read_csv('plot.csv')
x1 = dataset.iloc[:-3,3].values
y1 = dataset.iloc[:-3,4].values
name = dataset.iloc[:,0]
name = np.asarray(name)

# Data classification
grouped = dataset.groupby('class')
spy = grouped.get_group(5)
iwd = grouped.get_group(4)

spy_x = spy.iloc[:,3].values
spy_y = spy.iloc[:,4].values
IWD_x = iwd.iloc[:,3].values
IWD_y = iwd.iloc[:,4].values
x1 = x1[0:len(x1)-2]
y1 = y1[0:len(y1)-2]

# Data cleaning
outliers = []
x1_bound =  np.mean(x1) + 0.6*np.std(x1)
y1_bound = np.mean(y1) + 0.3*np.std(y1)
for i in range(0,len(y1)):
    if (y1[i] > y1_bound):
        y1[i] = y1_bound+ round(np.mean(y1)/15,2)
        outliers.append(i)
for i in range(0,len(x1)):
    if (x1[i] > x1_bound):
        x1[i] = x1_bound + round(np.mean(x1)/15,2)
        outliers.append(i)


#### Set convas size
# Get current size
fig_size = plt.rcParams["figure.figsize"]
 
# Prints: [8.0, 6.0]
print ("Current size:", fig_size)
 
# Set figure width to 12 and height to 9
fig_size[0] = 36
fig_size[1] = 20
plt.rcParams["figure.figsize"] = fig_size

#### Plot data
# Plot main part data 
fig, ax = plt.subplots()

plt.plot(x1,y1,"o",marker='v',markersize=12)

plt.subplots_adjust(top=0.95, bottom=0.08, left=0.10, right=0.95, hspace=0.01,
                    wspace=0.2)

ax.set_xlim(0,x1_bound)
ax.set_ylim(0,y1_bound)
plt.xticks(np.arange(0, x1_bound, round(np.mean(x1)/10,2)),fontsize=18)
plt.yticks(np.arange(0, y1_bound, round(np.mean(y1)/10,2)),fontsize=18)

ax.grid(color='black', linestyle='-', linewidth=0.1)
# these are matplotlib.patch.Patch properties
props1 = dict(boxstyle='round', facecolor='lightcoral', alpha=0.5)
props2 = dict(boxstyle='round', facecolor='white', alpha=0.5)
props3 = dict(boxstyle='round', facecolor='moccasin', alpha=0.5)
props4 = dict(boxstyle='round', facecolor='lightgreen', alpha=0.5)


for i in range(0,len(x1)):
    if (dataset.iloc[i][5] == 0):
        ax.text(x1[i], y1[i]-round(np.mean(y1)/30,2), name[i], fontsize=15,
                verticalalignment='top', bbox=props1)
    if (dataset.iloc[i][5] == 1):
        ax.text(x1[i], y1[i]-round(np.mean(y1)/30,2), name[i], fontsize=15,
                verticalalignment='top', bbox=props2)
    if (dataset.iloc[i][5] == 2):
        ax.text(x1[i], y1[i]-round(np.mean(y1)/30,2), name[i], fontsize=15,
                verticalalignment='top', bbox=props3)
    if (dataset.iloc[i][5] == 3):
        ax.text(x1[i], y1[i]-round(np.mean(y1)/30,2), name[i], fontsize=15,
                verticalalignment='top', bbox=props4)

# Plot index
plt.plot(spy_x,spy_y,"o",marker='o',color='red', markersize=20)
ax.text(spy_x,spy_y-round(np.mean(y1)/30,2), 'SPY', fontsize=15,
            verticalalignment='top', bbox=dict(boxstyle='round', facecolor='tomato', alpha=0.5))
plt.plot(IWD_x,IWD_y,"o",marker='o',color='green',markersize=20)
ax.text(IWD_x,IWD_y-round(np.mean(y1)/30,2), 'IWD', fontsize=15,
            verticalalignment='top', bbox=dict(boxstyle='round', facecolor='blue', alpha=0.5))

# Change the style of outliers
"""for i in outliers:
    ax.text(x1[i], y1[i]-round(np.mean(y1)/30,2), name[i], fontsize=15,
            verticalalignment='top', bbox=dict(boxstyle='round', facecolor='yellow', alpha=0.5))"""

# Add Trendlines
plt.plot([spy_x,0,x1_bound],[spy_y,0,x1_bound * spy_y / spy_x],color='orange')
plt.plot([IWD_x,0,x1_bound],[IWD_y,0,x1_bound * IWD_y / IWD_x],color='lightgreen')

# Modify configuration
plt.suptitle('George 1yr FWD Sharpe Ratio vs.P/E Aug 31st', fontsize=25, fontweight='bold')
#ax.text(x1_bound*4/5, y1_bound*1/6, 'Value', style='italic',fontweight='bold',color='green')
ax.annotate('Value', xy=(IWD_x*3, IWD_y*3),xytext=(x1_bound*4/6, y1_bound*3/6),
            arrowprops=dict(facecolor='lightcyan', shrink=1),fontsize =20)
ax.annotate('Growth', xy=(spy_x*3, spy_y*3),xytext=(x1_bound*4/6, y1_bound*7/8),
            arrowprops=dict(facecolor='sandybrown', shrink=1),fontsize =20)

# Decoration
plt.xlabel('P/E (FWD)',fontsize=18)
plt.ylabel('Sharpe Ratio (FWD) \n =(R(FWD)-Rf)/SD(FWD)',fontsize=18)

# Save files and plot
plt.savefig('FWD_SR_PE_ft_9.11.png')
plt.show()