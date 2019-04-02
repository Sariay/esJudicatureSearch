package com.lcc.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextTagsTest {

    @Test
    public void getTags() {
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
        TextTags.getTags(content);
    }
}