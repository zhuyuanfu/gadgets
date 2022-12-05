package cn.edu.njfu.zyf.util;

import org.apache.commons.codec.digest.DigestUtils;


public class Md5Utils {
    public static String toMd5(String s) {
        return DigestUtils.md5Hex(s);
    }
}
