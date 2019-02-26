package test.com.homve.livebroadcast.service;

import com.google.common.collect.Lists;
import com.homvee.youhui.dao.dictionary.DictionaryDao;
import com.homvee.youhui.dao.dictionary.model.Dictionary;
import com.homvee.youhui.dao.sms.PortInfoDao;
import org.junit.Test;
import org.springframework.util.StringUtils;
import test.com.homve.BaseTest;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-09-06 14:18
 */
public class PortInfoServiceImplTest extends BaseTest {

    @Resource
    private PortInfoDao portInfoDao
            ;

    @Resource
    private DictionaryDao dictionaryDao;
    @Test
    public void save() {
     List<String> words = read();

     for (String word : words){
         if (StringUtils.isEmpty(word)){
             continue;
         }
         Dictionary dictionary = new Dictionary();
         dictionary.setContent(word);
         dictionary.setCreator("sys");
         try{

             dictionaryDao.save(dictionary);
         }catch (Exception ex){

         }
     }
    }

    public List<String> read() {
        List<String> words = Lists.newArrayList();
        try {
            FileReader fr = new FileReader("D:/words.txt");
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                words.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return words;
    }
}