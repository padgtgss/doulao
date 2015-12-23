package common.util;

import java.util.Random;

/**
 * @Description: common.util
 * @author: li_bo
 * @CreateTime: 2015-12-08
 */
public class RandomUtil {

    /**
     * 控制触发时间的几率
     * @param a
     * @return
     */
    public static boolean chance(double a){
        double random = Math.random();
        if(random<a){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 生成长度为length的字符串
     * @param length
     * @return
     */
    public static String createRandomNumStr(int length,int serial){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        boolean flag= true;
        while(flag){
            sb.append(random.nextInt(9)+1);
            for(int i = 0; i <(length-1); i++){
                sb.append(random.nextInt(10));
            }
            sb = filterSerialNum(serial,sb);
            if(sb==null){
                flag = true;
                sb = new StringBuilder();
            }else{
                flag = false;
            }
        }
        String str = sb.toString();
        return str;
    }

    /**
     * 过滤掉连续n位相同字符的StringBuilder类型的字符串
     * @param n
     * @param str
     * @return
     */
    public static StringBuilder filterSerialNum(int n , StringBuilder str){
        int length = str.length();
        int a = 0;
        Character baseChar;
        Character moveChar;
        int continuity = 1;
        boolean flag = true;
        for(int i = 0; i<=(length-n);i++){
            baseChar = str.charAt(i);
            if (flag){
                for(int j = (i+1); j<length; j++){
                    moveChar = str.charAt(j);
                    if(!baseChar.equals(moveChar)){
                        continuity = 1;
                        break;
                    }else{
                        continuity++;
                        if(continuity>=n){
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
        if(flag){
            return str;
        }else{
            return null;
        }
    }

}
