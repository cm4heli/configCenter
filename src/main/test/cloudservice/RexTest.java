package cloudservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RexTest {
   public static void main(String args[]) throws Exception{
	   test1();
   }
   private static  void test1(){
	   int matchCount = 0;
	   Pattern pattern =   
	   Pattern.compile("(.*),(.*),(.*)"); 
	   Matcher matcher = pattern.matcher(new String("2,123李斌,3few"));
	   System.out.println(matcher.groupCount());
        if (matcher.matches()) {
            matchCount++;
            int groupCount = matcher.groupCount();
            for (int i = 0 ;i < groupCount + 1; i++) {
                System.out.println("TotalGroup:" + groupCount +", Group:" + i+ " , Value:"+ matcher.group(i));
            }
        }
        else {
            System.out.println("can't match with pattern.");
        }
	   
   }
}
