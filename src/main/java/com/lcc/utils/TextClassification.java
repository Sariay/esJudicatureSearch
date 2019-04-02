package com.lcc.utils;

import com.hankcs.hanlp.classification.classifiers.IClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.corpus.io.IOUtil;

import java.io.File;
import java.io.IOException;

import static com.lcc.utils.TestUtility.ensureTestData;

/**
 * @ClassName TextClassification
 * @Description 文本分类器
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/4/1 22:06
 * @Version 1.0
 **/
public class TextClassification {

    /**
     * 搜狗文本分类语料库5个类目，每个类目下1000篇文章，共计5000篇文章
     */
    public static final String CORPUS_FOLDER = ensureTestData("搜狗文本分类语料库迷你版", "http://hanlp.linrunsoft.com/release/corpus/sogou-text-classification-corpus-mini.zip");
    /**
     * 模型保存路径
     */
    public static final String MODEL_PATH = "data/test/classification-model.txt";


    public static void main(String[] args) throws IOException {

        IClassifier classifier = new NaiveBayesClassifier(trainOrLoadModel());
        predict(classifier, "C罗获2018环球足球奖最佳球员 德尚荣膺最佳教练");
        predict(classifier, "英国造航母耗时8年仍未服役 被中国速度远远甩在身后");
        predict(classifier, "研究生考录模式亟待进一步专业化");
        predict(classifier, "如果真想用食物解压,建议可以食用燕麦");
        predict(classifier, "通用及其部分竞争对手目前正在考虑解决库存问题");
    }

    private static void predict(IClassifier classifier, String text) {
        System.out.printf("《%s》 属于分类 【%s】\n", text, classifier.classify(text));
    }

    private static NaiveBayesModel trainOrLoadModel() throws IOException {
        NaiveBayesModel model = (NaiveBayesModel) IOUtil.readObjectFrom(MODEL_PATH);
        if (model != null) return model;

        File corpusFolder = new File(CORPUS_FOLDER);
        if (!corpusFolder.exists() || !corpusFolder.isDirectory()) {
            System.err.println("没有文本分类语料，请阅读IClassifier.train(java.lang.String)中定义的语料格式与语料下载：" +
                    "https://github.com/hankcs/HanLP/wiki/%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E4%B8%8E%E6%83%85%E6%84%9F%E5%88%86%E6%9E%90");
            System.exit(1);
        }

        IClassifier classifier = new NaiveBayesClassifier(); // 创建分类器，更高级的功能请参考IClassifier的接口定义
        classifier.train(CORPUS_FOLDER);                     // 训练后的模型支持持久化，下次就不必训练了
        model = (NaiveBayesModel) classifier.getModel();
        //IOUtil.saveObjectTo(model, MODEL_PATH);
        return model;
    }
}
