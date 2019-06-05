package com.lcc.dao;

import com.lcc.entity.IndexTemplate;
import com.lcc.entity.InformationTemplate;
import com.lcc.entity.MysqlConfig;
import com.lcc.utils.ElasticsearchHelper;
import com.lcc.utils.MysqlHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DataDaoImpl
 * @Description TODO
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 19:19
 * @Version 1.0
 **/
public class DataDaoImpl implements DataDao {
    Logger logger = LogManager.getLogger( DataDaoImpl.class );

    public void insertDataToMysql(InformationTemplate informationTemplate, String dataTable) {
        MysqlHelper mysqlHelper = new MysqlHelper();
        StringBuffer sql = new StringBuffer();

        sql.append("INSERT INTO " + MysqlConfig.DATABASE_NAME.getValue() + "." + dataTable + "(url, title, content, category, tags, date)")
                .append("VALUES(?, ?, ?, ?, ?, ?)");

        //检查sql字符串
        logger.info( "sql命令：" + sql );

        List<String> sqlValues = new ArrayList<>();
        sqlValues.add( informationTemplate.getInfoUrl());
        sqlValues.add( informationTemplate.getInfoTitle());
        sqlValues.add( informationTemplate.getInfoContent());
        sqlValues.add( informationTemplate.getInfoCategory());
        sqlValues.add( informationTemplate.getInfoTags());
        sqlValues.add( informationTemplate.getInfoDate());

        int status = mysqlHelper.executeUpdate(sql.toString(), sqlValues);

        logger.info("数据插入转态：success!" + "(" + status +")");
    }

    public void getDataFromMysql(IndexTemplate indexTemplate, String dataTable) {

        TransportClient client = ElasticsearchHelper.getSingleClient();

        MysqlHelper mysqlHelper = new MysqlHelper();
        String sql = "SELECT * FROM " + dataTable;
        ResultSet resultSet = mysqlHelper.executeQuery(sql, null);

        Information dataItem = new Information();

        try {

            while (resultSet.next()) {

                Map<String, Object> map = new HashMap<String, Object>();

                dataItem.setInfoId(resultSet.getInt(1));
                dataItem.setInfoUrl(resultSet.getString(2));
                dataItem.setInfoTitle(resultSet.getString(3));
                dataItem.setInfoContent(resultSet.getString(4));
                dataItem.setInfoCategory(resultSet.getString(5));
                dataItem.setInfoTags(resultSet.getString(6));
                dataItem.setInfoDate(resultSet.getString(7));

                map.put("id", dataItem.getInfoId());
                map.put("url", dataItem.getInfoUrl());
                map.put("title", dataItem.getInfoTitle());
                map.put("content", dataItem.getInfoContent());
                map.put("category", dataItem.getInfoCategory());
                map.put("tags", dataItem.getInfoTags());
                map.put("date", dataItem.getInfoDate());

                System.out.println(dataItem.getInfoId());
                System.out.println(map);

                //valueOf() 返回值：删除头尾空白符的字符串
                //prepareIndex(indexName, typeName, docId)
                client.prepareIndex(indexTemplate.getIndexName(), indexTemplate.getTypeName(), String.valueOf(dataItem.getInfoId()))
                        .setSource(map)
                        .execute()
                        .actionGet();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        //mysqlHelper.closeConnection();
    }

    public List<String> getUrlsFromMysql(String dataTable){
        List<String> urlsList = new ArrayList<>();
        int flag = 0;
        MysqlHelper mysqlHelper = new MysqlHelper();
        String sql = "SELECT * FROM " + dataTable;
        ResultSet resultSet = mysqlHelper.executeQuery(sql, null);

        try {
            while (resultSet.next() && flag < 300) {
                urlsList.add(resultSet.getString(2));

                flag++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return urlsList;
    }

    public String getAllTextFromMysql(String dataTable){
        String allText = new String();

        MysqlHelper mysqlHelper = new MysqlHelper();
        String sql = "SELECT * FROM " + dataTable;
        ResultSet resultSet = mysqlHelper.executeQuery(sql, null);

        try {
            while (resultSet.next()) {
                allText += resultSet.getString(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allText;
    }

    public String getAlltitleFromMysql(String dataTable){

        String allTitle = new String();

        MysqlHelper mysqlHelper = new MysqlHelper();
        String sql = "SELECT * FROM " + dataTable;
        ResultSet resultSet = mysqlHelper.executeQuery(sql, null);

        allTitle += "[";

        try {
            while (resultSet.next()) {
                allTitle += '"' + resultSet.getString(3) + '"' + ',';
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allTitle += "]";

        return allTitle;
    }
}