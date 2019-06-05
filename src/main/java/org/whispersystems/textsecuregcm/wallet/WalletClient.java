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

  /**
   * 3.7 绑定手机 
   * sign: MD5(address + phoneCode + phoneNumber + timestamp + signKey)
   * @param address 钱包地址
   * @param phoneCode 手机区号
   * @param phoneNumber 手机号码
   * @param password 密码，可能是支付密码/GA。 根据用户安全设置来
   * @return
   */
  public WalletCommData mobileBind(String address,String phoneCode,String phoneNumber,String password){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%s%d%s", address,phoneCode,phoneNumber,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/mobile_bind")
                      .queryParam("address", address)
                      .queryParam("phoneCode", phoneCode)
                      .queryParam("phoneNumber", phoneNumber)
                      .queryParam("password", password)
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

  /**
   * 3.12 添加信任
   * 说明:添加信任需要消耗 0.001VRP 作为手续费，余额不足会失败
   * sign: MD5(address + currency + issuer + timestamp + signKey)
   * @param address 钱包地址
   * @param currency 货币(比如说 CNY)
   * @param password 校验密码，手机短信/GA。 根据用户安全设置来 
   * @param issuer 网关地址(VRP 或者 VBC 不需要信任，系统默认) 
   * @return 交易 hash
   */
  public WalletCommData trustSet(String address,String currency,String password,String issuer){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%s%d%s", address,currency,issuer,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/trust_set")
                      .queryParam("address", address)
                      .queryParam("currency", currency)
                      .queryParam("password", password)
                      .queryParam("issuer", issuer)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
   * 3.13 汇率 
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
  

  /**
   * 3.14 获取充值地址
   * @param accountName 昵称/钱包地址/邮箱
   * @param currency 货币(当前支持:BTC、LTC、ETH、 XRP、BCH)
   * @return
   */
  public WalletCommData depositAddress(String accountName,String currency) {
    WalletCommData info = client
                      .target(radarUrl)
                      .path("/api/im/deposit_address")
                      .queryParam("accountName", accountName)
                      .queryParam("currency", currency)
                      .request()
                      .get(WalletCommData.class);  
    return info;
  }

  /**
   * 3.15 登陆(第一步) 登陆第一步，验证登陆密码
   * sign: MD5(nick + timestamp + signKey)
   * @param nick 用户昵称
   * @param password 登陆密码
   * @return 
   * "result": {
   *    "loginToken": "b73786f8e65c21e6ec017906ef59527ee0f358e32d85f25a1fc2e3d394302b27",  //登陆 token，供登陆第二步使用(只能用一次)
   *    "verifyType": 1 //登陆第二步验证方式， 1--支付密码 2--google auth 3--短信(若是短信，系统会自动往用户手机下发短信) 
   * },
   * "status": "success" }
   */
  public WalletCommData login(String nick,String password){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%d%s", nick,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/login")
                      .queryParam("nick", nick)
                      .queryParam("password", password)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
   * 3.16 登陆验证(第二步) 根据第一步 login 接口返回的 token 以及验证方式，进行登陆校验
   * o sign: MD5(token + timestamp + signKey)
   * @param token 登陆 token(登陆第一步返回的) 
   * @param password 校验密码(根据第一步的返回判断校验方式，若是短信不需要 手动申请，login 接口会自动下发)
   * @return {
              "result": {
              "address": "rEz8g4eHtb1tp83q5b5TtDgD29vctE6jEr", //钱包地址
              "mobile" : "86/12222112212" //手机号,若用户绑定了手机号有改返回 },
              "status": "success" 
            }
   */
  public WalletCommData loginConfirm(String token,String password){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%d%s", token,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/login_confirm")
                      .queryParam("token", token)
                      .queryParam("password", password)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }


  /**
   * 3.17 忘记密码 改接口用于找回密码第一步:申请邮箱或者手机验证码
   * sign: MD5(nick + timestamp + signKey)
 返回结果 
   * @param nick 用户昵称
   * @param flag 找回密码类型，0: 登陆密码 1–支付密码
   * @param recoveryWay 找回方式，0: 邮箱 1:手机(支付密码不能用邮箱找回)
   * @return {
              "result": {
              "email": "tes***2@163.com", //邮箱找回，返回邮箱，带* "mobile": "132****2122" //手机找回，返回手机,带*
              },
              "status": "success" 
            }
   */
  public WalletCommData forgotPassword(String nick,String flag,String recoveryWay){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%d%s", nick,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/forgot_password")
                      .queryParam("nick", nick)
                      .queryParam("flag", flag)
                      .queryParam("recoveryWay", recoveryWay)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  // 

  /**
   * 3.18 重置密码 使用说明:改接口用于找回密码第二步，google auth 不用调用 forgot_password 
   * sign: MD5(nick + timestamp + signKey)
   * @param nick 用户昵称
   * @param flag 找回密码类型，0: 登陆密码 1–支付密码
   * @param recoveryWay 找回方式，0: 邮箱(支付密码不能用邮箱找回) 1:手机 2:google auth
   * @param password 校验密码(邮箱密码或者手机或者 google) 
   * @param newPassword : 新密码
   * @return { "status": "success" }
   */
  public WalletCommData resetPassword(String nick,String flag,String recoveryWay,String password,String newPassword){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%d%s", nick,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/reset_password")
                      .queryParam("nick", nick)
                      .queryParam("flag", flag)
                      .queryParam("recoveryWay", recoveryWay)
                      .queryParam("password", password)
                      .queryParam("newPassword", newPassword)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
   * 3.19 获取当前提现地址
   * @param accountName 昵称/钱包地址/邮箱
   * @param currency 货币(当前支持:BTC、LTC、ETH、 XRP、BCH)
   * @return
   *    {
          "result": {
          "address": "rUKX9spSaLModnZm6zyMB63eCa8VpJQSs"
          },
          "status": "success" 
        }
   */
  public WalletCommData withdrawAddress(String accountName,String currency) {
    WalletCommData info = client
                      .target(radarUrl)
                      .path("/api/im/withdraw_address")
                      .queryParam("accountName", accountName)
                      .queryParam("currency", currency)
                      .request()
                      .get(WalletCommData.class);  
    return info;
  }

  /**
   * 3.20 新建/更新提现地址
   * @param accountName 昵称/钱包地址/邮箱
   * @param address 新的提现地址
   * @param currency 货币(当前支持:BTC、LTC、ETH、XRP、BCH) 
   * @param password 校验密码(支付密码/GA/短信)
   * @return { "status": "success" }
   */
  public WalletCommData withdrawUpdate(String accountName,String address,String currency,String password){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%s%d%s", accountName,address,currency,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/withdraw_update")
                      .queryParam("accountName", accountName)
                      .queryParam("address", address)
                      .queryParam("currency", currency)
                      .queryParam("password", password)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  /**
   * 3.21 提交提现申请
   * sign:MD5(accountName+amount+currency+timestamp+signKey) 
   * @param accountName 昵称/钱包地址/邮箱
   * @param currency 货币(当前支持:BTC、LTC、ETH、XRP、BCH) 
   * @param password 校验密码(支付密码/GA/短信)
   * @param amount 提现金额
   * @param dt 提现地址 memo(XRP 选填参数,必须是数字)
   * @return { "status": "success" }
   */
  public WalletCommData withdrawMake(String accountName,String currency,String password,String amount,String dt){
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    long longStamp = timestamp.getTime();
    String sign = getSign(String.format("%s%s%s%d%s", accountName,amount,currency,longStamp,this.md5Key));
    Response response = client
                      .target(radarUrl)
                      .path("/api/im/withdraw_make")
                      .queryParam("accountName", accountName)
                      .queryParam("amount", amount)
                      .queryParam("currency", currency)
                      .queryParam("password", password)
                      .queryParam("dt", dt)
                      .queryParam("timestamp", longStamp)
                      .queryParam("sign", sign)
                      .request()
                      .post(null);
    return response.readEntity(WalletCommData.class);
  }

  private String getSign(String str){
    // logger.info("md5 before =" + str);
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
