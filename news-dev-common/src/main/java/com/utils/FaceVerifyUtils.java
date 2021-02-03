package com.utils;

import com.aliyuncs.utils.Base64Helper;
import com.enums.FaceVerifyType;
import com.enums.ResponseStatusEnum;
import com.exception.NewsException;
import com.utils.extend.AliyunResource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

/**
 * Created by TongHaiJun
 * 2021/1/31 18:04
 */
@Component
public class FaceVerifyUtils {

    final static Logger logger = LoggerFactory.getLogger(FaceVerifyUtils.class);

    private final AliyunResource aliyunResource;

    private static final String gateway = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";

    public FaceVerifyUtils(AliyunResource aliyunResource) {
        this.aliyunResource = aliyunResource;
    }

    /**
     * @param type             0: 通过url识别，参数image_url不为空；1: 通过图片content识别，参数content不为空
     * @param face1            type为0，则传入图片url，为1则传入base64
     * @param face2            type为0，则传入图片url，为1则传入base64
     * @param targetConfidence 目标可信度，自定义阈值
     * @return 应答
     */
    public boolean faceVerify(int type, String face1, String face2, double targetConfidence) {

        String response = null;
        try {
            response = sendPostVerifyFace(type, face1, face2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> map = JsonUtils.jsonToPojo(response, Map.class);
        Object confidenceStr = map.get("confidence");
        Double responseConfidence = (Double) confidenceStr;

        logger.info("人脸对比结果：{}", responseConfidence);

        return responseConfidence > targetConfidence;
    }

    /*
     * 计算MD5+BASE64
     */
    private static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            encodeStr = Base64Helper.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */

    private static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64Helper.encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */

    private static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /**
     * 发送POST请求 进行两张图的人脸对比
     *
     * @param type  0: 通过url识别，参数image_url不为空；1: 通过图片content识别，参数content不为空
     * @param face1 type为0，则传入图片url，为1则传入base64
     * @param face2 type为0，则传入图片url，为1则传入base64
     * @return 应答
     */
    //如果发送的是转换为base64编码后后面加请求参数type为1，如果请求的是图片的url则不用加type参数。
    private String sendPostVerifyFace(int type, String face1, String face2) throws Exception {
        String body = "";
        if (type == FaceVerifyType.BASE64.type) {
            body = "{\"content_1\": \"" + face1 + "\", \"content_2\":\"" + face2 + "\", \"type\":\"" + type + "\"}";
        } else if (type == FaceVerifyType.IMAGE_URL.type) {
            body = "{\"image_url_1\": \"" + face1 + "\", \"image_url_2\":\"" + face2 + "\", \"type\":\"" + type + "\"}";
        } else {
            NewsException.display(ResponseStatusEnum.FACE_VERIFY_TYPE_ERROR);
        }
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        int statusCode = 200;
        try {
            URL realUrl = new URL(gateway);
            /*
             * http header 参数
             */
            String method = "POST";
            // 返回值类型
            String accept = "application/json";
            // 请求内容类型
            String content_type = "application/json";
            String path = realUrl.getFile();
            // GMT时间
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, aliyunResource.getAccessKeySecret());
            // 3.得到 authorization header
            String authHeader = "Dataplus " + aliyunResource.getAccessKeyId() + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Accept", accept);
            conn.setRequestProperty("Content-type", content_type);
            conn.setRequestProperty("Date", date);
            // 认证信息
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection) conn).getResponseCode();
            if (statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: " + statusCode + "\nErrorMessage: " + result);
        }
        return result.toString();
    }

    /**
     * 将图片转换为Base64
     * 将base64编码字符串解码成img图片
     *
     * @param imgUrl 图片地址
     * @return 图片base64
     */
    public String getImgBase64(String imgUrl) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgUrl);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容放到内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.encodeBase64String(data.toByteArray());
    }

}


