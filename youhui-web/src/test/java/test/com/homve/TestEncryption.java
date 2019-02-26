package test.com.homve;

import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright (c) 2018$. ddyunf.com all rights reserved
 *
 * @author Homvee.Tang(tanghongwei @ ddcloudf.com)
 * @version V1.0
 * @Description TODO(用一句话描述该文件做什么)
 * @date 2018-08-23 18:37
 */
public class TestEncryption {

    static class Test{

    }
    public static void main(String[] args) throws Exception {

        ConcurrentLinkedQueue<Long> linkedQueue = new ConcurrentLinkedQueue();
        for (Long acctId=1L ; acctId < 10 ; acctId ++) {
            linkedQueue.offer(acctId);
        }
        for (Long acctId=1L ; acctId < 10 ; acctId ++) {
            try {
                if (!linkedQueue.isEmpty()) {
                    if (linkedQueue.contains(acctId)) {
                        if (!linkedQueue.element().equals(acctId)) {
                            System.out.println(acctId);
                            continue;
                        }
                        acctId = linkedQueue.poll();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            linkedQueue.offer(acctId);
        }
        List<Test> tests = Lists.newArrayList();
        while (true){
            tests.add(new Test());
        }
    }


    private static String KEY_RSA_TYPE = "RSA";
    private static int KEY_SIZE = 1024;//JDK方式RSA加密最大只有1024位
    private static int ENCODE_PART_SIZE = KEY_SIZE/8;
    public static final String PUBLIC_KEY_NAME = "public";
    public static final String PRIVATE_KEY_NAME = "private";

    /**
     * 创建公钥秘钥
     * @return
     */
    public static Map<String,String> createRSAKeys(){
        Map<String,String> keyPairMap = new HashMap<>();//里面存放公私秘钥的Base64位加密
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_RSA_TYPE);
            keyPairGenerator.initialize(KEY_SIZE,new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //获取公钥秘钥
            String publicKeyValue = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKeyValue = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());

            //存入公钥秘钥，以便以后获取
            keyPairMap.put(PUBLIC_KEY_NAME,publicKeyValue);
            keyPairMap.put(PRIVATE_KEY_NAME,privateKeyValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPairMap;
    }

}
