package io.github.sinetian;
import java.util.*;
import java.security.MessageDigest;
import java.net.*;
import java.io.*;
import com.alibaba.fastjson2.*;
public class Main {
    public static String encryption(String plainText) {
            String re_md5 = new String();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(plainText.getBytes());
                byte b[] = md.digest();
    
                int i;
    
                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
    
                re_md5 = buf.toString();
    
            } catch (Exception e) {
                e.printStackTrace();
            }
            return re_md5;
        }
    public static String httpsGet(String WebReq) {
        String Result = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(WebReq);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if(connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                Result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();// 关闭远程连接
        }
        return Result;
    }
    public static void main(String[] args) {
        String Salt = "1435660288" , OriReq , MD5Req = null , MD5ret , WebReq = null , ResPon = null;// 原始输入 MD5加密请求 百度翻译请求 百度翻译返回
        final String AppID = "20230519001683413" , WebSec = "XRc6hJz7J9mTHyR402qp";
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用「Sinetian的英译中翻译器」");
        System.out.print("请提供原文本：");
        OriReq = scanner.nextLine();
        System.out.println("请稍后……");
        MD5Req = AppID + OriReq + Salt + WebSec;
        MD5ret = encryption(MD5Req);
        WebReq = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=" + OriReq + "&from=en&to=zh&appid=" + AppID + "&salt=" + Salt + "&sign=" + MD5ret;
        ResPon = httpsGet(WebReq);
        JSONArray object = JSONObject.parseObject(ResPon).getJSONArray("trans_result");
        Iterator<Object> it = object.iterator();// 使用Iterator迭代器
        while (it.hasNext()) {
            JSONObject arrayObj = (JSONObject) it.next();// JSONArray中是很多个JSONObject对象
            String src = arrayObj.getString("src");
            String dst = arrayObj.getString("dst");
            System.out.println("原文本：" + src);
            System.out.println("结果：" + dst);
        }
        scanner.close();
    }
}