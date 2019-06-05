package com.lcc.utils;

import com.lcc.dao.DataDao;
import com.lcc.dao.DataDaoImpl;
import com.lcc.entity.ElasticsearchConfig;
import com.lcc.entity.IndexTemplate;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @ClassName ElasticsearchHelper
 * @Description TODO
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:52
 * @Version 1.0
 **/
public class ElasticsearchHelper {
    private static volatile TransportClient client;

    //public static final String CLUSTER_NAME = "my-application";
    //public static final String HOST_IP = "127.0.0.1";
    //public static final int TCP_PORT = 9300;
    private static final String CLUSTER_NAME = ElasticsearchConfig.CLUSTER_NAME.getValue();
    private static final String HOST_IP = ElasticsearchConfig.HOST_IP.getValue();
    private static final int TCP_PORT = Integer.parseInt(ElasticsearchConfig.TCP_PORT.getValue());

    static Settings settings = Settings.builder()
            .put("cluster.name", CLUSTER_NAME)
            .build();

    /**
     * @MethodName getClient
     * @Description 产生多个client
     * @Param []
     * @Return org.elasticsearch.client.transport.TransportClient
     **/
    public static TransportClient getClient() {
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName( HOST_IP ), TCP_PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * @MethodName getSingleClient
     * @Description 单例模式，只产生一个client
     * @Param []
     * @Return org.elasticsearch.client.transport.TransportClient
     **/
    public static TransportClient getSingleClient() {
        if (client == null) {
            synchronized (TransportClient.class) {
                //
            }
            if (client == null) {
                try {
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_IP), TCP_PORT));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return client;
    }

    /**
     * @MethodName getIndicesAdminClient
     * @Description 获取索引管理的IndicesAdminClient
     * @Param []
     * @Return org.elasticsearch.client.IndicesAdminClient
     **/
    public static IndicesAdminClient getIndicesAdminClient() {
        return getSingleClient().admin().indices();
    }

    /**
     * @MethodName createIndex
     * @Description 创建索引
     * @Param [indexTemplate]
     * @Return boolean
     **/
    public static boolean createIndex(IndexTemplate indexTemplate) {
        String indexName = indexTemplate.getIndexName();
        int shards = indexTemplate.getShardsNum();
        int replicas = indexTemplate.getReplicasNum();

        Settings settings = Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas)
                .build();

        CreateIndexResponse createIndexResponse = getIndicesAdminClient()
                .prepareCreate(indexName.toLowerCase())
                .setSettings(settings)
                .execute()
                .actionGet();

        boolean isIndexCreated = createIndexResponse.isAcknowledged();
        if (isIndexCreated) {
            System.out.println("The index of " + indexName + " was created successfully.");
        } else {
            System.out.println("The index of " + indexName + " was created unsuccessfully!!!");
        }

        return isIndexCreated;
    }

    /**
     * @MethodName deleteIndex
     * @Description 删除索引
     * @Param [indexTemplate]
     * @Return boolean
     **/
    public static boolean deleteIndex(IndexTemplate indexTemplate) {
        String indexName = indexTemplate.getIndexName();

        DeleteIndexResponse deleteIndexResponse = getIndicesAdminClient()
                .prepareDelete(indexName.toLowerCase())
                .execute()
                .actionGet();

        boolean isDeleteIndex = deleteIndexResponse.isAcknowledged();
        if (isDeleteIndex) {
            System.out.println("The index of " + indexName + " was deleted successfully.");
        } else {
            System.out.println("The index of " + indexName + " was deleted unsuccessfully!!!");
        }
        return isDeleteIndex;
    }

    /**
     * @MethodName setMapping
     * @Description 设置字段约束(mapping)
     * @Param [indexTemplate]
     * @Return boolean
     **/
    public static boolean setMapping(IndexTemplate indexTemplate) {
        String indexName = indexTemplate.getIndexName();
        String typeName = indexTemplate.getTypeName();
        String mapping = indexTemplate.getMappingStr();

        getIndicesAdminClient().preparePutMapping(indexName)
                .setType(typeName)
                .setSource(mapping, XContentType.JSON)
                .get();
        return true;
    }

    /**
     * @MethodName isIndexExists
     * @Description 判断索引是否存在
     * @Param [indexTemplate]
     * @Return boolean
     **/
    public static boolean isIndexExists(IndexTemplate indexTemplate) {
        String indexName = indexTemplate.getIndexName();

        IndicesExistsResponse indicesExistsResponse = getIndicesAdminClient()
                .exists(new IndicesExistsRequest().indices(new String[]{indexName}))
                .actionGet();

        boolean existsStatus = indicesExistsResponse.isExists();
        if (existsStatus) {
            System.out.println("The index of \"" + indexName + "\" is existed.");
        } else {
            System.out.println("The index of \"" + indexName + "\" is not existed.");
        }
        return existsStatus;
    }

    /**
     * @MethodName isTypeExists
     * @Description 判断索引的类型是否存在
     * @Param [indexTemplate]
     * @Return boolean
     **/
    public static boolean isTypeExists(IndexTemplate indexTemplate) {
        String indexName = indexTemplate.getIndexName();
        String typeName = indexTemplate.getTypeName();

        TypesExistsResponse typesExistsResponse = getIndicesAdminClient()
                .typesExists(new TypesExistsRequest(new String[]{indexName}, typeName))
                .actionGet();

        boolean existsStatus = typesExistsResponse.isExists();
        if (existsStatus) {
            System.out.println("The type of \"" + typeName + "\" is existed.");
        } else {
            System.out.println("The type of \"" + typeName + "\" is not existed.");
        }
        return existsStatus;
    }


    /**
     * @MethodName daoMysqlToElasticsearch
     * @Description 将Mysql中的数据导入elasticsearch, 并为每条记录创建索引(1-创建索引; 2-设置mapping; 3-查询Mysql并将数据写入elasticsearch索引库)
     * @Param []
     * @Return void
     **/
    public static void daoMysqlToElasticsearch(String dataTable, String indexName) {
        final String currentIndexName = (indexName!=null)?indexName:ElasticsearchConfig.INDEX_NAME.getValue();
        final String currentTypeName = ElasticsearchConfig.TYPE_NAME.getValue();
        final int currentIndexShards = Integer.parseInt(ElasticsearchConfig.INDEX_SHARDS.getValue());
        final int currentIndexReplicas = Integer.parseInt(ElasticsearchConfig.INDEX_REPLICAS.getValue());

        IndexTemplate indexTemplate = new IndexTemplate();
        indexTemplate.setIndexName(currentIndexName);
        indexTemplate.setTypeName(currentTypeName);
        indexTemplate.setShardsNum(currentIndexShards);
        indexTemplate.setReplicasNum(currentIndexReplicas);

        // 第一步 1
        boolean isIndexExists = ElasticsearchHelper.isIndexExists(indexTemplate);
        if (!isIndexExists) {
            ElasticsearchHelper.createIndex(indexTemplate);
        }

        // 第二步 2
        try {
            XContentBuilder builder = jsonBuilder().startObject()
                    .startObject("properties")

                    .startObject("id")
                    .field("type", "integer")
                    .endObject()

                    .startObject("url")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("title")
                    .field("type", "text")
                    .field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_max_word")
                    .field("boost", 2)
                    .endObject()

                    .startObject("content")
                    .field("type", "text")
                    .field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_max_word")
                    .endObject()

                    .startObject("category")
                    .field("type", "text")
                    .endObject()

                    .startObject("tags")
                    .field("type", "keyword")
                    .endObject()

                    .startObject("date")
                    .field("type", "keyword")
                    .endObject()

                    .endObject()
                    .endObject();

            System.out.println(builder.string());

            indexTemplate.setMappingStr(builder.string());

            ElasticsearchHelper.setMapping(indexTemplate);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 第三步 3
        DataDao dataDao = new DataDaoImpl();
        dataDao.getDataFromMysql(indexTemplate, dataTable);
    }
}