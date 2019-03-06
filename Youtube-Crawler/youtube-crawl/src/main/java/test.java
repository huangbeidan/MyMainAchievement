import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {




    public static void main(String[] args){
        String s = "https://www.youtube.com/watch?v=3H9_s8Qrpvw";

        String[] split = s.split("v=");
        System.out.println(split[split.length-1]);
    }



}
