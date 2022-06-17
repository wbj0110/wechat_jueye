package com.jueye.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jueye.entity.*;
import com.jueye.entity.qa.QA;
import com.jueye.service.material.WangyiMusicCrawler;
import com.jueye.solr.SolrClient;
import com.jueye.solr.impl.SolJSolrCloudClient;
import com.jueye.util.Constant;
import com.jueye.util.Util;
import com.jueye.util.WeChatUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信xml消息处理流程逻辑类
 *
 * @author soledede
 */
public class WechatProcess {

    private static String weixinUploadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";

//    static SolrClient solrClient = SolJSolrCloudClient.apply();
//    ImageSearchService imageSearchService = DefaultImageSearchServiceImpl.apply();


    String title = "";
    /**
     * 解析处理xml、获取智能回复结果（通过图灵机器人api接口）
     *
     * @param xml 接收到的微信数据
     * @return 最终的解析结果（xml格式数据）
     */
//    String menu = "我是棒棒哒sole秘,我会美美哒陪你文字，语音聊天，无所不知,七夕到了,请输入你的姓名";
    String menu = "欢迎大家,往期文章，请大家查看历史记录哈，大家可以和我语音，文字聊天，我们棒棒哒";

    List<String> givenList = Arrays.asList(
            "你是我一生都不想修复的 Bug,你的出现成为了我优先级最高的中断。",
            "遇见你生命才在增加,否则只是在虚耗",
            "我爱你,如果要给这份爱加上一个期限,我希望是一万年",
            "如果爱你是一种错误的话,那我情愿错上加错,哪怕错一辈子",
            "桃之2113夭夭，灼灼其华，之子于归，宜室宜家"
    );







    public String processWechatMag(String xml) {

        List<ArticleResult> articles = getArticleResults();
        ArticleResult article;

        Random rand = new Random();

        /** 解析xml数据 */
        ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);
        RsponseTypeXmlEntity rtyp = RsponseTypeXmlEntity.TEXT;
        /** 以文本消息为例，调用图灵机器人api接口，获取回复内容 */
        String result = "";
        MusicEntity music = null;
        List<String> recommendResultList = null;

        if ("text".endsWith(xmlEntity.getMsgType()) || "voice".endsWith(xmlEntity.getMsgType())) {

            String content = "";
            if ("text".endsWith(xmlEntity.getMsgType())) {
                content = xmlEntity.getContent();
            } else if ("voice".endsWith(xmlEntity.getMsgType())) {
                content = xmlEntity.getRecognition();
            }
            if (content.equalsIgnoreCase("?") || content.equalsIgnoreCase("？") || content.equalsIgnoreCase("") || content.equalsIgnoreCase(" ") || content.equalsIgnoreCase("help") || content.equalsIgnoreCase("菜单") || content.equalsIgnoreCase("menu")) {
                // result = getMenu();
                result = TulingApiProcess.getInstance().getTulingResult("menu_tuling");
            } else if (content.trim().contains("like") || content.trim().contains("歌手") || content.trim().contains("歌曲") || content.trim().contains("音乐") || content.trim().toLowerCase().contains("music") || content.trim().toLowerCase().contains("点首") || content.trim().toLowerCase().contains("要点") || content.trim().toLowerCase().contains("要听") || content.trim().toLowerCase().contains("想听")) {//search by music
                try {
                    music = WangyiMusicCrawler.search(content);
                    if (music != null) {
                        Double popularity = music.getPopularity();
                        if (popularity < 83) {
                            result = TulingApiProcess.getInstance().getTulingResult(content);
                        } else {
                            rtyp = RsponseTypeXmlEntity.MUSIC;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = TulingApiProcess.getInstance().getTulingResult(content);
                }
            } else {
                //search from solr
                try {
                    articles = null;
                    if (articles == null) {
                        //serarch from tuling
                        result = TulingApiProcess.getInstance().getTulingResult(content);
                    } else {
                        rtyp = RsponseTypeXmlEntity.ARTICLE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = TulingApiProcess.getInstance().getTulingResult(content);
                }
            }
            //入库问答
            new QA(content, result,xmlEntity.getFromUserName()).persistToDB();
            if(result.contains("列车信息")){

                try {
                    JSONObject orderInfo = JSON.parseObject(result);
                    if(orderInfo.containsKey("url")){
                      String url =  orderInfo.getString("url");

                        Pattern pStart  = Pattern.compile("startStation=(.*)&endStation=(.*)&searchType=(.*)&date=(.*)&sort=");
                        Matcher m = pStart.matcher(url);

                        String startAddr = "";
                        String endAddr = "";
                        String date = "";
                        if(m.find()){
                            startAddr =  m.group(1);
                            endAddr  =  m.group(2);
                            date = m.group(4);
                        }

                        articles = new ArrayList<ArticleResult>();
                         article = new ArticleResult();
                        article.setUrl("http://touch.train.qunar.com/trainList.html?startStation="+startAddr+"&endStation="+endAddr+"&date="+date);
                        article.setTitle(URLDecoder.decode(startAddr,"UTF-8")+"->"+URLDecoder.decode(endAddr,"UTF-8"));
                        article.setPicUrl("http://m.qpic.cn/psb?/V10sqCyZ2U1C59/n.s2gbI8DPQsPGjC1zDlqMVlN.iSTqsduTd7QhuzEhU!/b/dPIAAAAAAAAA&bo=hAP7AQAAAAARB00!&rf=viewer_4");
                        article.setAuthor("soledede");
                        article.setDescription("详细点击图文");
                        articles.add(article);
                        rtyp = RsponseTypeXmlEntity.ARTICLE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        } else if ("image".endsWith(xmlEntity.getMsgType())) {
            String picUrl = xmlEntity.getPicUrl();
            //save image to local disk
            WeChatUtil.saveImage(picUrl, Constant.imageSaveDir());
            recommendResultList = null;
            rtyp = RsponseTypeXmlEntity.IMAGE;


        } else if ("event".endsWith(xmlEntity.getMsgType())) {
            String event = xmlEntity.getEvent();
            if ("subscribe".equalsIgnoreCase(event)) {
                result = TulingApiProcess.getInstance().getTulingResult("subscribe");
            }
        }

        /** 此时，如果用户输入的是“你好”，在经过上面的过程之后，result为“我很好”类似的内容
         *  因为最终回复给微信的也是xml格式的数据，所有需要将其封装为文本类型返回消息
         * */
        if (rtyp == RsponseTypeXmlEntity.TEXT)
            result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);
        else if (rtyp == RsponseTypeXmlEntity.MUSIC) {
            if (music != null)
                result = new FormatXmlProcess().formatXmlMusic(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), music.getName(), music.getSinger(), music.getUrl(), music.getUrl(), null);
            else
                result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);
        } else if (rtyp == RsponseTypeXmlEntity.ARTICLE) {
            if (articles != null)
                result = new FormatXmlProcess().formatXmlArticles(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), articles);
            else
                result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);
        } else if (rtyp == RsponseTypeXmlEntity.IMAGE) {


            if (recommendResultList != null && recommendResultList.size() > 0) {
                List<ArticleResult> articleResultList = new ArrayList<ArticleResult>();
                for (String url : recommendResultList) {
                    ArticleResult articleResult = new ArticleResult();
                    articleResult.setPicUrl(url);
                    articleResult.setUrl(url);
                    articleResult.setTitle("以图搜图");
                    articleResult.setDescription("");
                    articleResultList.add(articleResult);
                }
                if (articleResultList.size() > 0)
                    result = new FormatXmlProcess().formatXmlArticles(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), articleResultList);
            }
        }
        return result;
    }

    private List<ArticleResult> getArticleResults() {
        ArticleResult article = null;
        List<ArticleResult> articles = new ArrayList<ArticleResult>();
        article= new ArticleResult();
        article.setUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5q*CDuQTLBwU5ztihXmFTTlDwuPWc68od8BVWb48s2InFtSAtbqJKMU.BivhvIbkMi.ht5B57r6Yfx0zYZrSpAU!/b&bo=1ATIAgAAAAADBzg!&rf=viewer_4");
        article.setTitle("遇见你生命才在增加");
        article.setPicUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5pM1HlVzqfPBcSA2HY4VJNJWhh9qjPiFIc**zFub.nXZt7P145GlM3YvWeKrCqy5Eau1Cz3e4mpgHZZSUf8WRVM!/b&bo=OAS3BAAAAAADB60!&rf=viewer_4");
        article.setAuthor("soledede");
        article.setDescription("详细点击图文");
        articles.add(article);

        article = new ArticleResult();
        article.setUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5pM1HlVzqfPBcSA2HY4VJNI234lnGkWnIwKCEV8097.OyBsK9KdUEGgvP2a3LvcLjs5TK3E7pbtOR9g0ThSXPo8!/b&bo=uASiAQAAAAADBz0!&rf=viewer_4");
        article.setTitle("直到遇见你");
        article.setPicUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5pM1HlVzqfPBcSA2HY4VJNKsl1vqKVBkAKg3l1VRnSPlOjvPcg4OUo6dmW2hm.hR8*KMCFQKkzFtqaq5wTzlXWg!/b&bo=OATlBAAAAAADB*8!&rf=viewer_4");
        article.setAuthor("soledede");
        article.setDescription("详细点击图文");
        articles.add(article);

        article = new ArticleResult();
        article.setUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5oM3g*cUcV9J*SKKmeOK0rWRQwi9KrBDEWFXqnqaeqyHvdriV*MvQCrK8RRKwqkxmSZJIqfWsCSNPLnFXKZ.Cdk!/b&bo=wASUAQAAAAADB3M!&rf=viewer_4");
        article.setTitle("心跳");
        article.setPicUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5pM1HlVzqfPBcSA2HY4VJNKgbkRb07agtqZ*B..m.h.N6ONLRrNo1n1H*5rBl8dMaH14*NSA8cyZmVba.NTcv8w!/b&bo=kgQ4BAAAAAADB4g!&rf=viewer_4");
        article.setAuthor("soledede");
        article.setDescription("详细点击图文");
        articles.add(article);

        article = new ArticleResult();
        article.setUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/45NBuzDIW489QBoVep5mcXCJ8RXcg.FBJTc48qMo2LF0W3YmAIvOqCobmePLWJGbz7vjwDaHuKG5m8YUITDmTGGvLNy8X3AziqfOoV3JDZ8!/b&bo=xgTiAgAAAAADJyA!&rf=viewer_4");
        article.setTitle("爱你一万年");
        article.setPicUrl("http://m.qpic.cn/psc?/V54bgpUq1alLW70qKdPf2EJMfc1DFbpP/ruAMsa53pVQWN7FLK88i5oM3g*cUcV9J*SKKmeOK0rX39HkF6zGCAZi2tlsE44iIhJcV.F8M*9tmI6fpWZDsO298g.NJZpVGFbKn*DZPqP4!/b&bo=hANqBAAAAAADB8s!&rf=viewer_4");
        article.setAuthor("soledede");
        article.setDescription("详细点击图文");
        articles.add(article);
        return articles;
    }





    public String getMenu() {
        StringBuffer sb = new StringBuffer();
        sb.append(menu).append("\n\n");
        sb.append("七夕快乐,请输入你的姓名").append("\n\n");
        sb.append("歌曲点播操作指南").append("\n\n");
        sb.append("语音或文字输入：歌曲").append("\n");
        sb.append("例如：左右手").append("\n\n");
        sb.append("语音或文字输入：歌曲 歌手").append("\n");
        sb.append("例如：梦一场 那英").append("\n\n");
        sb.append("回复“?”显示主菜单");
        return sb.toString();
    }





}
