package com.lcc.utils.crawler;

import com.lcc.dao.DataDao;
import com.lcc.dao.DataDaoImpl;
import com.lcc.entity.InformationTemplate;
import com.lcc.entity.MysqlConfig;
import com.lcc.utils.TextTags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName LiveVideoHelper
 * @Description 中国庭审公开网，视频爬虫
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/5/11 17:26
 * @Version 1.0
 **/
public class LiveVideoHelper implements PageProcessor {
    private Logger logger = LogManager.getLogger(LiveVideoHelper.class);

    private int i=0;


    // 链接去重，防止抓取相同的网页
    private Set<String> allUrls = new HashSet<>();

    //private final String SINGLE_URL = "https://splcgk\\.court\\.gov\\.cn/gzfwww//sfjs/.*";
    private final String SINGLE_URL = "http://tingshen\\.court\\.gov\\.cn/live/\\w*";

    //抓取网站相关的配置，包括：编码、重试次数、抓取间隔
    private Site site = Site.me()
            .setRetryTimes(10)
            .setSleepTime(4000);

    public InformationTemplate getPageContent(Page page){
        InformationTemplate pageContent = new InformationTemplate();

        String url = page.getUrl().toString();
        logger.info(url);


        String title =  page.getHtml().xpath("//body//div[@class='live-video']//span[@class='video-title']/text()").toString();
        logger.info(title);

        String content =  page.getHtml().xpath("//body//div[@class='part case-summary']/allText()").toString() +
                page.getHtml().xpath("//body//div[@class='part trial-info']/allText()").toString();
        logger.info(content);

        String category = "直播视频";

        String date = page.getHtml().xpath("//body//ul[@class='clearfix']/li[2]/text()").toString();
        logger.info(date);

        pageContent.setInfoUrl(
                url
        );
        pageContent.setInfoTitle(
                title
        );
        pageContent.setInfoContent(
                content
        );

        pageContent.setInfoCategory(
                category
        );

        pageContent.setInfoDate(
                date
        );

        return pageContent;
    }

    public void process(Page page){
        i++;
        System.out.println("第" + i + "条******************************************************************");

        if (page.getUrl().regex( SINGLE_URL ).match()){
            InformationTemplate pageContent = getPageContent(page);

            String webpageAllText = pageContent.getInfoContent();
            String webpageTags = TextTags.getTags(webpageAllText, 3);
            pageContent.setInfoTags( webpageTags );
            logger.info( webpageTags );

            // url未访问过
            if (!allUrls.contains( pageContent.getInfoUrl())) {
                allUrls.add(pageContent.getInfoUrl());

                DataDao dataDao = new DataDaoImpl();
                dataDao.insertDataToMysql(pageContent, MysqlConfig.DATATABLE_LIVE_VIDOE_NAME.getValue());
                System.out.println("輸出" + pageContent.getInfoUrl() + " : " + pageContent.getInfoTitle());
            }else {
                page.setSkip(true);
            }
        }else {
            page.setSkip(true);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        DataDao dataDao = new DataDaoImpl();
        List<String> startUrls = dataDao.getUrlsFromMysql( MysqlConfig.LIVE_VIDOE_URL.getValue());
        System.out.println( startUrls );

        Spider.create(new LiveVideoHelper())
                .startUrls( startUrls )
                .addPipeline(new ConsolePipeline())
                .thread(1)
                .run();
    }
}
