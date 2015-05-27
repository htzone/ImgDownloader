package com.example.test_5_imagedownload;

import java.security.MessageDigest;

public class Md5
{
  public static final String MD5(String str)
  {
    char[] hexDigits = { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 
      'e', 'f' };
    try {
      byte[] strTemp = str.getBytes();
      MessageDigest mdTemp = MessageDigest.getInstance("MD5");
      mdTemp.update(strTemp);
      byte[] tmp = mdTemp.digest();

      char[] strs = new char[32];

      int k = 0;
      for (int i = 0; i < 16; i++)
      {
        byte byte0 = tmp[i];
        strs[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];

        strs[(k++)] = hexDigits[(byte0 & 0xF)];
      }
      return new String(strs).toUpperCase(); } catch (Exception e) {
    }
    return null;
  }

//  public static void main(String[] args)
//  {
//    System.out.println(MD5("2011123456").toLowerCase());
//  }
}
