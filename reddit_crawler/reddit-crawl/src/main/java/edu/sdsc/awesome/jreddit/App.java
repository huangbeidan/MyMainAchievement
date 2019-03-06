package edu.sdsc.awesome.jreddit;


import com.github.jreddit.oauth.RedditOAuthAgent;
import com.github.jreddit.oauth.RedditToken;
import com.github.jreddit.oauth.app.RedditApp;
import com.github.jreddit.oauth.app.RedditInstalledApp;
import com.github.jreddit.oauth.client.RedditClient;
import com.github.jreddit.oauth.client.RedditHttpClient;
import com.github.jreddit.oauth.exception.RedditOAuthException;
import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.exception.RedditParseException;
import com.github.jreddit.parser.listing.SubmissionsListingParser;
import com.github.jreddit.request.retrieval.param.SubmissionSort;
import com.github.jreddit.request.retrieval.submissions.SubmissionsOfSubredditRequest;

import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws RedditOAuthException, RedditParseException {
        System.out.println( "Hello World!" );

        // Information about the app
        String userAgent = "jReddit: Reddit API Wrapper for Java";
        String clientID = "JKJF3592jUIisfjNbZQ";
        String redirectURI = "https://www.example.com/auth";

// Reddit application
        RedditApp redditApp = new RedditInstalledApp(clientID, redirectURI);
        RedditOAuthAgent agent = new RedditOAuthAgent(userAgent, redditApp);
        RedditClient client = new RedditHttpClient(userAgent, HttpClientBuilder.create().build());

// Create a application-only token (will be valid for 1 hour)
        RedditToken token = agent.tokenAppOnly(false);

// Create parser for request
        SubmissionsListingParser parser = new SubmissionsListingParser();

// Create the request
        SubmissionsOfSubredditRequest request = (SubmissionsOfSubredditRequest) new SubmissionsOfSubredditRequest("programming", SubmissionSort.HOT).setLimit(100);

// Perform and parse request, and store parsed result
        List<Submission> submissions = parser.parse(client.get(token, request));

// Now print out the result (don't care about formatting)
        System.out.println(submissions);




    }
}
