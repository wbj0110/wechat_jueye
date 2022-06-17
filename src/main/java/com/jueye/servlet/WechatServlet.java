package com.jueye.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jueye.process.WechatProcess;
import com.jueye.util.WeChatUtil;

/**
 * 微信服务端收发消息接口
 *
 * @author soledede
 */
public class WechatServlet extends HttpServlet {


    /**
     * The doGet method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        /** 读取接收到的xml消息 */
        StringBuffer sb = new StringBuffer();
        InputStream is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        String xml = sb.toString();    //次即为接收到微信端发送过来的xml数据

        String result = "";
        /** 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回 */
        // 随机字符串
        String echostr = request.getParameter("echostr");
        if (echostr != null && echostr.length() > 1) {
            String signature = request.getParameter("signature");// 微信加密签名
            String timestamp = request.getParameter("timestamp");// 时间戳
            String nonce = request.getParameter("nonce");// 随机数


            String checkSignature = WeChatUtil.checkSignature(timestamp,nonce);

//            Writer out = response.getWriter();
            if (signature.equals(checkSignature)) {
//                out.write(echostr);// 请求验证成功，返回随机码
                result = echostr;

            } else {
//                out.write("");
                result = "";
            }
//            out.flush();
//            out.close();

        } else {
            //正常的微信处理流程
            //xml = xml.replaceAll("]></Content>", "]]></Content>");
            System.out.println("请求#############" + xml);
            if (!xml.equals(""))
                result = new WechatProcess().processWechatMag(xml);
        }

        try {
            OutputStream os = response.getOutputStream();
            System.out.println("Test++++++++" + result);
            os.write(result.getBytes("UTF-8"));
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The doPost method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
