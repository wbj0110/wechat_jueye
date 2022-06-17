package com.jueye.process;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.jueye.util.Constant;

/**
 * 调用图灵机器人api接口，获取智能回复内容
 * @author soledede
 *
 */
public class TulingApiProcess {

	private volatile static TulingApiProcess singleton;

	public static TulingApiProcess getInstance() {
		if (singleton == null) {
			synchronized (TulingApiProcess.class) {
				if (singleton == null) {
					singleton = new TulingApiProcess();
				}
			}
		}
		return singleton;
	}
	/**
	 * 调用图灵机器人api接口，获取智能回复内容，解析获取自己所需结果
	 * @param content
	 * @return
	 */
	public String getTulingResult(String content){
		/** 此处为图灵api接口，参数key需要自己去注册申请 */
		String apiUrl = Constant.tulingApi();
		String param = "";
		System.out.println(content+"输入内容!!!!!!!!!!!!!!!!!!!!!");
		try {
			param = apiUrl+URLEncoder.encode(content,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //将参数转为url编码
		
		/** 发送httpget请求 */
		HttpGet request = new HttpGet(param);
		String result = "";
		try {
			HttpResponse response = HttpClients.createDefault().execute(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
				System.out.println("_____________"+result+"##########");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** 请求失败处理 */
		if(null==result){
			return "对不起，你说的话真是太高深了……";
		}
		
		try {
			JSONObject json = new JSONObject(result);
			//以code=100000为例，参考图灵机器人api文档
			if(100000==json.getInt("code")){
				result = json.getString("text");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

    public static void main(String[] args) {
        String content = "你好";
        String result = TulingApiProcess.getInstance().getTulingResult(content);
        System.out.println(result);
    }

}
