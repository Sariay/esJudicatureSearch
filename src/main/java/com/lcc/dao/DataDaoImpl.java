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

    public void insertDataToMysql(InformationTemplate informationTemplate) {
        MysqlHelper mysqlHelper = new MysqlHelper();
        StringBuffer sql = new StringBuffer();

        sql.append("INSERT INTO " + MysqlConfig.DATABASE_NAME.getValue() + "." + MysqlConfig.DATATABLE_NAME.getValue() + "(url, title, content, tags)")
                .append("VALUES(?, ?, ?, ?)");

        //检查sql字符串
        logger.info( "sql命令为：" + sql );

        List<String> sqlValues = new ArrayList<>();
        sqlValues.add( informationTemplate.getInfoUrl());
        sqlValues.add( informationTemplate.getInfoTitle());
        sqlValues.add( informationTemplate.getInfoContent());
        sqlValues.add( informationTemplate.getInfoTags());

        int status = mysqlHelper.executeUpdate(sql.toString(), sqlValues);

        logger.info("数据更新状态：" + status);
    }

    public void getDataFromMysql(IndexTemplate indexTemplate) {

        TransportClient client = ElasticsearchHelper.getSingleClient();

        MysqlHelper mysqlHelper = new MysqlHelper();
        String sql = "SELECT * FROM " + MysqlConfig.DATATABLE_NAME.getValue();
        ResultSet resultSet = mysqlHelper.executeQuery(sql, null);

        Information dataItem = new Information();


        try {

            while (resultSet.next()) {

                Map<String, Object> map = new HashMap<String, Object>();

                dataItem.setInfoId(resultSet.getInt(1));
                dataItem.setInfoUrl(resultSet.getString(2));
                dataItem.setInfoTitle(resultSet.getString(3));
                dataItem.setInfoContent(resultSet.getString(4));
                dataItem.setInfoTags(resultSet.getString(5));

                map.put("id", dataItem.getInfoId());
                map.put("url", dataItem.getInfoUrl());
                map.put("title", dataItem.getInfoTitle());
                map.put("content", dataItem.getInfoContent());
                map.put("tags", dataItem.getInfoTags());

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

        mysqlHelper.closeConnection();
    }
}