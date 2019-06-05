package com.lcc.entity;

/**
 * @ClassName DocumentTemplate
 * @Description 文档结构模板，用于searchSimilarDocuments()
 * @Properties:
 * > documentIndexName 文档的索引名
 * > documentTypeName 文档的类型名
 * > documentId 文档的编号
 * @Author 水煮鱼肠面
 * @Date 2019/5/13 21:20
 * @Version 1.0
 **/
public class DocumentTemplate {
    private String documentIndexName;

    private String documentTypeName;

    private String documentId;

    public String getDocumentIndexName() {
        return documentIndexName;
    }

    public void setDocumentIndexName(String documentIndexName) {
        this.documentIndexName = documentIndexName;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
