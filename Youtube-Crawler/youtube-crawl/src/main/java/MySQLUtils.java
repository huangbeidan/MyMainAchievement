/**
 * Mysql Utilities
 *
 * @author Ralph Ritoch <rritoch@gmail.com>
 * @copyright Ralph Ritoch 2011 ALL RIGHTS RESERVED
 * @link http://www.vnetpublishing.com
 *
 */



public class MySQLUtils {

    /**
     * Escape string to protected against SQL Injection
     *
     * You must add a single quote ' around the result of this function for data,
     * or a backtick ` around table and row identifiers.
     * If this function returns null than the result should be changed
     * to "NULL" without any quote or backtick.
     *
     * @param link
     * @param str
     * @return
     * @throws Exception
     */

    public static String mysql_real_escape_string(java.sql.Connection link, String str)
            throws Exception
    {
        if (str == null) {
            return null;
        }

        if (str.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/? ]","").length() < 1) {
            return str;
        }

        String clean_string = str;
        clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
        clean_string = clean_string.replaceAll("\\n","\\\\n");
        clean_string = clean_string.replaceAll("\\r", "\\\\r");
        clean_string = clean_string.replaceAll("\\t", "\\\\t");
        clean_string = clean_string.replaceAll("\\00", "\\\\0");
        clean_string = clean_string.replaceAll("'", "\\\\'");
        clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

        if (clean_string.replaceAll("[a-zA-Z0-9_!@#$%^&*()-=+~.;:,\\Q[\\E\\Q]\\E<>{}\\/?\\\\\"' ]"
                ,"").length() < 1)
        {
            return clean_string;
        }

        java.sql.Statement stmt = link.createStatement();
        String qry = "SELECT QUOTE('"+clean_string+"')";

        stmt.executeQuery(qry);
        java.sql.ResultSet resultSet = stmt.getResultSet();
        resultSet.first();
        String r = resultSet.getString(1);
        return r.substring(1,r.length() - 1);
    }

    /**
     * Escape data to protected against SQL Injection
     *
     * @param link
     * @param str
     * @return
     * @throws Exception
     */

    public static String quote(java.sql.Connection link, String str)
            throws Exception
    {
        if (str == null) {
            return "NULL";
        }
        return "'"+mysql_real_escape_string(link,str)+"'";
    }

    /**
     * Escape identifier to protected against SQL Injection
     *
     * @param link
     * @param str
     * @return
     * @throws Exception
     */

    public static String nameQuote(java.sql.Connection link, String str)
            throws Exception
    {
        if (str == null) {
            return "NULL";
        }
        return "`"+mysql_real_escape_string(link,str)+"`";
    }

    public String cleanSQL(String str){

        String clean_string = str;
        clean_string = clean_string.replaceAll("\\\\", "\\\\\\\\");
        clean_string = clean_string.replaceAll("\\n","\\\\n");
        clean_string = clean_string.replaceAll("\\r", "\\\\r");
        clean_string = clean_string.replaceAll("\\t", "\\\\t");
        clean_string = clean_string.replaceAll("\\00", "\\\\0");
        clean_string = clean_string.replaceAll("'", "\\\\'");
        clean_string = clean_string.replaceAll("\\\"", "\\\\\"");

        return clean_string;

    }

    public String cleanNonwords(String str){
        str = str.replaceAll("[^\\p{L}\\p{Nd}\\s]+","");
        return str;
    }

    public static void main(String[] args){

        String test = "<b>Netflix</b> :Hey you fat lazy piece of shit! We see you alone on Valentine’s Day, so here’s a new show to remind you how alone you’ll always be!”<br /><b>Me</b> :thank u master\" author: \"ryann45\" published at: \"2019-02-07T19:58:31.000Z\"\n";

        MySQLUtils cleaner = new MySQLUtils();
        String after = cleaner.cleanSQL(test);
        after = cleaner.cleanNonwords(after);
        System.out.println(after);


    }

}