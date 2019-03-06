package edu.sdsc.awesome.jreddit;



import com.github.jreddit.oauth.RedditOAuthAgent;
import com.github.jreddit.oauth.RedditToken;
import com.github.jreddit.oauth.app.RedditApp;
import com.github.jreddit.oauth.app.RedditInstalledApp;
import com.github.jreddit.oauth.client.RedditClient;
import com.github.jreddit.oauth.client.RedditHttpClient;
import com.github.jreddit.oauth.client.RedditPoliteClient;
import com.github.jreddit.oauth.exception.RedditOAuthException;
import com.github.jreddit.parser.entity.Comment;
import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.entity.imaginary.CommentTreeElement;
import edu.sdsc.awesome.jreddit.helperclass.FullComment_bd;
import com.github.jreddit.parser.entity.imaginary.FullSubmission;
import com.github.jreddit.parser.exception.RedditParseException;
import com.github.jreddit.parser.listing.SubmissionsListingParser;
import edu.sdsc.awesome.jreddit.helperclass.FullCommentParser_bd;
import com.github.jreddit.parser.single.FullSubmissionParser;
import com.github.jreddit.parser.util.CommentTreeUtils;
import com.github.jreddit.request.retrieval.mixed.FullSubmissionRequest;
import com.github.jreddit.request.retrieval.param.*;
import com.github.jreddit.request.retrieval.submissions.SubmissionsOfSubredditRequest;
import com.opencsv.CSVWriter;
import edu.sdsc.awesome.jreddit.helperclass.FullCommentParser_bd;
import org.apache.commons.cli.*;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wrapper {

    // Information about the app
    public static final String USER_AGENT = "jReddit: Reddit API Wrapper for Java";
    //    public static final String CLIENT_ID = "KN0I2zXecRY4QA";
//    public static final String REDIRECT_URI = "https://github.com/jReddit/jReddit";
    public static final String CLIENT_ID = "PfnhLt3VahLrbg";
    public static final String REDIRECT_URI = "https://github.com/snkas/jReddit";

    // Variables
    private RedditApp redditApp;
    private RedditOAuthAgent agent;
    private RedditClient client;

    public Wrapper() throws RedditOAuthException {

        // Reddit application
        redditApp = new RedditInstalledApp(CLIENT_ID, REDIRECT_URI);

        // Create OAuth agent
        agent = new RedditOAuthAgent(USER_AGENT, redditApp);

        // Create client
        client = new RedditPoliteClient(new RedditHttpClient(USER_AGENT, HttpClientBuilder.create().build()));

    }

    /**
     * get a full list of comments with depth set
     */
    public List<String> AllComments(String submissionIdentifier) throws RedditOAuthException, RedditParseException {
        FullCommentParser_bd bd = new FullCommentParser_bd();
        return bd.AllComentParser(submissionIdentifier).getAllboxdies();
    }

//    public Map<String,String> CommentMap(String submissionIdentifier) throws RedditOAuthException, RedditParseException {
//        FullCommentParser_bd bd = new FullCommentParser_bd();
//        return bd.AllComentParser(submissionIdentifier).getCommentMap();
//    }

    public Map<String, List<String>> featuresMap(String submissionIdentifier) throws RedditOAuthException, RedditParseException {
        FullCommentParser_bd bd = new FullCommentParser_bd();
        return bd.AllComentParser(submissionIdentifier).getfeaturesMapMap();
    }

    /**
     * get a full list of submissions
     */
    public Object[] FullSubmissionArray(String subreddit, String limitN) throws RedditParseException, RedditOAuthException {
        // Create token (will be valid for 1 hour)
        RedditToken token = agent.tokenAppOnly(false);

        // by beidan: get submission list

        SubmissionsListingParser parser2 = new SubmissionsListingParser();
        SubmissionsOfSubredditRequest request2 = (SubmissionsOfSubredditRequest) new SubmissionsOfSubredditRequest(subreddit, SubmissionSort.HOT).setLimit(Integer.parseInt(limitN));
        List<Submission> submissions2 = parser2.parse(client.get(token, request2));

        Object[] myArray = submissions2.toArray();

        return myArray;

    }

    /**
     * Utility: get submittion id from full submission content
     */

    public String extractSubmissionID(Object fullsubmission) {

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(fullsubmission.toString());
        if (m.find() && m.group(1).contains("t3_")) {

            return m.group(1).toString();
            //System.out.println(submission_id.toString());
        }
        return "";
    }

    /**
     * get a full list of submissions, this wil return a full list of submission ids
     */
    public List<String> FullSubmissionid() throws RedditParseException, RedditOAuthException {
        // Create token (will be valid for 1 hour)
        RedditToken token = agent.tokenAppOnly(false);
        // by beidan: get submission list
        SubmissionsListingParser parser2 = new SubmissionsListingParser();
        SubmissionsOfSubredditRequest request2 = (SubmissionsOfSubredditRequest) new SubmissionsOfSubredditRequest("netflix", SubmissionSort.HOT).setLimit(100);
        List<Submission> submissions2 = parser2.parse(client.get(token, request2));
        Object[] myArray = submissions2.toArray();
        List<String> submission_id = new ArrayList<String>();
        for (Object myObject : myArray) {
            //id + title
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(myObject.toString());
            while (m.find() && m.group(1).contains("t3_")) {
                submission_id.add(m.group(1));
                //System.out.println(submission_id.toString());
            }
        }
        return submission_id;
    }

    public static void main(String[] args) throws RedditOAuthException, RedditParseException, ParseException {

        /** argument input part */
        /** there are two arguments
         * subreddit name -- e.g. Netflix （required)
         * limit number (limit the number of submission list, default 10 (optional)
         */

        Options options = new Options();

        Option subreddit = new Option("s","subreddit",true,"subReddit Name");
        subreddit.setRequired(true);
        options.addOption(subreddit);

        Option limitSubmission = new Option("l","limitS",true,"Limit Submission Number");
        limitSubmission.setRequired(false);
        options.addOption(limitSubmission);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        final CommandLine commandLine = parser.parse(options,args);
        final String subredditName = commandLine.getOptionValue("subreddit");
        final String limitS = commandLine.getOptionValue("limitS","10");


        Wrapper example = new Wrapper();

        /** Fully submission_list: submission id + a list of all comments ID array (depth preset in FullCommentParser_bd.java) */
        Object[] submission_list = example.FullSubmissionArray(subredditName,limitS);

        for(Object submission:submission_list){
            String subId = example.extractSubmissionID(submission).substring(3);


            //
       /** Fully comment list and featuresMap*/
            FullCommentParser_bd full = new FullCommentParser_bd();
            List<String> all_comments = example.AllComments("9xead9");
            Map<String, List<String>> featuresMap = example.featuresMap(subId);
//

            if(!featuresMap.isEmpty()) {
                for (String com : featuresMap.keySet()) {
                    System.out.println(com + ":" + featuresMap.get(com));
                }
//
                /** Write submission tables and comment details tables to database */
                example.writeFeaturesMap2DB(featuresMap);
//       // example.writeCommentDetails2DB(commentMap);
//       //  example.write2DBV2(submission_list);
//       // example.write2csv(submission_list);

            }



        }




    }

    /**
     * Below are helper methods to write data to database
     */
    public void writeFeaturesMap2DB(Map<String, List<String>> featuresMap) {

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "beidan", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            for (String com : featuresMap.keySet()) {

                List<String> features = featuresMap.get(com);

                String body = features.get(0).replaceAll("\"", "&quote;");
                body = body.replaceAll("\'", "’");
                String parentid = features.get(1);
                String parent_type = features.get(2);
                String author = features.get(3);
                String score = features.get(4);


                // write the result to database here
                stmt = c.createStatement();

                String sql = String.format("DELETE FROM featuresMap WHERE \"commentid\" = '%s';", com);
                sql = sql + "INSERT INTO featuresMap (commentid,bodytext,parentid,parent_type,author,score) "
                        + "VALUES (\'" + com + "\'," + "\'" + body + "\'," + "\'" + parentid + "\'," + "\'" + parent_type + "\',"
                        + "\'" + author + "\'," + "\'" + score + "\')";
                stmt.executeUpdate(sql);
            }

            stmt.close();
            c.commit();
            c.close();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public void writeCommentDetails2DB(Map<String, String> commentMap) {

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "beidan", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            for (String com : commentMap.keySet()) {
                String val = commentMap.get(com).replaceAll("\"", "&quote;");
                val = val.replaceAll("\'", "’");

                // write the result to database here
                stmt = c.createStatement();
                //String sql = "UPDATE commentdetails SET bodytext = val WHERE commentid = com;";
                String sql = String.format("DELETE FROM commentdetails WHERE \"commentid\" = '%s';", com);
                sql = sql + "INSERT INTO commentdetails (commentid,bodytext) "
                        + "VALUES (\'" + com + "\'" + "," + "\'" + val + "\')";

                // sql = sql + "WHERE NOT EXISTS (SELECT commentid FROM commentdetails WHERE commentid = com);";

//                sql = sql + "ON CONFLICT (commentid)"
//                        + "DO UPDATE SET bodytext = " +  String.format("%s",val);

                stmt.executeUpdate(sql);
            }

            stmt.close();
            c.commit();
            c.close();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");


    }

    public void write2csv(Object[] submission_list) throws RedditParseException, RedditOAuthException {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File("comments_table.csv");

        int i = 0;

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            //put the value from dict to array so as to write to the csv file

            for (Object submission : submission_list) {


                if (i > 10) break;

                String sub_id = extractSubmissionID(submission);
                List<String> all_comments = new Wrapper().AllComments(sub_id.substring(3));
                String sub = submission.toString();

                writer.writeNext(new String[]{sub, all_comments.toString()});
                System.out.println("Suceess!");
                i++;
            }

            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * The database should look like: submission id + column with allcomments (bodytext) in one cell
     */
    public void write2DBV2(Object[] submission_list) throws RedditParseException, RedditOAuthException {

        // Create requests for all the submission here
        /** connect to the database and pass along all the results */

        Connection c = null;
        Statement stmt = null;
        int i = 0;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "beidan", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            for (Object submission : submission_list) {

                if (i > 10) break;

                String sub_id = extractSubmissionID(submission);
                List<String> all_comments = new Wrapper().AllComments(sub_id.substring(3));
                String sub = submission.toString().replaceAll("\"", "&quote;");
                sub = sub.replaceAll("\'", "’");
                String all_comments_tostring = all_comments.toString().replaceAll("\"", "&quote;");
                all_comments_tostring = all_comments_tostring.replaceAll("\'", "’");


                // write the result to database here
                stmt = c.createStatement();

                String sql = "INSERT INTO allcomments (submission,comments) "
                        + "VALUES (\'" + sub + "\'" + "," + "\'" + all_comments_tostring + "\');";
                stmt.executeUpdate(sql);

            }
            i++;
            stmt.close();
            c.commit();
            c.close();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");

    }

    /**
     * The database should look like: submission id + a list of comment id
     */

    public void write2dbV1(List<String> submission_id) throws RedditParseException, RedditOAuthException {

        RedditToken token = agent.tokenAppOnly(false);

        int i = 0;

        // Create requests for all the submission here
        /** connect to the database and pass along all the results */

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "beidan", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");


            for (String sub : submission_id) {

                if (i > 10) break;

                // Create parser for request
                FullSubmissionParser parser = new FullSubmissionParser();

                // Create the request
                FullSubmissionRequest request = new FullSubmissionRequest(sub.substring(3)).setDepth(3);

                // Perform and parse request, and store parsed result
                FullSubmission fullSubmission = parser.parse(client.get(token, request));


                // Now print out the result of the submission (don't care about formatting)
                Submission s = fullSubmission.getSubmission();
                // System.out.println(s);

                // Now print out the result of the comment tree (don't care about formatting)
                //System.out.println(CommentTreeUtils.printCommentTree(fullSubmission.getCommentTree()));

                // Flatten the tree
                List<CommentTreeElement> flat = CommentTreeUtils.flattenCommentTree(fullSubmission.getCommentTree());


                // Retrieve ALL comments hiding behind MOREs


                StringBuilder sb_comment = new StringBuilder();

                for (CommentTreeElement e : flat) {

                    // we can set number limit to comments here
                    if (e instanceof Comment) {

                        // get the comment identifiers inside the bracket
                        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(e.toString());
                        while (m.find()) {

                            sb_comment.append(m.group(1) + ",");

                        }
                    }

                }

                i++;
                System.out.println(sb_comment);
                // write the result to database here
                stmt = c.createStatement();

                String sql = "INSERT INTO submissions (submission_id,commentid_list) "
                        + "VALUES (\'" + sub + "\'" + "," + "\'" + sb_comment + "\');";
                stmt.executeUpdate(sql);

            }

            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");

    }
}

