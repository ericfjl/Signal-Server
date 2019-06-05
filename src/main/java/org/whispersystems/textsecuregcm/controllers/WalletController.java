package org.whispersystems.textsecuregcm.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

import org.assertj.core.internal.bytebuddy.implementation.bytecode.Throw;
import org.whispersystems.textsecuregcm.entities.ProvisioningMessage;
import org.whispersystems.textsecuregcm.storage.Account;
import org.whispersystems.textsecuregcm.wallet.WalletClientManager;
import org.whispersystems.textsecuregcm.wallet.WalletCommData;

import io.dropwizard.auth.Auth;

@Path("/v1/wallet/")
public class WalletController {

  private final WalletClientManager manager;

  public WalletController(WalletClientManager manager) {
    this.manager = manager;
  }

  // 3.1 根据手机获取账号列表
  @Timed
  @GET
  @Path("account_list/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData getAccountList(
                                //  @Auth                            Account account,
                                 @QueryParam("phoneCode")         String phoneCode,
                                 @QueryParam("phoneNumber")       String phoneNumber,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.getAccounts(phoneCode,phoneNumber);
  }

  // 3.2 根据昵称/email/钱包查询账号
  @Timed
  @GET
  @Path("walletinfo/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData getWalletInfo(
                                //  @Auth                            Account account,
                                 @QueryParam("accountName")         String accountName,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.getWalletinfo(accountName);
  }

  // 3.3 注册
  @Timed
  @POST
  @Path("register/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData register(
                                //  @Auth                            Account account,
                                 @QueryParam("phoneCode")         String phoneCode,
                                 @QueryParam("phoneNumber")       String phoneNumber,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.register(phoneCode,phoneNumber);
  }

  // 3.4 用户余额
  @Timed
  @GET
  @Path("balance/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData getBalance(
                                //  @Auth                            Account account,
                                 @QueryParam("address")         String address,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.getBalance(address);
  }

  // 3.5 申请手机短信
  @Timed
  @POST
  @Path("sms_code/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData getSmsCode(
                                //  @Auth                            Account account,
                                 @QueryParam("address")         String address,
                                 @QueryParam("phoneCode")       String phoneCode,
                                 @QueryParam("phoneNumber")     String phoneNumber,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.getSmsCode(address, phoneCode, phoneNumber);
  }

  // 3.6 转账
  @Timed
  @POST
  @Path("make_tx/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData makeTx(
                                //  @Auth                            Account account,
                                 @QueryParam("address")           String address,
                                 @QueryParam("destination")       String destination,
                                 @QueryParam("currency")          String currency,
                                 @QueryParam("amount")            String amount,
                                 @QueryParam("password")          String password,
                                 @QueryParam("issuer")            String issuer,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.makeTx(address, destination, currency, amount, password, issuer);
  }
  // 3.7 绑定手机
  @Timed
  @POST
  @Path("mobile_bind/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData mobileBind(
                                //  @Auth                            Account account,
                                 @QueryParam("address")           String address,
                                 @QueryParam("phoneCode")         String phoneCode,
                                 @QueryParam("phoneNumber")       String phoneNumber,
                                 @QueryParam("password")          String password,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.mobileBind(address, phoneCode, phoneNumber, password);
  }

  // 3.8 获取 google_auth 密钥(开启 google auth 第一步)
  @Timed
  @POST
  @Path("set_google_auth/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData setGoogleAuth(
                                //  @Auth                            Account account,
                                 @QueryParam("address")         String address,
                                 @QueryParam("flag")            int flag,
                                 @QueryParam("password")        String password,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // if (!account.getAuthenticatedDevice().isPresent()) throw new AssertionError();
    return manager.setGoogleAuth(address,flag,password);
  }

  // 3.9 确认开启 google_auth(开启 google auth 第二步:确认) , 
  @Timed
  @POST
  @Path("confirm_google_auth/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData confirmGoogleAuth(
                                //  @Auth                          Account account,
                                 @QueryParam("address")         String address,
                                 @QueryParam("verifyCode")      String verifyCode,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account); 
    return manager.confirmGoogleAuth(address,verifyCode);
  }


  // 3.10 验证用户权限(绑定用户的时候使用)
  @Timed
  @GET
  @Path("verify/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData verfy(
                                //  @Auth                          Account account,
                                 @QueryParam("accountName")     String accountName,
                                 @QueryParam("password")        String password,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account); 
    return manager.verify(accountName, password);
  }

  // 3.11 交易历史
  @Timed
  @GET
  @Path("tx_history/")
  @Produces(MediaType.APPLICATION_JSON)
  public String getTxHistory(
                                //  @Auth                            Account account,
                                 @QueryParam("address")         String address,
                                 @QueryParam("marker")          String marker,
                                 @QueryParam("coinType")        String coinType,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);  
    return manager.getTxHistory(address,marker,coinType);
  }

  // 
  // 3.12 汇率接口
  @Timed
  @GET
  @Path("charts/latest/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData getChatsLatest(
                                //  @Auth                            Account account,
                                 @QueryParam("cur1")             String cur1,
                                 @QueryParam("issuer1")          String issuer1,
                                 @QueryParam("cur2")             String cur2,
                                 @QueryParam("issuer2")          String issuer2,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);  
    return manager.getChartsLatest(cur1, issuer1, cur2, issuer2);
  }

  // getTxHistory2
  // 3.11-2 交易历史2
  @Timed
  @GET
  @Path("tx_history2/")
  @Produces(MediaType.APPLICATION_JSON)
  public String getTxHistory2(
                                //  @Auth                            Account account,
                                 @QueryParam("address")         String address,
                                 @QueryParam("marker")          String marker,
                                 @QueryParam("coinType")        String coinType,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);  
    return manager.getTxHistory2(address,marker,coinType);
  }

  // 3.12
  @Timed
  @POST
  @Path("trust_set/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData trustSet(
                                //  @Auth                          Account account,
                                 @QueryParam("address")         String address,
                                 @QueryParam("currency")        String currency,
                                 @QueryParam("password")        String password,
                                 @QueryParam("issuer")          String issuer,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account); 
    return manager.trustSet(address, currency, password, issuer);
  }

  // 3.14 获取充值地址
  @Timed
  @GET
  @Path("deposit_address/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData depositAddress(
                                //  @Auth                          Optional<Account> account,
                                 @QueryParam("accountName")     String accountName,
                                 @QueryParam("currency")        String currency,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);  
    return manager.depositAddress(accountName, currency);
  }

  @Timed
  @GET
  @Path("deposit_address2/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData depositAddress2(
                                 @Auth                          Optional<Account> account,
                                 @QueryParam("accountName")     String accountName,
                                 @QueryParam("currency")        String currency,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    checkAuth(account);  
    return manager.depositAddress(accountName, currency);
  }


  // 3.15 登陆(第一步)
  @Timed
  @POST
  @Path("login/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData login(
                                //  @Auth                            Account account,
                                 @QueryParam("nick")              String nick,
                                 @QueryParam("password")          String password,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.login(nick, password);
  }
  

  // 3.16 登陆验证(第二步) 根据第一步 login 接口返回的 token 以及验证方式，进行登陆校验
  @Timed
  @POST
  @Path("login_confirm/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData loginConfirm(
                                //  @Auth                            Account account,
                                 @QueryParam("token")              String token,
                                 @QueryParam("password")          String password,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.loginConfirm(token, password);
  }

  // 3.17 忘记密码 改接口用于找回密码第一步:申请邮箱或者手机验证码
  @Timed
  @POST
  @Path("forgot_password/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData forgotPassword(
                                //  @Auth                            Account account,
                                 @QueryParam("nick")              String nick,
                                 @QueryParam("flag")              String flag,
                                 @QueryParam("recoveryWay")       String recoveryWay,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.forgotPassword(nick, flag, recoveryWay);
  }

  // 3.18 重置密码 使用说明:改接口用于找回密码第二步，google auth 不用调用 forgot_password 
  @Timed
  @POST
  @Path("reset_password/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData resetPassword(
                                //  @Auth                            Account account,
                                 @QueryParam("nick")              String nick,
                                 @QueryParam("flag")              String flag,
                                 @QueryParam("recoveryWay")       String recoveryWay,
                                 @QueryParam("password")          String password,
                                 @QueryParam("newPassword")          String newPassword,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.resetPassword(nick, flag, recoveryWay, password, newPassword);
  }

  //3.19 获取当前提现地址
  @Timed
  @GET
  @Path("withdraw_address/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData withdrawAddress(
                                //  @Auth                            Account account,
                                 @QueryParam("accountName")     String accountName,
                                 @QueryParam("currency")        String currency,
                                 @Valid                         ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);  
    return manager.withdrawAddress(accountName, currency);
  }

  // 3.20 新建/更新提现地址
  @Timed
  @POST
  @Path("withdraw_update/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData withdrawUpdate(
                                //  @Auth                            Account account,
                                 @QueryParam("accountName")       String accountName,
                                 @QueryParam("address")           String address,
                                 @QueryParam("currency")          String currency,
                                 @QueryParam("password")          String password,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.withdrawUpdate(accountName, address, currency, password);
  }

  //3.21 提交提现申请
  @Timed
  @POST
  @Path("withdraw_make/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData withdrawMake(
                                //  @Auth                            Account account,
                                 @QueryParam("accountName")       String accountName,
                                 @QueryParam("currency")          String currency,
                                 @QueryParam("password")          String password,
                                 @QueryParam("amount")            String amount,
                                 @QueryParam("dt")                int dt,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.withdrawMake(accountName, currency, password, amount, dt);
  }


  //3.21-2 提交提现申请2
  @Timed
  @POST
  @Path("withdraw_make2/")
  @Produces(MediaType.APPLICATION_JSON)
  public WalletCommData withdrawMake2(
                                //  @Auth                            Account account,
                                 @QueryParam("accountName")       String accountName,
                                 @QueryParam("currency")          String currency,
                                 @QueryParam("address")           String address,
                                 @QueryParam("password")          String password,
                                 @QueryParam("amount")            String amount,
                                 @QueryParam("dt")                int dt,
                                 @Valid                           ProvisioningMessage message
                                )
      throws IOException
  {
    // checkAuth(account);
    return manager.withdrawMake2(accountName, currency, password, amount, dt, address);
  }

  private void checkAuth(Optional<Account> account)throws IOException{

    
    if (!account.isPresent() ) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
  }

}
