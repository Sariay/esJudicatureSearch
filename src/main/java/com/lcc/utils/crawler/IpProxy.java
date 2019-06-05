package com.lcc.utils.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName IpProxy
 * @Description ip代理生成器，绕过反爬虫
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/5/11 20:16
 * @Version 1.0
 **/
public class IpProxy {
   public static List<Proxy> buildProxyIp() throws IOException {
        Document parse = Jsoup.parse(new URL("http://www.89ip.cn/tqdl.html?api=1&num=30&port=&address=&isp="), 5000);
        String pattern = "(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+):(\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(parse.toString());
        List<Proxy> proxies = new ArrayList<Proxy>();
        while (m.find()) {
            String[] group = m.group().split(":");
            int prot = Integer.parseInt(group[1]);
            proxies.add(new Proxy(group[0], prot));
        }
        return proxies;
    }
}
