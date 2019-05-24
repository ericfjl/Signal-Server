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
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.textsecuregcm.configuration.RadarConfiguration;
import org.whispersystems.textsecuregcm.entities.CoinInfo;

import io.dropwizard.lifecycle.Managed;

public class WalletClientManager implements Managed{
  private final WalletClient client;
  private final Logger logger = LoggerFactory.getLogger(WalletClient.class);
  private final RadarConfiguration config;
  private HashMap<String,CoinInfo> coinMap = new HashMap<String,CoinInfo>();
  private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  public WalletClientManager(WalletClient client,RadarConfiguration config) {
    this.client = client;
    this.config = config;
    setCoinInfos(config.getCoinIcons());
  }

  private void setCoinInfos(List<String> coins){

    for (String coin : coins) {
      if(Strings.isNullOrEmpty(coin))
      {
        logger.error("config errror ... radar-coinIcons ! ");
        continue;
      }
      String[] list = coin.split(",");
      if(list == null || list.length <6){
        logger.error("config errror ... radar-coinIcons ! " + coin);
        continue;
      }

      coinMap.put(list[0], new CoinInfo(list[0], list[1], list[2],list[3],list[4],Float.valueOf(list[5])));
    }
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
    List<LinkedHashMap> list = (ArrayList) info.getResult();
    List<Object> result = new ArrayList<>();
    
    for (int i =0;i<list.size();i++){
      LinkedHashMap<String,Object> map = list.get(i);
      String currency = String.valueOf(map.get("currency"));
      if(!coinMap.containsKey(currency)){
        continue;
      }
      LinkedHashMap<String,Object> retMap = new LinkedHashMap<String,Object>();

      retMap.put("balance", map.get("balance"));
      retMap.put("currency", currency);
      retMap.put("rate", coinMap.get(currency).getRate());
      retMap.put("icon",coinMap.get(currency).getIcon());
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

  private void updateRates(){

    Float cnyRate = getRate("CNY",coinMap.get("CNY").getIssuer(),"USD", coinMap.get("USD").getIssuer());
    for(Map.Entry<String, CoinInfo> entry : coinMap.entrySet()) {
      String key = entry.getKey();
      CoinInfo info = entry.getValue();

      Float rate = getRate(info.getTargetCurrency(),info.getTargetIssuer(),info.getCurrency(), info.getIssuer());
      if("CNY".equals(info.getTargetCurrency())){//人民币换算成美元
        rate = rate/info.getTargetRate();
      }
      info.setRate(rate);
    }
    
  }

  private Float getRate(String targetCur,String targetIssuer,String cur,String issuer){
    WalletCommData rateInfo = client.getChartsLatest(targetCur,targetIssuer,cur,issuer);
    if("success".equals(rateInfo.getStatus())){
      Map rateMap = (LinkedHashMap) rateInfo.getResult();
      Float rate = Float.parseFloat(String.valueOf(rateMap.get("value")));
      return rate;
    }else{
      printError(rateInfo,String.format("getRate api %s,%s,%s,%s",targetCur, targetIssuer, cur, issuer));
    }
    return 0.0f;
  }

  @Override
  public void start() throws Exception {
    long delay  = 1000L;
    long period = 1000 * 10L;
    TimerTask task = new TimerTask(){
      int count =0;
      @Override
      public void run() {
        // logger.info(String.format("===== TimerTask =====%d", count++));
        updateRates();
      }
    };

    executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    Thread.sleep(delay + period * 3);
  }

  @Override
  public void stop() throws Exception {
    executor.shutdown();
  }

}
