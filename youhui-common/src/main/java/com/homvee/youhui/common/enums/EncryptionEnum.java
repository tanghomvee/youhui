package com.homvee.youhui.common.enums;


import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
* Copyright (c) 2018. slst.com all rights reserved
* @Description 加密
* @author  Homvee.Tang
* @date 2018-07-17 16:56
* @version V1.0
*/
public enum EncryptionEnum {


    /**
     * 将MD5的结果转为Base64
     * MD5加密后的值是128bit的，将结果用Base64 编码
     */
    MD5_2_BASE64(){
        @Override
        public String encrypt(String data) throws Exception {
            byte[] encrypted = EncryptionEnum.getMD5(data);
            return BASE_64_ENCODER.encode(encrypted);
        }
        @Override
        public String decode(String encryptedData) throws Exception {
            return null;
        }
    },
    /**
     * 将MD5的结果转为16进制数据
     * MD5加密后的值是128bit的，按4位二进制组合成一个十六进制，所以最后出来的十六进制字符串是32个
     */
    MD5_2_HEX(){
        @Override
        public String encrypt(String data) throws Exception {
            byte[] encrypted = EncryptionEnum.getMD5(data);
            return new BigInteger(1,encrypted).toString(16);
        }
        @Override
        public String decode(String encryptedData) throws Exception {
            return null;
        }
    },

    RSA(){
        private  String KEY_RSA_TYPE = "RSA";
        private  String publicKeyBase64Str = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOf+929MZBj/PvUtIGh0sCbe+BnVjkkBJbhuN+4DqTWpRAlZGpN0VPH8qOyzrKnRwu46oB8FFVAh3dDXgXELx0MC1I/dNyLaG26NTVDfZh2fnSRjcpxTf82LQZbfxxNoYgmRIDgg6thwAvZqTCSy8JjRkRDDJEthLko+Zli/b1TQIDAQAB";
        private  String privateKeyBase64Str = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI5/73b0xkGP8+9S0gaHSwJt74GdWOSQEluG437gOpNalECVkak3RU8fyo7LOsqdHC7jqgHwUVUCHd0NeBcQvHQwLUj903Itobbo1NUN9mHZ+dJGNynFN/zYtBlt/HE2hiCZEgOCDq2HAC9mpMJLLwmNGREMMkS2EuSj5mWL9vVNAgMBAAECgYAetU06elc7C7WKrnNaOs+cT2qcqPbeEnoSRUhDMDkPRibKoDDKoYGLIpO5H+ykeyT2qdMaJdqxEgqzd1KYzd9+au5J9XZ+C2Si4IuIOWvwNqIO8Y8Q2nnSptysKwihjai2xpZqLss63rEvXAAqkF+D7moBnVQD2EAFb/ox2edqdQJBAOE+dFEsm59qgsd6CYvwCNxe2phXfJ0n6yaeF2ksSirRldMurzYsHhCeTs9iqGjbrQ7VWr8bIWTUrse1MMFefs8CQQCh9RrBfSyfPf8EN9CoGwS12xtJWB6HFmk76efZefNYA0aKHyH0mT614NortFLmcFutxmvncaPm0zKLAq2/WjEjAkBqxGjsZWGrEhjFp0JJM9FvbALf/XBTKi3b00ozj0vkug3z2Ygx/H2P0Qp4YUKE2cRRaK6oMZsFqY7jmS5wKnOfAkAUgkBIHjzHUYd4lkTS9NDs4nkbGZCMFLdwwdb9QCcjjw6gsP6QxdMeHwIhpK0Wx0WkxMqnPUm/EiGFXup3n8k3AkEAptZXMRnK0qOOLTq/VPT5EVph+1DVqwtiE5lDDRJxT07Q0ljAn+fkJYI/JWY0qACvazVgS3RNYudlayvQdOIxPQ==";
        //JDK方式RSA加密最大只有1024位
        private  int KEY_SIZE = 1024;
        private  int ENCODE_PART_SIZE = KEY_SIZE/8;
        public  final String PUBLIC_KEY_NAME = "public";
        public  final String PRIVATE_KEY_NAME = "private";

        /**
         * 公钥加密
         * 描述：
         *     1字节 = 8位；
         *     最大加密长度如 1024位私钥时，最大加密长度为 128-11 = 117字节，不管多长数据，加密出来都是 128 字节长度。
         * @param data
         * @return
         */
        @Override
        public String encrypt(String data) throws Exception {
            byte [] publicBytes = Base64.decodeBase64(publicKeyBase64Str);
            //公钥加密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicBytes);
            List<byte[]> alreadyEncodeListData = new LinkedList<>();

            int maxEncodeSize = ENCODE_PART_SIZE - 11;
            String encodeBase64Result = null;
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KEY_RSA_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] sourceBytes = data.getBytes("UTF-8");
            int sourceLen = sourceBytes.length;
            for(int i=0;i<sourceLen;i+=maxEncodeSize){
                int curPosition = sourceLen - i;
                int tempLen = curPosition;
                if(curPosition > maxEncodeSize){
                    tempLen = maxEncodeSize;
                }
                //待加密分段数据
                byte[] tempBytes = new byte[tempLen];
                System.arraycopy(sourceBytes,i,tempBytes,0,tempLen);
                byte[] tempAlreadyEncodeData = cipher.doFinal(tempBytes);
                alreadyEncodeListData.add(tempAlreadyEncodeData);
            }
            //加密次数
            int partLen = alreadyEncodeListData.size();

            int allEncodeLen = partLen * ENCODE_PART_SIZE;
            //存放所有RSA分段加密数据
            byte[] encodeData = new byte[allEncodeLen];
            for (int i = 0; i < partLen; i++) {
                byte[] tempByteList = alreadyEncodeListData.get(i);
                System.arraycopy(tempByteList,0,encodeData,i*ENCODE_PART_SIZE,ENCODE_PART_SIZE);
            }
            encodeBase64Result = Base64.encodeBase64String(encodeData);
            return encodeBase64Result;
        }

        /**
         * 私钥解密
         * @param encryptedData
         */
        @Override
        public String decode(String encryptedData) throws Exception {
            byte[] privateBytes = Base64.decodeBase64(privateKeyBase64Str);
            byte[] encodeSource = Base64.decodeBase64(encryptedData);
            int encodePartLen = encodeSource.length/ENCODE_PART_SIZE;
            //所有解密数据
            List<byte[]> decodeListData = new LinkedList<>();
            String decodeStrResult = null;
            //私钥解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_RSA_TYPE);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(KEY_RSA_TYPE);
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            //初始化所有被解密数据长度
            int allDecodeByteLen = 0;
            for (int i = 0; i < encodePartLen; i++) {
                byte[] tempEncodedData = new byte[ENCODE_PART_SIZE];
                System.arraycopy(encodeSource,i*ENCODE_PART_SIZE,tempEncodedData,0,ENCODE_PART_SIZE);
                byte[] decodePartData = cipher.doFinal(tempEncodedData);
                decodeListData.add(decodePartData);
                allDecodeByteLen += decodePartData.length;
            }
            byte [] decodeResultBytes = new byte[allDecodeByteLen];
            for (int i = 0,curPosition = 0; i < encodePartLen; i++) {
                byte[] tempSorceBytes = decodeListData.get(i);
                int tempSourceBytesLen = tempSorceBytes.length;
                System.arraycopy(tempSorceBytes,0,decodeResultBytes,curPosition,tempSourceBytesLen);
                curPosition += tempSourceBytesLen;
            }
            decodeStrResult = new String(decodeResultBytes,"UTF-8");
            return decodeStrResult;
        }

        public Map<String,String> createRSAKeys() throws Exception{
            //里面存放公私秘钥的Base64位加密
            Map<String,String> keyPairMap = Maps.newHashMap();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_RSA_TYPE);
            keyPairGenerator.initialize(KEY_SIZE,new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //获取公钥秘钥
            String publicKeyValue = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            String privateKeyValue = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());

            //存入公钥秘钥，以便以后获取
            keyPairMap.put(PUBLIC_KEY_NAME,publicKeyValue);
            keyPairMap.put(PRIVATE_KEY_NAME,privateKeyValue);

            return keyPairMap;
        }

    }

    ;


    private static byte[] getMD5(String data) throws Exception {
        String in = data + "";
        byte[] bytes = in.getBytes("UTF-8");
        MessageDigest md5=MessageDigest.getInstance("MD5");
        /**
         * MD5的结果是一个16字节128位数组
         */
        byte[] encrypted = md5.digest(bytes);
        return encrypted;
    }


   private static final BASE64Encoder BASE_64_ENCODER = new BASE64Encoder();

   public abstract String encrypt(String data) throws Exception;
   public abstract String decode(String encryptedData) throws Exception;
}
