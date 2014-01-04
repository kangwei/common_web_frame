/**
 * ClassName: SerializeUtil
 * CopyRight: TalkWeb
 * Date: 13-12-26
 * Version: 1.0
 */
package com.opensoft.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Description :
 *
 * @author : KangWei
 */
public class SerializeUtil {
    private static final Logger log = LoggerFactory.getLogger(SerializeUtil.class);

    /**
     * 序列化对象到byte数组
     *
     * @param object 对象
     * @return byte数组
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            //序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("serialize object invoke error, object:{}", object.toString(), e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("close ObjectOutputStream invoke error", e);
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("close ByteArrayOutputStream invoke error", e);
                }
            }
        }
        return null;
    }

    /**
     * 反序列化byte数组到对象
     *
     * @param bytes byte数组
     * @return 对象
     */
    public static Object unSerialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            //反序列化
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            log.error("unSerialize bytes invoke error", e);
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    log.error("close ByteArrayInputStream invoke error", e);
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("close ObjectInputStream invoke error", e);
                }
            }
        }
        return null;
    }
}
