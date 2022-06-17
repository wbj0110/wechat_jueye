package com.jueye.process;

import com.jueye.entity.ArticleResult;

import java.util.Date;
import java.util.List;

/**
 * 封装最终的xml格式结果
 *
 * @author soledede
 */
public class FormatXmlProcess {
    /**
     * 封装文字类的返回消息
     *
     * @param to
     * @param from
     * @param content
     * @return
     */
    public String formatXmlAnswer(String to, String from, String content) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(to);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(from);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");
        sb.append(content);
        sb.append("]]></Content></xml>");
        //<FuncFlag>0</FuncFlag>
        return sb.toString();
    }


    /**
     *
     *
     *
     <xml>
     <ToUserName><![CDATA[toUser]]></ToUserName>
     <FromUserName><![CDATA[fromUser]]></FromUserName>
     <CreateTime>12345678</CreateTime>
     <MsgType><![CDATA[music]]></MsgType>
     <Music>
     <Title><![CDATA[TITLE]]></Title>
     <Description><![CDATA[DESCRIPTION]]></Description>
     <MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
     <HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
     <ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
     </Music>
     </xml>
     */


    /**
     StringBuffer sb = new StringBuffer();
     Date date = new Date();
     sb.append("<xml><ToUserName><![CDATA[");
     sb.append(to);
     sb.append("]]></ToUserName><FromUserName><![CDATA[");
     sb.append(from);
     sb.append("]]></FromUserName><CreateTime>");
     sb.append(date.getTime());
     sb.append("</CreateTime><MsgType><![CDATA[music]]></MsgType><Music><Title><![CDATA[");
     sb.append(title);
     sb.append("]]></Title><Description>");
     sb.append(description);
     sb.append("</Description><MusicUrl><![CDATA[" );
     sb.append(musicUrl);
     sb.append("]]></MusicUrl><HQMusicUrl><![CDATA[");
     sb.append(hQMusicUrl);
     sb.append("]]></HQMusicUrl><ThumbMediaId><![CDATA[");
     sb.append(mediaId);
     sb.append("]]></ThumbMediaId></Music></xml>");
     return sb.toString();
     */

    /**
     * StringBuffer sb = new StringBuffer();
     Date date = new Date();
     sb.append("<xml><ToUserName><![CDATA[");
     sb.append(to);
     sb.append("]]></ToUserName><FromUserName><![CDATA[");
     sb.append(from);
     sb.append("]]></FromUserName><CreateTime>");
     sb.append(date.getTime());
     sb.append("</CreateTime><MsgType><![CDATA[music]]></MsgType><Music><Title><![CDATA[");
     sb.append(title);
     sb.append("]]></Title><MusicUrl><![CDATA[");
     sb.append(musicUrl);
     sb.append("]]></MusicUrl></Music></xml>");
     return sb.toString();
     */
    /**
     * @param to
     * @param from
     * @param title
     * @param description
     * @param musicUrl
     * @param hQMusicUrl
     * @param mediaId
     * @return
     */
    public String formatXmlMusic(String to, String from, String title, String description, String musicUrl, String hQMusicUrl, String mediaId) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(to);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(from);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[music]]></MsgType><Music><Title><![CDATA[");
        sb.append(title);
        sb.append("]]></Title><Description>");
        sb.append(description);
        sb.append("</Description><MusicUrl><![CDATA[");
        sb.append(musicUrl);
        sb.append("]]></MusicUrl><HQMusicUrl><![CDATA[");
        sb.append(hQMusicUrl);
        sb.append("]]></HQMusicUrl></Music></xml>");
        return sb.toString();
    }

    public String formatXmlArticles(String to, String from, List<ArticleResult> articles) {
        int size = articles.size();
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(to);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(from);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[news]]></MsgType><ArticleCount>");
        sb.append(size);
        sb.append("</ArticleCount><Articles>");
        for (ArticleResult article : articles) {
            sb.append("<item>");
            sb.append("<Title><![CDATA[");
            sb.append(article.getTitle());
            sb.append("]]></Title><Description><![CDATA[");
            sb.append(article.getDescription());
            sb.append("]]></Description><PicUrl><![CDATA[");
            sb.append(article.getPicUrl());
            sb.append("]]></PicUrl><Url><![CDATA[");
            sb.append(article.getUrl());
            sb.append("]]></Url></item>");
        }

        sb.append("</Articles></xml>");
        return sb.toString();
    }

    public String formatXmlImages(String to, String from, String image, String mediaId) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(to);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(from);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[image]]></MsgType>");
        sb.append("<Image>");
        sb.append("<MediaId><![CDATA[");
        sb.append(mediaId);
        sb.append("]]></MediaId>");
        sb.append("</Image></xml>");
        return sb.toString();
    }

}
