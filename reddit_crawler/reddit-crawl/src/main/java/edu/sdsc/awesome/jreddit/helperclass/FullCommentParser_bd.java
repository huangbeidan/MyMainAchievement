package edu.sdsc.awesome.jreddit.helperclass;

import com.github.jreddit.oauth.RedditOAuthAgent;
import com.github.jreddit.oauth.RedditToken;
import com.github.jreddit.oauth.app.RedditApp;
import com.github.jreddit.oauth.app.RedditInstalledApp;
import com.github.jreddit.oauth.client.RedditClient;
import com.github.jreddit.oauth.client.RedditHttpClient;
import com.github.jreddit.oauth.exception.RedditOAuthException;
import edu.sdsc.awesome.jreddit.helperclass.Comment;
import com.github.jreddit.parser.entity.Kind;
import com.github.jreddit.parser.entity.More;
import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.entity.imaginary.CommentTreeElement;
import com.github.jreddit.parser.exception.RedditParseException;
import com.github.jreddit.parser.util.JsonUtils;
import com.github.jreddit.request.retrieval.mixed.FullSubmissionRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.jreddit.parser.util.JsonUtils.safeJsonToString;





public class FullCommentParser_bd {
    public List<String> allcommentbody = new ArrayList<String>();
    public Map<String, List<String>> featuresMap = new HashMap<String, List<String>>();


    protected static final JSONParser JSON_PARSER = new JSONParser();

    public FullCommentParser_bd(){

    }

    /**
     * Parse JSON received from reddit into a full submission.
     * A full submissions means it has both (a) the submission, and (b) the comment tree.
     *
     *
     * @return Full submission
     *
     * @throws ParseException
     */

    public FullCommentParser_bd(List<String> allcommentbody,Map<String, List<String>> featuresMap ){
        this.allcommentbody = allcommentbody;
        this.featuresMap = featuresMap;
    }


    public FullCommentParser_bd AllComentParser (String submissionIdentifier) throws RedditOAuthException, RedditParseException {
        // Information about the app
        String userAgent = "jReddit: Reddit API Wrapper for Java";
//        String clientID = "KN0I2zXecRY4QA";
//        String redirectURI = "https://github.com/jReddit/jReddit";
        String clientID = "PfnhLt3VahLrbg";
        String redirectURI = "https://github.com/snkas/jReddit";

        // Reddit application
        RedditApp redditApp = new RedditInstalledApp(clientID, redirectURI);

        // Create OAuth agent
        RedditOAuthAgent agent = new RedditOAuthAgent(userAgent, redditApp);

        // Create request executor
        RedditClient client = new RedditHttpClient(userAgent, HttpClientBuilder.create().build());

        // Create token (will be valid for 1 hour)
        RedditToken token = agent.tokenAppOnly(false);

        // Create parser for request
        FullCommentParser_bd parser = new FullCommentParser_bd();

        // Create the request ID e.g. 9xead9
        FullSubmissionRequest request = new FullSubmissionRequest(submissionIdentifier).setDepth(3);

        // Perform and parse request, and store parsed result
        //FullSubmission fullSubmission = parser.parse(client.get(token, request));

        // Now print out the result of the submission (don't care about formatting)
        //Submission s = fullSubmission.getSubmission();
        //System.out.println(s);

        // Perform and parse request, and store parsed result
        FullComment_bd fullcomment = parser.parse(client.get(token, request));

        // Now print out the result of the submission (don't care about formatting)
        List<String> allcomments = fullcomment.getAllcomments();
        //Map<String, String> commentmap = fullcomment.getCommentMap();
        Map<String, List<String>> featuresMap = fullcomment.getFeaturesMap();


        return new FullCommentParser_bd(allcomments, featuresMap);

    }



    public FullComment_bd parse(String jsonText) throws RedditParseException {

        try {

            // Parse JSON text
            Object response = JSON_PARSER.parse(jsonText);
            //System.out.println(response);

            // Validate response
            validate(response);

            // Create submission (casting with JSON is horrible)
            JSONObject main = (JSONObject) ((JSONArray) response).get(0);
            Submission submission = new Submission((JSONObject) ((JSONObject) ((JSONArray)((JSONObject) main.get("data")).get("children")).get(0)).get("data"));

            // Create comment tree
            JSONObject mainTree =  (JSONObject) ((JSONArray) response).get(1);
            List<CommentTreeElement> commentTree = parseRecursive(mainTree);

            // Create allcomments tree + allcomments map
            parseRecursive2(mainTree);
            List<String> allbodies = getAllboxdies();
            //Map<String,String> commentmap = getCommentMap();
            Map<String, List<String>> featuresMap = getfeaturesMapMap();

            // Printing out body to verify mannually
//            for(String body:allbodies){
//                System.out.println(body);
//            }

            // Return the set of submission and its comment tree
            return new FullComment_bd(submission,  allbodies, featuresMap);

        } catch (ParseException pe) {
            throw new RedditParseException(pe);
        }

    }


    public List<String> getAllboxdies() {
        return allcommentbody;
    }
    //public Map<String,String> getCommentMap() { return commentMap; }
    public Map<String, List<String>> getfeaturesMapMap() { return featuresMap; }


    protected void parseRecursive2(JSONObject main) throws RedditParseException {



        // Iterate over the comment tree results
        JSONArray array = (JSONArray) ((JSONObject) main.get("data")).get("children");
        for (Object element : array) {

            List<String> features = new ArrayList<String>();

            // Get the element
            JSONObject data = (JSONObject) element;

            // Make sure it is of the correct kind
            String kind = safeJsonToString(data.get("kind"));

            // If it is a comment
            if (kind != null && kind.equals(Kind.COMMENT.value())) {

                // Create comment
                Comment comment = new Comment((JSONObject) data.get("data"));

                // Create comment bd
                String body = comment.getBody().replaceAll("(?m)^[ \t]*\r?\n", " ");
                body.replaceAll("(?m)^[\\p{Zs}\t]+$", "");
                String id = comment.getID();
                String parent = comment.getParentId();
                String parentid = parent.split("_")[1];
                String parent_type = parent.split("_")[0];
                String author = comment.getAuthor();
                String score = String.valueOf(comment.getScore());
                features.add(body);features.add(parentid);features.add(parent_type);features.add(author);features.add(score);

                //System.out.println(body);
                if(!body.equals(" ") && body != null && id!=null &&!id.equals("") && !body.contains("[removed]") && !body.contains("[deleted]")){
                    allcommentbody.add(body);

                    featuresMap.put(id,features);}

                // Retrieve replies
                // Object replies = comment.getReplies();
                Object replies = ((JSONObject) data.get("data")).get("replies");

                // If it is an JSON object
                if (replies instanceof JSONObject) {
                    comment.setReplies(parseRecursive((JSONObject) replies));
                    parseRecursive2((JSONObject) replies);

                    // If there are no replies, end with an empty one
                } else {
                    comment.setReplies(new ArrayList<CommentTreeElement>());
                }

            }

            // If it is a more
            if (kind != null && kind.equals(Kind.MORE.value())) {

                // Add to comment tree
                // commentTree.add(new More((JSONObject) data.get("data")));

            }

        }


    }




    /**
     * Parse a JSON object consisting of comments and add them
     * to the already existing list of comments. This does NOT create
     * a new comment list.
     *
     */

    protected List<CommentTreeElement> parseRecursive(JSONObject main) throws RedditParseException {

        List<CommentTreeElement> commentTree = new ArrayList<CommentTreeElement>();

        // Iterate over the comment tree results
        JSONArray array = (JSONArray) ((JSONObject) main.get("data")).get("children");
        for (Object element : array) {

            // Get the element
            JSONObject data = (JSONObject) element;

            // Make sure it is of the correct kind
            String kind = safeJsonToString(data.get("kind"));

            // If it is a comment
            if (kind != null && kind.equals(Kind.COMMENT.value())) {

                // Create comment
                Comment comment = new Comment( (JSONObject) data.get("data") );

                //you can get all comment variables here. e.g
                //comment.getAuthor();comment.getBody();

                // Retrieve replies
                Object replies = ((JSONObject) data.get("data")).get("replies");

                // If it is an JSON object
                if (replies instanceof JSONObject) {
                    comment.setReplies(parseRecursive( (JSONObject) replies ));

                    // If there are no replies, end with an empty one
                } else {
                    comment.setReplies(new ArrayList<CommentTreeElement>());
                }

                // Add comment to the tree
                commentTree.add(comment);
            }

            // If it is a more
            if (kind != null && kind.equals(Kind.MORE.value())) {

                // Add to comment tree
                commentTree.add(new More((JSONObject) data.get("data")));

            }

        }

        return commentTree;

    }

    /**
     * Validate that it is in fact a full submission.
     *

     */
    public void validate(Object response) throws RedditParseException {

        // Check for null
        if (response == null) {
            throw new RedditParseException();
        }

        // Check it is a JSON response
        if (response instanceof JSONObject) {

            // Cast to JSON object
            JSONObject jsonResponse = (JSONObject) response;

            // Check for error
            if (jsonResponse.get("error") != null) {
                throw new RedditParseException(JsonUtils.safeJsonToInteger(jsonResponse.get("error")));
            } else {
                throw new RedditParseException("invalid json format, started with object (should start with array)");
            }

        }

        // It must start with an array
        if (!(response instanceof JSONArray)) {
            throw new RedditParseException("invalid json format, did not start with array");
        }

    }
}
