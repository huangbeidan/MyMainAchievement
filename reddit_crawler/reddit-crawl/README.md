## Social Media API Wrapper Project- San Diego Supercomputer Center

Main class could be found in:src/main/java/edu/sdsc/awesome/jreddit/Wrapper.java

Parameters are:

-subreddit="Netflix" (required)

-limitS="10" (optional: the maximum number of submission to retrieve)


Functions:
1. Name a thread of Reddit as input
2. Grab all conversations of the thread with depth preset (submissions, comments, etc.)
2. Save the results into the database
3. More Features could be added

Output 1 - SUBMISSION TABLE (submission id + a list of comment id <Depth First Search>)
[![image](https://github.com/huangbeidan/Spider/raw/master/assets/output44.png)](#capture)

Output 2 - FEATURES TABLE (submission id + comment id + selected features)
[![image](https://github.com/huangbeidan/Spider/raw/master/assets/output55.png)](#capture)

Output 3 - ALLCOMMENTS TABLE (submission details + all comments combined <ordered by DFS>)
[![image](https://github.com/huangbeidan/Spider/raw/master/assets/output66.png)](#capture)

