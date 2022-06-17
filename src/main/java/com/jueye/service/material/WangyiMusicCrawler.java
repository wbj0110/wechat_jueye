package com.jueye.service.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jueye.entity.MusicEntity;
import com.jueye.httpclient.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by soledede on 2015/10/19.
 */
public class WangyiMusicCrawler {
    static String url = "http://music.163.com/api/search/pc";

    public static MusicEntity search(String music){
        if(music==null || music.trim().equalsIgnoreCase("")) return null;
        RequestEntity entity = new RequestEntity();
        entity.setUrl(url);
        entity.setMethod(Http.POST);

        Map<String, String> header = new HashMap<String, String>();
        header.put("Cookie", "Cookie: os=pc; deviceId=B55AC773505E5606F9D355A1A15553CE78B89FC7D8CB8A157B84; osver=Microsoft-Windows-8-Professional-build-9200-64bit; appver=1.5.0.75771; usertrack=ezq0alR0yqJMJC0dr9tEAg==; MUSIC_A=088a57b553bd8cef58487f9d01ae");
        header.put("Referer", "http://music.163.com/");
        entity.setHeader(header);


        Map<String, String> p = new HashMap<String, String>();
        p.put("s", music);
        p.put("offset", "0");
        p.put("limit", "1");
        p.put("type", "1");
        entity.setPostParameters(p);
        HttpRequest request = new HttpClientRequest();
        Response result = request.download(entity, null, RequestType.SYNC);
        try {
            String resp = new String(result.getContent(),"utf8");
           // System.out.println(resp);
            if (resp != null) {
                JSONObject jObj = JSON.parseObject(resp);
                JSONObject res = jObj.getJSONObject("result");
                JSONArray resA = res.getJSONArray("songs");
                JSONObject musi = resA.getJSONObject(0);
                Double popularity = musi.getDouble("popularity");
                String mp3Url = musi.getString("mp3Url");
                String name = musi.getString("name");
                JSONArray artists = musi.getJSONArray("artists");
                String singer = artists.getJSONObject(0).getString("name");
                //System.out.println(mp3Url+"popularity:"+popularity);
                return new MusicEntity(name,popularity,mp3Url,singer);
            }else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
