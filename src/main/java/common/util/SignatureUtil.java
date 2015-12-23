package common.util;

import java.util.*;

/**
 * @Description: 接口数据签名验证工具类
 * @anthor: shi_lin
 * @CreateTime: 2015-11-23
 */

public class SignatureUtil {


    /**
     * 拼接参数，
     * 每个参数使用 & 按照 key=value 连接 ，
     * 各个参数排列顺序，按照集合排序方法排列
     * @return
     */
    public static String createSignString(Map<String,String[]> params){
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer paramsString = new StringBuffer();
        for(int i = 0 ; i< keys.size(); i++){
            //去除签名参数
            if(keys.get(i).equalsIgnoreCase("sign")) continue;

            paramsString.append(keys.get(i))
                    .append("=")
                    .append(params.get(keys.get(i))[0]);
            if( i != keys.size() - 1){     //最后一个参数不拼接 & 符号
                paramsString.append("&");
            }
        }
        return paramsString.toString();

    }


    public static String createSignStrings(Map<String,Object> params){
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer paramsString = new StringBuffer();
        for(int i = 0 ; i< keys.size(); i++){
            //去除签名参数
            if(keys.get(i).equalsIgnoreCase("sign")) continue;

            paramsString.append(keys.get(i))
                    .append("=")
                    .append(params.get(keys.get(i)));
            if( i != keys.size() - 1){     //最后一个参数不拼接 & 符号
                paramsString.append("&");
            }
        }
        return paramsString.toString();

    }

}
