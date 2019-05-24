/**
 * Copyright (C) 2018 Open WhisperSystems
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.whispersystems.textsecuregcm.wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalletClientManager {
  private final WalletClient client;
  private final Logger logger = LoggerFactory.getLogger(WalletClient.class);
  Map hMap = new HashMap();
  private final String cur1;
  private final String issuer1;
  // List coins = new ArrayList<>();

  public WalletClientManager(WalletClient client) {
    this.client = client;
    hMap.put("VRP", "https://cdn.iradar.org/icon_rad.png");
    cur1 = "USD";
    issuer1 = "rH4XBcbU8aYkCTiiRBpmhjXHpKABHbqUKz";

    // coins.add("VRP");
  }

  // 3.1 根据手机获取账号列表
  public WalletCommData getAccounts(String phoneCode, String phoneNumber) {
    WalletCommData info = client.getAccounts(phoneCode, phoneNumber);
    if("success".equals(info.getStatus()) && info.getResult() != null){
      List accounts = (ArrayList)info.getResult();
      if(accounts == null || accounts.size() ==0)
      {
        WalletCommData regi = client.register(phoneCode, phoneNumber);
        if("success".equals(regi.getStatus())){
          Map map = (LinkedHashMap)regi.getResult();
          accounts.add(String.format("%s,%s", phoneNumber,map.get("address")));
          info.setResult(accounts);
        }else{
          printError(regi,"register api");
        }
      }
    }
    return info;
  }

  // 3.2 根据昵称/email/钱包查询账号
  public WalletCommData getWalletinfo(String accountName) {


    // String rate = client.getChartsLatest(cur1, issuer1, cur2, issuer2);
    return client.getWalletinfo(accountName);
  }

  // 3.3 注册
  public WalletCommData register(String phoneCode, String phoneNumber) {
    return client.register(phoneCode, phoneNumber);
  }

  // 3.4 用户余额
  public WalletCommData getBalance(String addr) {
    WalletCommData info = client.getBalance(addr);
    if(!"success".equals(info.getStatus())){
      return info;
    }
    List list = (ArrayList) info.getResult();
    List result = new ArrayList<>();
    
    for (int i =0;i<list.size();i++){
      Map map = (LinkedHashMap) list.get(i);
      String currency = map.get("currency").toString();
      if(!hMap.containsKey(currency)){
        continue;
      }
      Map retMap = new LinkedHashMap();
      String issuer2 = "RADR";
      if(!"VRP".equals(currency) && !"VBC".equals(currency))
      {
        issuer2 = String.valueOf(map.get("issuer"));
      }
      WalletCommData rateInfo = client.getChartsLatest(cur1, issuer1, currency, issuer2);
      String rate = "0.0";
      if("success".equals(rateInfo.getStatus())){
        Map rateMap = (LinkedHashMap) rateInfo.getResult();
        rate =  String.valueOf(rateMap.get("value"));
      }else{
        printError(rateInfo,"getChartsLatest api");
      }

      retMap.put("balance", map.get("balance"));
      retMap.put("currency", currency);
      retMap.put("rate", rate);
      retMap.put("icon",hMap.get(currency));
      result.add(retMap);
      
    }
    info.setResult(result);
    
    return info;
  }

  // 3.5 申请手机短信
  public WalletCommData getSmsCode(String address,String phoneCode,String phoneNumber) {
    return client.getSmsCode(address, phoneCode, phoneNumber);
  }
  
  //
  public WalletCommData makeTx(String address,String destination,String currency,String amount,String password,String issuer){
    return client.makeTx(address, destination, currency, amount, password, issuer);
  }

  //
  public WalletCommData setGoogleAuth(String address, int flag, String password) {
    return client.setGoogleAuth(address, flag, password);
  }

  //
  public WalletCommData confirmGoogleAuth(String address, String verifyCode) {
    return client.confirmGoogleAuth(address, verifyCode);
  }

  //
  public String getTxHistory(String address,String marker,String coinType) {
    String str = client.getTxHistory(address,marker);
    if(!Strings.isNullOrEmpty(str) && str.contains("error_code")){
      logger.error("getTxHistory error resp =" + str);
      return str;
    }
    if(Strings.isNullOrEmpty(coinType))
    {
      return str;
    }

    // amount
    
    return str;
  }

  //
  public WalletCommData getChartsLatest(String cur1,String issuer1,String cur2,String issuer2) {
    return client.getChartsLatest(cur1, issuer1, cur2, issuer2);
  }


  private void printError(WalletCommData info,String name){

    logger.error(String.format("%s:status=%s,date=%s",name, info.getStatus(),info.getData().toString()));
  }

}
