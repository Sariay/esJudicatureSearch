package com.lcc.utils;

import com.lcc.dao.DataDao;
import com.lcc.dao.DataDaoImpl;
import com.lcc.entity.InformationTemplate;
import com.lcc.entity.MysqlConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName WebmagicHelper
 * @Description 爬虫实验，具体网站爬虫见于crawler包
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/4/1 20:44
 * @Version 1.0
 **/
public class WebmagicHelper implements PageProcessor {
    Logger logger = LogManager.getLogger(WebmagicHelper.class);

    // 链接去重，防止抓取相同的网页
    Set<String> allUrls  = new HashSet<>();

    //抓取网站相关的配置，包括：编码、重试次数、抓取间隔
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    /**
     * @MethodName getPageContent
     * @Description 网页页面信息抽取
     * @Param [page]
     * @Return com.lcc.entity.InformationTemplate
     **/
    public InformationTemplate getPageContent(Page page){
        InformationTemplate pageContent = new InformationTemplate();

        String webpageUrl = page.getUrl().toString();
        String webpageTitle =  page.getHtml().xpath("//title/text()").toString();
        String webpageContent =  page.getHtml().xpath("//body/allText()").toString();

        pageContent.setInfoUrl(
                webpageUrl
        );
        pageContent.setInfoTitle(
                webpageTitle
        );
        pageContent.setInfoContent(
                webpageContent
        );
        return pageContent;
    }

    /**
     * @MethodName getPageRelevancy
     * @Description 主题相关度计算：计算“网页内容“与“司法信息“的主题相关度
     * @Param [pageContent]
     * @Return float
     **/
    public float getPageRelevancy(InformationTemplate pageContent){
        final String DICTIONARY_PATH = "data/baidu_dictionary/law2.txt";
        List<String> keywords = new ArrayList<String>(); //关键词列表
        String currentStr;
        int currentKeywordweight = 1;//当前关键字权重
        float currentTagWeight = 0;//当前标签权重
        float pageRelevancy = 0;//总相关度
        int size = 0;

        BufferedReader dictionaryFile = null;

        try{
            dictionaryFile = new BufferedReader(new FileReader(DICTIONARY_PATH));
            String singleLine = dictionaryFile.readLine();
            while (singleLine != null){
                keywords.add( singleLine );
                singleLine = dictionaryFile.readLine();
            }

            if (keywords.size() > 0){
                if (pageContent.getInfoTitle() != null){
                    currentTagWeight = 5;
                    currentStr = pageContent.getInfoTitle();
                    size = MultiPatterMatchAc.resultSize(keywords, currentStr);
                    pageRelevancy += size*currentTagWeight;
                }
                if (pageContent.getInfoContent() !=null){
                    currentTagWeight = 3;
                    currentStr =pageContent.getInfoContent();
                    size = MultiPatterMatchAc.resultSize(keywords, currentStr);
                    pageRelevancy += size*currentTagWeight;
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return pageRelevancy;
    }

    public void process(Page page) {

        // 获取当前页的所有url
        page.addTargetRequests(
                page.getHtml().xpath("//body").links().all()
        );

        // 过滤当前页的url
        boolean urlIsMatch = page.getUrl()
                .regex("gov\\.cn")
                .match();

        if ( urlIsMatch ) {
            InformationTemplate pageContent = getPageContent(page);
            String webpageAllText = pageContent.getInfoTitle() + pageContent.getInfoContent();
            String webpageTags = TextTags.getTags(webpageAllText, 3);
            pageContent.setInfoTags( webpageTags );

            // 计算当前页的主题相关度
            float currentPageRelevancy = getPageRelevancy(pageContent);
            
            // 主题相关度>0, 且链接未访问过
            if (!allUrls.contains( pageContent.getInfoUrl()) && currentPageRelevancy > 0 ){
                allUrls.add( pageContent.getInfoUrl());

                //输出到控制台
                logger.info(pageContent);

                //将实例对象的数据持久化到数据库
                DataDao dataDao = new DataDaoImpl();
                dataDao.insertDataToMysql(pageContent, MysqlConfig.DATATABLE_LIVE_VIDOE_NAME.getValue());
            } else {
                page.setSkip(true);
            }
        } else {
            page.setSkip(true);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        List<String> urlFeeds = new ArrayList<String>();
        urlFeeds.add("https://splcgk.court.gov.cn/gzfwww/");
        urlFeeds.add("http://zxgk.court.gov.cn/");
        urlFeeds.add("http://tingshen.court.gov.cn/");
        
        Spider.create(new WebmagicHelper())
                .startUrls( urlFeeds )
                .addPipeline(new ConsolePipeline())
                .thread(5)
                .run();
    }
}