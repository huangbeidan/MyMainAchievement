


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.jclouds.openstack.swift.v1.options.*;



public class ApiExample {

    /** Application name. */
    private static final String APPLICATION_NAME = "API Sample";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/java-youtube-api-tests");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl", "https://www.googleapis.com/auth/youtubepartner");

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = ApiExample.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader( in ));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /** return the json of a specific search
     *
     * @param part  e.g. "snippet";
     * @param maxResults 0 - 50
     * @param q "netflix offical trailer"
     * @param type ""
     * @return
     * @throws IOException
     */
    public String searchAPI(String part, String maxResults, String q, String type) throws IOException{

        YouTube youtube = getYouTubeService();

        try {
            HashMap<String, String> parameters = new HashMap<>();

            parameters.put("part", part);
            parameters.put("maxResults", maxResults);
            parameters.put("q", q);
            parameters.put("type", type);

            YouTube.Search.List searchListByKeywordRequest = youtube.search().list(parameters.get("part").toString());
            if (parameters.containsKey("maxResults")) {
                searchListByKeywordRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
            }

            if (parameters.containsKey("q") && parameters.get("q") != "") {
                searchListByKeywordRequest.setQ(parameters.get("q").toString());
            }

            if (parameters.containsKey("type") && parameters.get("type") != "") {
                searchListByKeywordRequest.setType(parameters.get("type").toString());
            }

            SearchListResponse response = searchListByKeywordRequest.execute();
            return response.toString();

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }


    public String searchAdvanceAPI(String part, String maxResults, String q, String type, String pageToken, String publishedBefore, String publishedAfter) throws IOException{

        YouTube youtube = getYouTubeService();

        try {
            HashMap<String, String> parameters = new HashMap<>();

            parameters.put("part", part);
            parameters.put("maxResults", maxResults);
            parameters.put("q", q);
            parameters.put("type", type);
            parameters.put("pageToken", pageToken);
            parameters.put("publishedBefore", publishedBefore);
            parameters.put("publishedAfter", publishedAfter);



            YouTube.Search.List searchListByKeywordRequest = youtube.search().list(parameters.get("part").toString());
            if (parameters.containsKey("maxResults")) {
                searchListByKeywordRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
            }

            if (parameters.containsKey("q") && !parameters.get("q").equals("")) {
                searchListByKeywordRequest.setQ(parameters.get("q").toString());
            }

            if (parameters.containsKey("type") && !parameters.get("type").equals("")) {
                searchListByKeywordRequest.setType(parameters.get("type").toString());
            }

            if (parameters.containsKey("pageToken") && !parameters.get("pageToken").equals("")) {
                searchListByKeywordRequest.setPageToken(parameters.get("pageToken").toString());
            }

            if (parameters.containsKey("publishedBefore") && !parameters.get("publishedBefore").equals("")) {
                searchListByKeywordRequest.setPublishedBefore(new DateTime(parameters.get("publishedBefore")));
            }

            if (parameters.containsKey("publishedAfter") && !parameters.get("publishedAfter").equals("")) {
                searchListByKeywordRequest.setPublishedAfter(new DateTime(parameters.get("publishedAfter")));
            }

            SearchListResponse response = searchListByKeywordRequest.execute();
            System.out.println("This is Advance Search API: " + response.toString());
            return response.toString();

        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * fter parse the json file in ParseJSON.java, begin to search in CommentThreads API
     *      * return the json file firs
     * @param part "snippet,replies"
     * @param videoId "e.g."o2AsIXSh2xo";
     * @return
     * @throws IOException
     */

    public String commentthreadAPI(String part, String videoId) throws IOException{

        YouTube youtube = getYouTubeService();
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", part);
            parameters.put("videoId", videoId);

            YouTube.CommentThreads.List commentThreadsListByVideoIdRequest = youtube.commentThreads().list(parameters.get("part").toString());
            if (parameters.containsKey("videoId") && !parameters.get("videoId").equals("")) {
                commentThreadsListByVideoIdRequest.setVideoId(parameters.get("videoId").toString());
            }

            CommentThreadListResponse response = commentThreadsListByVideoIdRequest.execute();
            return response.toString();

        }catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;

    }

    public void ytbvideoinfo(String response){

        Map<String,String> map = new HashMap<>();
        map = new ParseJSON().parseVideoIdANDtitle(response);

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "beidan", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            for(String videoId : map.keySet()) {

                String videoTitle = map.get(videoId);

                if (!videoId.equals("") && !videoId.isEmpty() && !videoTitle.isEmpty() && !videoTitle.equals("")) {

                    try {
                        stmt = c.createStatement();

                        String sql = "INSERT INTO ytbvideoinfo (videoid,videotitle) "
                                + "VALUES (\'" + videoId + "\'," + "\'" + videoTitle.replaceAll("\'", "`") + "\')";

                        System.out.println("This is sql: " +videoId + videoTitle);
                        stmt.executeUpdate(sql);

                    }catch (NullPointerException ne){
                        System.out.println("something wrong here");
                    }

                }
            }

            stmt.close();
            c.commit();
            c.close();


        }catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }



    }

    /** recursively search all the pages to get all the responses */
    public List<String> recursiveSearch(String snippet, String maxResult, String q, String type,
                                        String pageToken, String publishedBefore,
                                        String publishedAfter, List<String> res, String pagenumber)
            throws IOException{

        ApiExample example = new ApiExample();
        String AdvancedsearchResultJson = example.searchAdvanceAPI(snippet,maxResult,q,type,pageToken,publishedBefore,
                publishedAfter);

        JSONObject obj = new JSONObject(AdvancedsearchResultJson);
        JSONArray arr = obj.getJSONArray("items");

        int pageNumbers = Integer.parseInt(pagenumber);

        if(!arr.isEmpty() && pageNumbers>=0){

            //get the nextPageToken first
            String nextPageToken =  obj.getString("nextPageToken");
            // add the qualified response to our result list
            res.add(AdvancedsearchResultJson);

            // recursively search next
            recursiveSearch(snippet,maxResult,q,type,nextPageToken,publishedBefore,publishedAfter,res,String.valueOf(--pageNumbers));
        }

        return res;


    }



    public String extractVideoIdfromURL(String url){

        String[] split = url.split("v=");
        return split[split.length-1];

    }


    public void WriteResponseToSQL(String response){

        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "beidan", "");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonTree = jsonParser.parse(response);
            //System.out.println(jsonTree.toString());

            System.out.println("This is writeResponseToSQL_response " + response);


            if (jsonTree.isJsonObject()) {

                JsonObject jsonObject = jsonTree.getAsJsonObject();

                JsonArray items = jsonObject.getAsJsonArray("items");


                for (int i = 0; i < items.size(); i++) {

                    JsonObject jo = items.get(i).getAsJsonObject();
                    //get comment_id
                    JsonElement commentID = jo.get("id");

                    //get topLevelComment Object
                    JsonObject jo_snippet = jo.getAsJsonObject("snippet");
                    JsonObject topLevelComment = jo_snippet.getAsJsonObject("topLevelComment");

                    //get viedo_id
                    JsonElement videoID = jo_snippet.get("videoId");

                    //get topLeveComment snippet
                    JsonObject comment_snippet = topLevelComment.getAsJsonObject("snippet");


                    //get author details
                    JsonElement authorName = comment_snippet.get("authorDisplayName");
                    JsonElement publishedtime = comment_snippet.get("publishedAt");
                    //get comment details
                    JsonElement commentDetails = comment_snippet.get("textDisplay");

                    //System.out.println("video id: "+videoID+" id: "+commentID+" details: "+commentDetails + " author: "+ authorName + " published at: "+publishedtime);

                    //write the results to database
                    stmt = c.createStatement();

                    String cleantext = new MySQLUtils().cleanSQL(commentDetails.toString());


                    String sql = "INSERT INTO youtubedataapi (videoid,commentid,authorname,publishedtime,commentdetails) "
                            + "VALUES (\'" + videoID.toString() + "\'," + "\'" + commentID.toString().replaceAll("\'", "`") + "\'," + "\'" + authorName.toString().replaceAll("\'", "`") + "\'," + "\'" + publishedtime.toString() + "\',"
                            + "\'" + cleantext.replaceAll("\'", "`") + "\')";

                    //System.out.println(sql);
                    stmt.executeUpdate(sql);

                    System.out.println("This is sql: "+sql);


                }


            }

            stmt.close();
            c.commit();
            c.close();


        }catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);


        }






        }


    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, ParseException {

        Options options = new Options();

        /** options:
         * modelType - required. Two options: search or url
         * url(for url model) e.g. https://www.youtube.com/watch?v=3H9_s8Qrpvw
         *
         * part -- not required, default: snippet
         * maxResult -- not required, default:50, range:0-50
         * query -- required, e.g. netflix+official+trailer
         * type -- not required
         * pageToken -- not required
         * pulishedBefore -- not required, specific format: 2019-01-03T00:00:00Z
         * publishedAfter -- not required, specific format: 2019-01-01T00:00:00Z
         * pageNumbers -- required, e.g. 100
         *
         *
         */
        Option model = new Option("mo","model",true,"query or search");
        model.setRequired(true);
        options.addOption(model);

        Option part = new Option("p","part",true,"part");
        part.setRequired(false);
        options.addOption(part);

        Option url = new Option("u","url",true,"URL");
        part.setRequired(false);
        options.addOption(url);

        Option maxResult = new Option("n","maxresult",true,"maxResultPerPage");
        part.setRequired(false);
        options.addOption(maxResult);

        Option query = new Option("q","query",true,"Search query");
        part.setRequired(false);
        options.addOption(query);

        Option type = new Option("t","type",true,"type");
        part.setRequired(false);
        options.addOption(type);

        Option pageToken = new Option("pt","pageToken",true,"Page token");
        part.setRequired(false);
        options.addOption(pageToken);

        Option publishedBefore = new Option("timeb","publishedBefore",true,"pulished before");
        part.setRequired(false);
        options.addOption(publishedBefore);

        Option publishedAfter = new Option("timea","publishedAfter",true,"published after");
        part.setRequired(false);
        options.addOption(publishedAfter);

        Option pageNumber = new Option("pn","pageNumber",true,"page numbers");
        part.setRequired(false);
        options.addOption(pageNumber);



        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        final CommandLine commandLine = parser.parse(options,args);
        final String modelType = commandLine.getOptionValue("model","search").toLowerCase();
        final String urlLink = commandLine.getOptionValue("url");



        final String partName = commandLine.getOptionValue("part","snippet");
        final String mResult = commandLine.getOptionValue("maxresult","50");
        final String q = commandLine.getOptionValue("query");
        final String typeName = commandLine.getOptionValue("type");
        final String pageTokenCode = commandLine.getOptionValue("pageToken");
        final String publishB = commandLine.getOptionValue("publishedBefore");
        final String publishA = commandLine.getOptionValue("publishedAfter");
        final String pageN = commandLine.getOptionValue("pageNumber","5");




        /** Initialize and authorize, get the first response with input query */
        ApiExample example = new ApiExample();
        // String searchResultJson = example.searchAPI("snippet","30","Hulu","");
        // String AdvancesearchResultJson = example.searchAdvanceAPI("snippet","30","Hulu","","","2019-01-02T00:00:00Z","2019-01-01T23:55:00Z");

        /** keep search until the items become empty, or date exceed the restriction frame */
        //String nextPageToken = new ParseJSON().parsePageToken(AdvancesearchResultJson);



//        example.recursiveSearch("snippet","50","Netflix official trailer",
//                "","","2019-01-03T00:00:00Z","2019-01-01T23:00:00Z",
//                response_list,"5");


        if(modelType.equals("search")){

            List<String> all_commentThread_responses = new ArrayList<>();

            List<String> response_list = new ArrayList<>();

        example.recursiveSearch(partName,mResult,q,typeName,pageTokenCode,publishB,publishA,response_list,pageN);



            for (String rr : response_list) {

                /** write the videoinfo (video id + video info) to database
                 */
                example.ytbvideoinfo(rr);


                /** parse the searchAPI response and get a list of video ids */
                ParseJSON JSONparser = new ParseJSON();
                Map<String, String> videoInfo = JSONparser.parseVideoIdANDtitle(rr);

                if (!videoInfo.isEmpty()){


                    for (String videoId : videoInfo.keySet()) {
                        if (!videoId.equals("")) {
                            String response = example.commentthreadAPI("snippet,replies", videoId);
                            if(!response.isEmpty()) {
                                all_commentThread_responses.add(response);
                            }

                        }


                    }

                    for (String res : all_commentThread_responses) {

                        example.WriteResponseToSQL(res);

                    }
                }
            }


        } else if(modelType.equals("url")){

            String videoId = example.extractVideoIdfromURL(urlLink);
            System.out.println("videoId is: " + videoId);
            String response = example.commentthreadAPI("snippet,replies", videoId);

            example.WriteResponseToSQL(response);

        }

    }

}


