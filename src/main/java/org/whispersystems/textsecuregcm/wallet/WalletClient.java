/**
 * 
 */
package org.whispersystems.textsecuregcm.wallet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.textsecuregcm.configuration.RadarConfiguration;

public class WalletClient {

  private final String radarUrl;
  private final Client client;
  private final String md5Key;
  private final Logger logger = LoggerFactory.getLogger(WalletClient.class);

  public WalletClient(RadarConfiguration config)
  {
    this.radarUrl = config.getUrl();
    this.md5Key   = config.getMd5Key();
    this.client   = initializeClient(config);
  }

  // 3.1 根据手机获取账号列表
  public WalletCommData getAccounts(String phoneCode,String phoneNumber) {

    WalletCommData info = client
                      .target(radarUrl)
                      .path("/api/im/account_list")
                      .queryParam("phoneCode", phoneCode)
                      .queryParam("phoneNumber", phoneNumber)
                      .request()
                      .get(WalletCommData.class);
    
    return info;
  }

  // 3.2 账号余额查询:根据昵称/email/钱包
  public WalletCommData getWalletinfo(String accountName) {

    WalletCommData info = client
                      .target(radarUrl)
                      .path("/api/im/check")
                      .queryParam("accountName", accountName)
                      .request()
                      .get(WalletCommData.class);
    return info;
  }

  // 3.3 注册
  public WalletCommData register(String phoneCode,String phoneNumber){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%d%s", phoneCode,phoneNumber,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/register")
                      .queryParam("phoneCode", phoneCode)
                      .queryParam("phoneNumber", phoneNumber)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  // 3.4 用户余额
  public WalletCommData getBalance(String address) {

    WalletCommData info =   client
                      .target(radarUrl)
                      .path("/api/im/balance")
                      .queryParam("address", address)
                      .request()
                      .get(WalletCommData.class);
    return info;
  }

  /**
   * 3.5 申请手机短信 sign: MD5(address+ timestamp + signKey) or MD5(phoneCode + phoneNumber+ timestamp + signKey)
   * @param address 钱包地址
   * @param phoneCode 手机区号
   * @param phoneNumber 手机号
   * @return 
   */
  public WalletCommData getSmsCode(String address,String phoneCode,String phoneNumber){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = "";
    if(Strings.isNullOrEmpty(address))
    {
      sign = getSign(String.format("%s%s%d%s", phoneCode,phoneNumber,longStamp,this.md5Key));
    }else{
      sign = getSign(String.format("%s%d%s", address,longStamp,this.md5Key));
    }
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/sms_code")
                      .queryParam("address", address)
                      .queryParam("phoneCode", phoneCode)
                      .queryParam("phoneNumber", phoneNumber)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
   * 3.6 转账 sign: MD5(address + destination + currency + issuer + amount + timestamp + signKey)
   * @param address 钱包地址
   * @param destination 目标钱包地址
   * @param currency 货币(比如说 CNY)
   * @param amount 转账金额(string 类型)
   * @param password 转账密码，可能是支付密码/手机短信/GA。 根据用户安全设置来 
   * @param issuer 货币网关地址(VRP 或者 VBC 传"RADR"即可，否则传具体网关地
   * @return
   */
  public WalletCommData makeTx(String address,String destination,String currency,String amount,String password,String issuer){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%s%s%s%d%s", address,destination,currency,issuer,amount,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/make_tx")
                      .queryParam("address", address)
                      .queryParam("destination", destination)
                      .queryParam("currency", currency)
                      .queryParam("amount", amount)
                      .queryParam("password", password)
                      .queryParam("issuer", issuer)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  // 3.8 获取 google_auth 密钥(开启 google auth 第一步)
  /**
     * 
      o address: 钱包地址
      o flag: 0:关闭 1:开启
      o password :密码，可能是支付密码/手机短信/GA。 根据用户安全设置来 
      o sign: MD5(address + flag + timestamp + signKey)
     */
  public WalletCommData setGoogleAuth(String address,int flag,String password){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%d%d%s", address,flag,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/set_google_auth")
                      .queryParam("address", address)
                      .queryParam("flag", flag)
                      .queryParam("password", password)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
    * 3.9 确认开启 google_auth(开启 google auth 第二步:确认) sign: MD5(address + verifyCode + timestamp + signKey)
    * @param address  钱包地址
    * @param verifyCode set_google_auth 获取的 google auth 验证码
    * 
    * @return
  */
  public WalletCommData confirmGoogleAuth(String address,String verifyCode){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%d%s", address,verifyCode,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/confirm_google_auth")
                      .queryParam("address", address)
                      .queryParam("verifyCode", verifyCode)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
   * 3.10 验证用户权限(绑定用户的时候使用) sign: MD5(accountName + timestamp + signKey)
   * @param address 昵称/钱包地址/邮箱
   * @param password 转账密码，可能是支付密码/手机短信/GA。 根据用户安全设置来
   * @return
   */
  public WalletCommData verify(String accountName,String password){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%d%s", accountName,longStamp,this.md5Key));
    WalletCommData response = client
                      .target(radarUrl)
                      .path("/api/im/verify")
                      .queryParam("accountName", accountName)
                      .queryParam("password", password)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .get(WalletCommData.class);
    return response;
  }

  /**
   * 3.11 交易历史
   * @param address 钱包地址
   * @param marker 翻页(不传默认第一页,是个 json，每次接口会返回 maker，下一页传该值即可)
   * @return 
   */
  public Response getTxHistory(String address,String marker) {

    String jsonStr = "";
    if(!Strings.isNullOrEmpty(marker)){
        jsonStr = marker.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
    } 
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/tx_history")
                      .queryParam("address", address)
                      .queryParam("marker", jsonStr)
                      .resolveTemplate("marker", String.format("{\"api/im/tx_history\":%s}", jsonStr))
                      .request()
                      .get();
    return response;
  }

  // charts/latest/
  /**
   * 3.12 汇率 
   * @param cur1 货币代号1
   * @param issuer1 网关1
   * @param cur2 货币代号2
   * @param issuer2 网关2
   * @return 价格默认以currency2位目标货币,currency1为价格货币.例如获取VBC价格,需要将CNY放在第一位,VBC放在第二位
   */
  public WalletCommData getChartsLatest(String cur1,String issuer1,String cur2,String issuer2) {

    WalletCommData info =   client
                      .target("https://c.radarlab.org")
                      .path("/api/charts/latest")
                      .queryParam("cur1", cur1)
                      .queryParam("issuer1", issuer1)
                      .queryParam("cur2", cur2)
                      .queryParam("issuer2", issuer2)
                      .request()
                      .get(WalletCommData.class);  
    return info;
  }
  
  private String getSign(String str){
    logger.info("md5 before =" + str);
    try {
      byte[] bytesOfMessage = str.getBytes("UTF-8");
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] md5 = md.digest(bytesOfMessage);
      BigInteger bigInt = new BigInteger(1,md5);
      String hashtext = bigInt.toString(16);
      return hashtext;
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return "";
  }
  

  private static Client initializeClient(RadarConfiguration radarfiguration)
  {
    return ClientBuilder.newBuilder()
                        // .register(HttpAuthenticationFeature.basic("iradar", radarfiguration.getReplicationPassword().getBytes()))
                        .build();
  }

}
