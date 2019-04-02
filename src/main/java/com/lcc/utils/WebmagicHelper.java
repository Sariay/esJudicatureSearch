package com.lcc.utils;

import com.lcc.dao.DataDao;
import com.lcc.dao.DataDaoImpl;
import com.lcc.entity.InformationTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @ClassName WebmagicHelper
 * @Description 爬虫模块
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/4/1 20:44
 * @Version 1.0
 **/
public class WebmagicHelper implements PageProcessor {
    Logger logger = LogManager.getLogger(WebmagicHelper.class);

    //抓取网站相关的配置，包括：编码、重试次数、抓取间隔
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public void process(Page page) {

        page.addTargetRequests(
                page.getHtml()
                        .xpath("//main[@class='main']")
                        .links()
                        .all()
        );

        boolean urlIsTrue = page.getUrl()
                .regex("https://blog\\.fliaping\\.com/")
                .match();

        if (urlIsTrue) {
            //-使用InformationTemplate类存取抓取的数据，方便存入数据库
            //-分别设置实例对象的url，title，content
            InformationTemplate informationTemplate = new InformationTemplate();

            String webpageUrl = page.getUrl().toString();
            String webpageTitle =  page.getHtml().xpath("//title/text()").toString();
            String webpageContent =  page.getHtml().xpath("//main/allText()").toString();
            String webpageAllText = webpageTitle + webpageContent;

            informationTemplate.setInfoUrl(
                    webpageUrl
            );
            informationTemplate.setInfoTitle(
                    webpageTitle
            );
            informationTemplate.setInfoContent(
                    webpageContent
            );

            //设置tags
            String webpageTags = TextTags.getTags(webpageAllText);
            informationTemplate.setInfoTags( webpageTags );

            //将对象输出到控制台
            logger.info(informationTemplate);

            //将实例对象的数据持久化到数据库
            DataDao dataDao = new DataDaoImpl();
            dataDao.insertDataToMysql(informationTemplate);

        } else {
            page.setSkip(true);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new WebmagicHelper())
                .addUrl("https://blog.fliaping.com/")
                .addPipeline(new ConsolePipeline())
                .thread(5)
                .run();
    }
}