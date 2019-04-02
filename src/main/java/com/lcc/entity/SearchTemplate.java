package com.lcc.entity;

/**
 * @ClassName SearchTemplate
 * @Description 司法信息 搜索结构模板
 * @Properties:
 * > query 搜索时，输入的字符串
 * > pageNumber 当前页码
 * > pageHits 每页展示的结果数
 * > highlightTitle 高亮标题字段
 * > highlightContent 高亮内容字段
 * > indexNames 搜索时，需要搜索的索引
 * > typeNames 搜索时，需要搜索的类型
 * > fieldsName  搜索时，需要搜索或高亮的字段
 *
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:20
 * @Version 1.0
 **/
public class SearchTemplate {
    private String query;

    private int pageNumber;

    private int pageHits;

    private String highlightTitle;

    private String highlightContent;

    private String[] indexNames;

    private String[] typeNames;

    private String[] fieldsName;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageHits() {
        return pageHits;
    }

    public void setPageHits(int pageHits) {
        this.pageHits = pageHits;
    }

    public String getHighlightTitle() {
        return highlightTitle;
    }

    public void setHighlightTitle(String highlightTitle) {
        this.highlightTitle = highlightTitle;
    }

    public String getHighlightContent() {
        return highlightContent;
    }

    public void setHighlightContent(String highlightContent) {
        this.highlightContent = highlightContent;
    }

    public String[] getIndexNames() {
        return indexNames;
    }

    public void setIndexNames(String[] indexNames) {
        this.indexNames = indexNames;
    }

    public String[] getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(String[] typeNames) {
        this.typeNames = typeNames;
    }


    public String[] getFieldsName() {
        return fieldsName;
    }

    public void setFieldsName(String[] fieldsName) {
        this.fieldsName = fieldsName;
    }
}