package com.lcc.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MultiPatterMatchAc
 * @Description 多模字符串匹配：The code snippet comes from & refer to 'https://www.cnblogs.com/nullzx/p/7499397.html'
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/5/5 0:10
 * @Version 1.0
 **/
public class MultiPatterMatchAc {
    /*AC自动机处理的字符串长度*/
    private static final int ASCII = 99999;

    /*AC自动机的根结点，根结点不存储任何字符信息*/
    private Node root;

    /*待查找的目标字符串集合*/
    private List<String> target;

    /*表示在文本字符串中查找的结果，key表示目标字符串， value表示目标字符串在文本串出现的位置*/
    private HashMap<String, List<Integer>> result;

    /*内部静态类，用于表示AC自动机的每个结点，在每个结点中我们并没有存储该结点对应的字符*/
    private static class Node{

        /*如果该结点是一个终点，即，从根结点到此结点表示了一个目标字符串，则str != null, 且str就表示该字符串*/
        String str;

        /*所以这里相当于ASCII叉树*/
        Node[] table = new Node[ASCII];

        /*当前结点的孩子结点不能匹配文本串中的某个字符时，下一个应该查找的结点*/
        Node fail;

        public boolean isWord(){
            return str != null;
        }

    }

    /**
     * @MethodName MultiPatterMatchAc
     * @Description 构造函数，target表示待查找的目标字符串集合
     * @Param [target]
     * @Return
     **/
    public MultiPatterMatchAc(List<String> target) {
        root = new Node();
        this.target = target;
        buildTrieTree();
        build_AC_FromTrie();
    }

    /**
     * @MethodName buildTrieTree
     * @Description 由目标字符串构建Trie树
     * @Param []
     * @Return void
     **/
    private void buildTrieTree(){
        for(String targetStr : target){
            Node curr = root;
            for(int i = 0; i < targetStr.length(); i++){
                char ch = targetStr.charAt(i);
                if(curr.table[ch] == null){
                    curr.table[ch] = new Node();
                }
                curr = curr.table[ch];
            }
            /*将每个目标字符串的最后一个字符对应的结点变成终点*/
            curr.str = targetStr;
        }
    }

    /**
     * @MethodName build_AC_FromTrie
     * @Description 由Trie树构建AC自动机，本质是一个自动机，相当于构建KMP算法的next数组
     * @Param []
     * @Return void
     **/
    private void build_AC_FromTrie(){
        /*广度优先遍历所使用的队列*/
        LinkedList<Node> queue = new LinkedList<Node>();

        /*单独处理根结点的所有孩子结点*/
        for(Node x : root.table){
            if(x != null){
                /*根结点的所有孩子结点的fail都指向根结点*/
                x.fail = root;
                queue.addLast(x);/*所有根结点的孩子结点入列*/
            }
        }

        while(!queue.isEmpty()){
            /*确定出列结点的所有孩子结点的fail的指向*/
            Node p = queue.removeFirst();
            for(int i = 0; i < p.table.length; i++){
                if(p.table[i] != null){
                    /*孩子结点入列*/
                    queue.addLast(p.table[i]);
                    /*从p.fail开始找起*/
                    Node failTo = p.fail;
                    while(true){
                        /*说明找到了根结点还没有找到*/
                        if(failTo == null){
                            p.table[i].fail = root;
                            break;
                        }

                        /*说明有公共前缀*/
                        if(failTo.table[i] != null){
                            p.table[i].fail = failTo.table[i];
                            break;
                        }else{/*继续向上寻找*/
                            failTo = failTo.fail;
                        }
                    }
                }
            }
        }
    }

    /**
     * @MethodName find
     * @Description 在文本串中查找所有的目标字符串
     * @Param [text]
     * @Return java.util.HashMap<java.lang.String,java.util.List<java.lang.Integer>>
     **/
    public HashMap<String, List<Integer>> find(String text){
        /*创建一个表示存储结果的对象*/
        result = new HashMap<String, List<Integer>>();
        for(String s : target){
            result.put(s, new LinkedList<Integer>());
        }

        Node curr = root;
        int i = 0;
        while(i < text.length()){
            /*文本串中的字符*/
            char ch = text.charAt(i);

            /*文本串中的字符和AC自动机中的字符进行比较*/
            if(curr.table[ch] != null){
                /*若相等，自动机进入下一状态*/
                curr = curr.table[ch];

                if(curr.isWord()){
                    result.get(curr.str).add(i - curr.str.length()+1);
                }

                /*这里很容易被忽视，因为一个目标串的中间某部分字符串可能正好包含另一个目标字符串，
                 * 即使当前结点不表示一个目标字符串的终点，但到当前结点为止可能恰好包含了一个字符串*/
                if(curr.fail != null && curr.fail.isWord()){
                    result.get(curr.fail.str).add(i - curr.fail.str.length()+1);
                }

                /*索引自增，指向下一个文本串中的字符*/
                i++;
            }else{
                /*若不等，找到下一个应该比较的状态*/
                curr = curr.fail;

                /*到根结点还未找到，说明文本串中以ch作为结束的字符片段不是任何目标字符串的前缀，
                 * 状态机重置，比较下一个字符*/
                if(curr == null){
                    curr = root;
                    i++;
                }
            }
        }
        return result;
    }

    /**
     * @MethodName resultSize
     * @Description 多模字符串匹配的结果大小
     * @Param [keywords, pageContent]
     * @Return int
     **/
    public static int resultSize(List<String> keywords, String pageContent){
        MultiPatterMatchAc multiPatterMatchAc = new MultiPatterMatchAc(keywords);
        HashMap<String, List<Integer>> multiPatterMatchResult = multiPatterMatchAc.find(pageContent);
        int resultSize = 0;

        for(Map.Entry<String, List<Integer>> entry : multiPatterMatchResult.entrySet()){
            resultSize += entry.getValue().size();
        }
        return resultSize;
    }
}