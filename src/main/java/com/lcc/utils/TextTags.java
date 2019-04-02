package com.lcc.utils;

import com.hankcs.hanlp.HanLP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @ClassName TextTags
 * @Description TODO
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/4/2 12:27
 * @Version 1.0
 **/
public class TextTags {

    /**
     * @MethodName getTags
     * @Description 关键词提取
     * @Param []
     * @Return void
     **/
    public static String getTags(String textContent){
        Logger logger = LogManager.getLogger( TextTags.class );

        int keyWordSize = 3;

        String tags = new String();

        List<String> keywordList = HanLP.extractKeyword(textContent, keyWordSize);


        for (int i=0; i<keyWordSize; i++){
            if( keywordList.get(i).length() >= 2){
                tags += "<span>" + keywordList.get(i) + "</span>";
            }
        }

        logger.info( tags );

        return tags;
    }
}