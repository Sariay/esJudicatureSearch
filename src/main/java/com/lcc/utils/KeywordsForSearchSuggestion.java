package com.lcc.utils;

import com.lcc.dao.DataDao;
import com.lcc.dao.DataDaoImpl;
import com.lcc.entity.MysqlConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName KeywordsForSearchSuggestion
 * @Description 对所有网页的标题进行关键词提取处理
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/5/16 11:46
 * @Version 1.0
 **/
public class KeywordsForSearchSuggestion extends TextTags{
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(KeywordsForSearchSuggestion.class);

        final String KEYWORDS_TXT_FILE_PATH = "data/keywords/keywords.txt";

        DataDao dataDao = new DataDaoImpl();

        String liveVidoeString = dataDao.getAllTextFromMysql(MysqlConfig.DATATABLE_LIVE_VIDOE_NAME.getValue());
        String judicialInterpretationString = dataDao.getAllTextFromMysql(MysqlConfig.DATATABLE_JUDICIAL_INTERPRETATION_NAME.getValue());
        String judicialCasesString = dataDao.getAllTextFromMysql(MysqlConfig.DATATABLE_JUDICIAL_CASES_NAME.getValue());
        String litigationGuideString = dataDao.getAllTextFromMysql(MysqlConfig.DATATABLE_LITIGATION_GUIDE_NAME.getValue());
        String allString = liveVidoeString + judicialCasesString +judicialInterpretationString +litigationGuideString;
        String tags = KeywordsForSearchSuggestion.getTags(allString, 30);
        logger.info(tags);

        Boolean fileIsCreate =  KeywordsForSearchSuggestion.insertKeywordsToFile(allString, 1000, KEYWORDS_TXT_FILE_PATH);
        logger.info(fileIsCreate);
    }
}
