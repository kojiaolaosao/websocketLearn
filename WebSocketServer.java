package com.example.Controller;

import cn.hutool.log.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import cn.hutool.log.Log;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: learnwebsokect
 * @description: WebSocketServer
 * @author: Mr.qi
 * @create: 2021-08-10 21:53
 **/
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    static Log log = LogFactory.get(WebSocketServer.class);
    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServer = new CopyOnWriteArraySet<WebSocketServer>();
    private Session session;
    private String sid = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketServer.add(this);
        addOnlineCount();
        log.info("有新的窗口开始监听：" + sid + ",当前在线的人数为 " + getOnlineCount());
        this.sid=sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }
    @OnClose
    public void onClose(){
        webSocketServer.remove(this);
        subOnlineCount();
        log.info("有一处连接关闭！当前在线人数为 "+getOnlineCount());
    }
    @OnMessage
    public void onMessage(String message ,Session session){
        log.info("收到来自窗口"+sid+"的消息"+message);
        for (WebSocketServer server : webSocketServer) {
            try {
                server.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @OnError
    public void  onError(Session session,Throwable error){
        log.error("发生错误");
        error.printStackTrace();
    }

    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketServer) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
}

