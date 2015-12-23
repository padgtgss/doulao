package common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.thoughtworks.xstream.XStream;
import com.yslt.doulao.chat.entity.InviteRequestBody;
import com.yslt.doulao.chat.entity.MembersRequestBody;
import common.entity.BaseBusinessException;
import common.var.exception.GroupError;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 数据结构转换
 * Created by Libo on 2015/11/27.
 */
public class FormatStructure {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static XmlMapper xmlMapper = new XmlMapper();

    //HashMap转换成Xml的字符串形式，针对需要一维Xml
    public static String MapToXmlStr(Map<String, Object> map) {
        StringBuilder xmlStr = new StringBuilder();
        Iterator it = map.entrySet().iterator();
        xmlStr.append("<Request>");
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            xmlStr.append("<" + key + ">");
            xmlStr.append(value);
            xmlStr.append("</" + key + ">");
        }
        xmlStr.append("</Request>");
        return xmlStr.toString();
    }

    //用jackson把Xml转换成json
    public static String XmlToJsonStr(String xmlStr) {
        StringWriter writer = new StringWriter();
        try {
            JsonParser jp = xmlMapper.getFactory().createParser(xmlStr);
            JsonGenerator jg = objectMapper.getFactory().createGenerator(writer);
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
            jp.close();
            jg.close();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //对象转换成xml
    public static String objectToXml(String groupId,String[] receiverIds,String confirm,String declared) {
        InviteRequestBody inviteRequestBody = new InviteRequestBody();
        //设置数据
        inviteRequestBody.setGroupId(groupId);
        List<MembersRequestBody> list = new ArrayList<MembersRequestBody>();
        MembersRequestBody member = null;
        for(int i = 0;i<receiverIds.length;i++){
            /*GroupChatServiceImpl g = new GroupChatServiceImpl();
            //根据id获取子账户
            SubAccounts subAccounts = g.getSubAccount(receiverIds[i].toString());
            if(subAccounts!=null){
                member = new MembersRequestBody(subAccounts.getVoipAccount());
            }else{
                throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
            }*/
            member = new MembersRequestBody(receiverIds[i]);
            list.add(member);
        }
        inviteRequestBody.setMembers(list);
        inviteRequestBody.setConfirm(confirm);
        inviteRequestBody.setDeclared(declared);
        //开始解析
        XStream xStream = new XStream();
        xStream.alias("Request",InviteRequestBody.class);
        xStream.alias("111",MembersRequestBody.class);
        String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String xmlStr = xmlHead+xStream.toXML(inviteRequestBody);
        xmlStr = xmlStr.replaceAll("</111>","");
        xmlStr = xmlStr.replaceAll("<111>","");
        xmlStr = xmlStr.replaceAll("\n","");
        return xmlStr;
    }

    //对象转换成xml
    public static String objectToXml2(String groupId,String[] memberIds) {
        InviteRequestBody inviteRequestBody = new InviteRequestBody();
        //设置数据
        inviteRequestBody.setGroupId(groupId);
        List<MembersRequestBody> list = new ArrayList<MembersRequestBody>();
        MembersRequestBody member = null;
        for(int i = 0;i<memberIds.length;i++){
            /*GroupChatServiceImpl g = new GroupChatServiceImpl();
            //根据id获取子账户
            SubAccounts subAccounts = g.getSubAccount(memberIds[i].toString());
            if(subAccounts!=null){
                member = new MembersRequestBody(subAccounts.getVoipAccount());
            }else{
                throw new BaseBusinessException(GroupError.FAILED_GET_SUBACCOUNT);
            }*/
            member = new MembersRequestBody(memberIds[i]);
            list.add(member);
        }
        inviteRequestBody.setMembers(list);
        //开始解析
        XStream xStream = new XStream();
        xStream.alias("Request",InviteRequestBody.class);
        xStream.alias("111",MembersRequestBody.class);
        String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String xmlStr = xmlHead+xStream.toXML(inviteRequestBody);
        xmlStr = xmlStr.replaceAll("</111>","");
        xmlStr = xmlStr.replaceAll("<111>","");
        xmlStr = xmlStr.replaceAll("\n","");
        return xmlStr;
    }

    //解析xml
    public static List<String> AnalyzeXmlByDom4j(String xml){
        List<String> list = new ArrayList<String>();
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            System.out.println("root element:"+root.getName());
            Element statusCode = root.element("statusCode");
            if(!"000000".equals(statusCode.getData())){
                throw new BaseBusinessException(GroupError.RL_NOT_000000);
            }
            Iterator iterator = root.elementIterator("members");
            while (iterator.hasNext()){
                Element e = (Element)iterator.next();
                Iterator iterator1 = e.elementIterator("member");
                while (iterator1.hasNext()){
                    Element e2 = (Element)iterator1.next();
                    String memberId = e2.elementTextTrim("voipAccount");
                    list.add(memberId);
                }
            }
        } catch (DocumentException e) {
            throw new BaseBusinessException(GroupError.XML_ANALIZE_FAILED);
        }
        return list;
    }

    //测试map转xmlStr
   /* public static void main(String[] args){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("名字","李博");
        map.put("性别","男");
        System.out.println(FormatStructure.MapToXmlStr(map));
    }*/

    //测试xml转jsonStr
  /*public static void main(String[] args){
       String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
               "<Response>\n" +
               "  <statusCode>000000</statusCode>\n" +
               "  <groupId>g80000049837921</groupId>\n" +
               "</Response>";
       System.out.println(FormatStructure.XmlToJsonStr(xmlStr));
   }*/

}