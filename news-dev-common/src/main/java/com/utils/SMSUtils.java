package com.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.utils.extend.AliyunResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by TongHaiJun
 * 2021/1/16 9:00
 */
@Component
public class SMSUtils {

    private final AliyunResource aliyunResource;

    @Autowired
    public SMSUtils(AliyunResource aliyunResource) {
        this.aliyunResource = aliyunResource;
    }

    public void sendSMS(String mobile, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai",
                aliyunResource.getAccessKeyID(),
                aliyunResource.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-shanghai");

        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "野猪佩奇666");
        request.putQueryParameter("TemplateCode", "SMS_209835919");
        String codeJson = "{\"code\":\"" + code + "\"}";
        request.putQueryParameter("TemplateParam", codeJson);

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
