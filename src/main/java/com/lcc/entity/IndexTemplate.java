package com.lcc.entity;

/**
 * @ClassName IndexTemplate
 * @Description 司法信息 索引结构模板
 * @Properties:
 * > indexName 索引名
 * > typeName 类型名
 * > mappingStr mapping字符串
 * > fieldsName 字段名
 * > shardsNum 分片数
 * > replicasNum 副分片数
 *
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:19
 * @Version 1.0
 **/
public class IndexTemplate {

    private String indexName;

    private String typeName;

    private String mappingStr;

    private String[] fieldsName;

    private int shardsNum;

    private int replicasNum;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getMappingStr() {
        return mappingStr;
    }

    public void setMappingStr(String mappingStr) {
        this.mappingStr = mappingStr;
    }

    public String[] getFieldsName() {
        return fieldsName;
    }

    public void setFieldsName(String[] fieldsName) {
        this.fieldsName = fieldsName;
    }

    public int getShardsNum() {
        return shardsNum;
    }

    public void setShardsNum(int shardsNum) {
        this.shardsNum = shardsNum;
    }

    public int getReplicasNum() {
        return replicasNum;
    }

    public void setReplicasNum(int replicasNum) {
        this.replicasNum = replicasNum;
    }
}