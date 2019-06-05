package com.lcc.entity;

/**
 * @ClassName InformationTemplate
 * @Description 司法信息 信息结构模板
 * @Properties:
 * > infoId 网页编号
 * > infoUrl 网页源
 * > infoTitle 网页标题
 * > infoContent 网页内容
 * > infoCategory 网页分类
 * > infoTags 网页关键词
 * > infodate 网页日期
 *
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:18
 * @Version 1.0
 **/
public class InformationTemplate {
    private long infoId;

    private String infoUrl;

    private String infoTitle;

    private String infoContent;

    private String infoCategory;

    private String infoTags;

    private String infoDate;

   public long getInfoId() {
        return infoId;
    }

    public void setInfoId(long infoId) {
        this.infoId = infoId;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public String getInfoCategory() {
        return infoCategory;
    }

    public void setInfoCategory(String infoCategory) {
        this.infoCategory = infoCategory;
    }

    public String getInfoTags() {
        return infoTags;
    }

    public void setInfoTags(String infoTags) {
        this.infoTags = infoTags;
    }

    public String getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(String infoDate) {
        this.infoDate = infoDate;
    }
}