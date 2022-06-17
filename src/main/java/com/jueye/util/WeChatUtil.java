package com.jueye.util;

import com.jueye.es.controller.Utils;
import com.jueye.http.HttpClientUtil;
import com.jueye.token.UpdateToken;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by soledede on 16/3/28.
 */
public class WeChatUtil {


    public static String  checkSignature(String timestamp,String nonce){

        // 重写totring方法，得到三个参数的拼接字符串
        List<String> list = new ArrayList<String>(3) {
            private static final long serialVersionUID = 2621444383666420433L;

            public String toString() {
                return this.get(0) + this.get(1) + this.get(2);
            }
        };
        list.add(Utils.TOKEN);
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);// 排序
        String newSignature = new MySecurity().encode(list.toString(),
                MySecurity.SHA_1);// SHA-1加密
        return newSignature;
    }


    public static void getImageMediaId(String recommendImageUrl,String weixinUploadUrl) {
        File file = null;
        try {
            file = new File(recommendImageUrl.substring(recommendImageUrl.lastIndexOf("/")+1));

            try {
                FileUtils.copyURLToFile(new URL(recommendImageUrl), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map parametersMap = new java.util.HashMap<String, Object>();

        parametersMap.put("access_token", UpdateToken.accessToken());
        parametersMap.put("type", "image");

        CloseableHttpResponse httpResp = HttpClientUtil.uploadFile("media", file, weixinUploadUrl, file.getName(), parametersMap);
        try {
            String sResponse = EntityUtils.toString(httpResp.getEntity());
            System.out.println(sResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpResp);
        }
    }

    public static void saveImage(String url,String dir){
        File file = null;
        try {
            file = new File(dir+url.substring(url.lastIndexOf("/")+1)+ UUID.randomUUID()+".jpg");

            try {
                FileUtils.copyURLToFile(new URL(url), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
