package com.lcc.utils;

import com.hankcs.hanlp.HanLP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName TextTags
 * @Description 关键词提取
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/4/2 12:27
 * @Version 1.0
 **/
public class TextTags {

    /**
     * @MethodName getTags
     * @Description 提取关键词， 并将其转换为html文本
     * @Param [textContent, wordsLimit]
     * @Return java.lang.String
     **/
    public static String getTags(String textContent, int wordsLimit){
        Logger logger = LogManager.getLogger( TextTags.class );

        String tags = new String();

        List<String> keywordList = HanLP.extractKeyword(textContent, wordsLimit);

        for (int i=0; i<wordsLimit; i++){
            if( keywordList.get(i).length() >= 2){
                tags += "<span>" + keywordList.get(i) + "</span>";
            }
        }

        logger.info( tags );

        return tags;
    }

    /**
     * @MethodName insertKeywordsToFile
     * @Description 提取关键词， 并将其写入txt文件
     * @Param [textContent, wordsLimit, filePath]
     * @Return boolean
     **/
    public static boolean insertKeywordsToFile(String textContent, int wordsLimit, String filePath){
        Logger logger = LogManager.getLogger( TextTags.class );

        String currentKeyword = new String();

        boolean fileIsExist = false;

        List<String> keywordList = HanLP.extractKeyword(textContent, wordsLimit);

        File file = new File(filePath);

        try {
            if (!file.exists()) {
                boolean fileIsCreate = file.createNewFile();
            }

            FileOutputStream keywordOut = new FileOutputStream(file, false);
            StringBuffer stringBuffer = new StringBuffer();

            for (int i=0; i<wordsLimit; i++){
                currentKeyword = keywordList.get(i);
                if( currentKeyword.length() >= 2){
                    stringBuffer.append(currentKeyword + "\r\n");
                }
            }

            keywordOut.write(stringBuffer.toString().getBytes("utf-8"));//注意需要转换对应的字符集
            keywordOut.flush();
            keywordOut.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        fileIsExist = file.exists();

        logger.info( "文件路径" + filePath);
        logger.info( fileIsExist );
        return  fileIsExist;
    }
}