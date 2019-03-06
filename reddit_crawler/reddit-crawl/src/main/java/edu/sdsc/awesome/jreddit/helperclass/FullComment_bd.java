package edu.sdsc.awesome.jreddit.helperclass;

import com.github.jreddit.parser.entity.Comment;
import com.github.jreddit.parser.entity.Submission;

import java.util.List;
import java.util.Map;

public class FullComment_bd {


    private Submission submission;
    private List<String> allcomments;
    private Map<String, List<String>> featuresMap;

    public FullComment_bd(Submission submission, List<String> allcomments, Map<String, List<String>> featuresMap) {
        this.submission = submission;
        this.allcomments = allcomments;
        this.featuresMap = featuresMap;

    }

    /**
     *
     * @return the submission
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     *
     * @return the commentTree
     */
    public List<String> getAllcomments() {
        return allcomments;
    }

    // public Map<String,String> getCommentMap() {return CommentMap; }
    public Map<String, List<String>> getFeaturesMap() {
        return featuresMap;
    }




}
