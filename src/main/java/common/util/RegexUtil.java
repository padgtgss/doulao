package common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: common.util
 * @author: li_bo
 * @CreateTime: 2015-12-03
 */
public class RegexUtil {
    /**
     * 验证手机号格式
     * @param mobile
     * @return
     */
    public static boolean regexMobile(String mobile){
        Pattern p = Pattern.compile("^[1][3-8]+\\d{9}$");
        Matcher m = p.matcher(mobile);
        if(m.matches()){
            return true;
        }
        return false;
    }

    /**
     * 验证8位数字
     * @param numStr
     * @return
     */
    public static boolean regexEightNum(String numStr){
        Pattern p = Pattern.compile("^\\d{3,8}$");
        Matcher m = p.matcher(numStr);
        if(m.matches()){
            return true;
        }
        return false;
    }


}
