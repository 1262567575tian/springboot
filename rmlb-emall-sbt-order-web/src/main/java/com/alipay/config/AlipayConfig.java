package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id ="2016091800541090";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZ9mheRcoRK2ddiHWmhINuMZkwSjFegrgfPCl1/jyQ1+UxDFd5SdNi/9yx6wmHXzPJjqM25vNDWsDEMigSxSaJ+H/qpajlVNDTck/ECh3UijPUhDz2pw+NMwQ254EqtQ1ayl+PjlU5INV7m4pPvwDkOAVh+hsFLv7uxV0A+egI4P+GQalkOLitNi95xs1uoufLQ421OaGLcZpGjRJQX6fnYEIf0HptG+7iqMIcom1FnEy1mKKasFqDN1Lb8Nue/8EWr2LVVoTPkjnLx4PYwCx46yMOq67v+f6hlWd6EqfJ6RP/Zb7Uho0qaC0PsLxg32VgLaAmPOYL7Ix7Fwp5J9BnAgMBAAECggEAMlNW/im1yTL00KAARPPfui9AMYau6IO2B4+0xbratUW8xvGPzjeRfzS/QPDg5nz6ApI7S4DmV0fc5sS2UJxc4pB1z49iy8OTluowpJXh9k1Ob9Lh0oX9EVxxrjiyAfG4ccrgvZ36wXe4EHVhGnwGoJKnP8Vu+5zG++lLYzxM2kmfmJvckf22bIf9lQBwbwHM0TA3SaNvnhlQkDaxor3nFese0lbhTuL+telSAYeqGAjnjjUpdrbRRi6HTR5Ze1yWUWCgkcvOrb/7T/JCDFgUknQh6MWb2wY6j9/BB7E/fX0TS0VcWoj724bgS4qlGvYIjubECaifu5Ig+s+uJWkfkQKBgQDOix3+bDFHIyPN4HNgvmycVFFXkAFLhBUuYii+74BxLmMaUdb50cMNH5BtDgeoykE5jO9ltwrb9+Li7X+H6iAtV/Vf0Ho/yxGFK0tW9rjYc+k/aKhS5LZ9q0Ico0bapbJWO5MMEmWxJIt/37Cg1MImcPwGWcdI+IO9E/UNGMdtEwKBgQC+1CPSMQ18RJBP/wXfqDn6QyQcQ0EqDp2dUwl1CYtfTt4TZl5qSVEF/BLoZPxJZaRb8mwCHnZvHcui0fUPbQDTp+Pi6lOBG/EAiUhb1y0arOzN9bQlLzWTGgFZncQLG2lgnrKObjiWj7x15niC7CIs1JOVD68gN2fMvRh3Qb6d3QKBgQC8+6OVEO/B1FBFZvKK+ZGtNKU8xkG5FADBcQUxNzlB1hzNj0ZxTNH4pwHiFu1BLtahTugZHHNuM5FA6+g9tyZ79rhIVBwXVLoSLDwCNTuKqfwojZ0+Q5EKyXFzfJ4vH19e8aGpcQflHrgZZ9ZYQnPm8ohXUzw581Gu5pzYv+xxSwKBgQCBIBclEaAoaFSmbNBSqQiiOXBj13ZuuPkaDagmk9DFKx7GTcyxau7yd8Frk3nLSIXQ2dcQEADf629xu1RxnbajgcWINDtQ79FCJaLa0l1o4X5TV2EQkENWSJCFTWXshh3mDTyReC++YYCK3urI3HO7lhRPFw/PYu5P8dxuRgRv2QKBgG/BSflJfGoUCY7ApXYtt1PCuEEalSRkawyPp18xx7xxUYMgFpt15V6O8UdRjaR50t7bO+oE418ryoaRyFBhlIODGrjeqSgr7uiAlSPpkkku/bq6GzC7ZOumJ0KewcJ/s8erG3DPrezk14CBAyMWsoB5GWam69VXIrsiC6ne4FbV";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA652avCEQBB6f6VMoIEQ8Yz9dvVgXDq01X479a+OQUPt9bVXKSg9YEVAnfVT7+AkqwmsDyXYuG39Z7a5h2fath9p0jEgZAgPpeBVwMf/Bsg2hu1Pr3qFsP7hpeqVC0aZCXSt7mVaHqmhQaDWk/QzaKs+WK7eZEOal5Y+ce9tJFwrxfVjU1r2czz1rkuUKy9f0l4OfLUjgNBOuHSeMnWb5jYByFFzH0GlkKbv/qxjJM6fgXsWKKS3uwxou5BIa6IR/Pvj02rTHIUlYBG5qrY5FriMWATK5vfZC5u6nQYde+f8nf3OdoK59EditruQ98B0NzDx8bSLpgoXxf/Cqf6reKQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://192.168.4.215:8035/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://192.168.4.215:8035/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "https://openapi.alipaydev.com/gateway.do";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

