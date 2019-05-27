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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.textsecuregcm.configuration.RadarConfiguration;
import org.whispersystems.textsecuregcm.entities.CoinInfo;

import io.dropwizard.lifecycle.Managed;

public class WalletClientManager implements Managed {
  private final WalletClient client;
  private final Logger logger = LoggerFactory.getLogger(WalletClient.class);
  private final RadarConfiguration config;
  private HashMap<String, CoinInfo> coinMap = new HashMap<String, CoinInfo>();
  private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  public WalletClientManager(WalletClient client, RadarConfiguration config) {
    this.client = client;
    this.config = config;
    setCoinInfos(config.getCoinIcons());
  }

  private void setCoinInfos(List<String> coins) {

    for (String coin : coins) {
      if (Strings.isNullOrEmpty(coin)) {
        logger.error("config errror ... radar-coinIcons ! ");
        continue;
      }
      String[] list = coin.split(",");
      if (list == null || list.length < 6) {
        logger.error("config errror ... radar-coinIcons ! " + coin);
        continue;
      }

      coinMap.put(list[0], new CoinInfo(list[0], list[1], list[2], list[3], list[4], Float.valueOf(list[5])));
    }
  }

  // 3.1 根据手机获取账号列表
  public WalletCommData getAccounts(String phoneCode, String phoneNumber) {
    WalletCommData info = client.getAccounts(phoneCode, phoneNumber);
    if ("success".equals(info.getStatus()) && info.getResult() != null) {
      List accounts = (ArrayList) info.getResult();
      if (accounts == null || accounts.size() == 0) {
        WalletCommData regi = client.register(phoneCode, phoneNumber);
        if ("success".equals(regi.getStatus())) {
          Map map = (LinkedHashMap) regi.getResult();
          accounts.add(String.format("%s,%s", phoneNumber, map.get("address")));
          info.setResult(accounts);
        } else {
          printError(regi, "register api");
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
    if (!"success".equals(info.getStatus())) {
      return info;
    }
    List<LinkedHashMap> list = (ArrayList) info.getResult();
    List<Object> result = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {
      LinkedHashMap<String, Object> map = list.get(i);
      String currency = obj2String(map.get("currency"));
      if (!coinMap.containsKey(currency)) {
        continue;
      }
      LinkedHashMap<String, Object> retMap = new LinkedHashMap<String, Object>();

      retMap.put("balance", map.get("balance"));
      retMap.put("currency", currency);
      retMap.put("rate", coinMap.get(currency).getRate());
      retMap.put("icon", coinMap.get(currency).getIcon());
      retMap.put("issuer", coinMap.get(currency).getIssuer());
      result.add(retMap);
    }
    info.setResult(result);

    return info;
  }

  // 3.5 申请手机短信
  public WalletCommData getSmsCode(String address, String phoneCode, String phoneNumber) {
    return client.getSmsCode(address, phoneCode, phoneNumber);
  }

  //
  public WalletCommData makeTx(String address, String destination, String currency, String amount, String password,
      String issuer) {
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
  public WalletCommData verify(String accountName, String password) {
    return client.verify(accountName, password);
  }

  //
  public String getTxHistory(String address, String marker, String coinType) {
    String str = client.getTxHistory(address, marker).readEntity(String.class);
    // if(!checkTxHis(str)) return str;

    return str;
  }

  private Long formatDate(LinkedTreeMap<String, Object> data)
  {
    Long date = Long.parseLong(Objects.toString(data.get("date"),"0"));
    date = date + 946656000L + 8 * 60 * 60;
    return date;
  }

  
  /**
   * //0:收款;1:付款;2:手续费;3:创建挂单;4:挂单完全成交;5:挂单部分成交;6.取消挂单;100.其他
   * 1. 挂单完全成交    --> 4 挂单完全成交
   * 2. 挂单部分成交    --> 5 挂单部分成交 
   * 3. 创建挂单       --> 3 创建挂单
   * 4. 取消挂单       --> 6 取消挂单
   * 5. 发送          --> 1 付款 
   * 6. 接收          --> 0 收款 
   */
  private List<String> sents = Arrays.asList("active_show", "sent");
  private List<String> recis = Arrays.asList("active_acc", "received");
  private int getType(LinkedTreeMap<String, Object> data,LinkedTreeMap<String, Object> effect){
    
    String type = obj2String(data.get("type"));
    if(recis.contains(type))
    {
      return 0;
    }
    if(sents.contains(type)){
      return 1;
    }
    if(effect != null){
      if("fee".equals(effect.get("type"))) return 2;
    }
    if("offer_cancelled".equals(type)) return 6;
    if("offercreate".equals(type))
    {
      String offerStatus = obj2String(data.get("OfferStatus"));
      if("offer_funded".equals(offerStatus)) return 4;
      if("offer_partially_funded".equals(offerStatus)) return 5;
      if("offer_create".equals(offerStatus)) return 3;
      // 挂单全部成交:offer_funded 
      // 挂单部分成交: offer_partially_funded 
      // 创建挂单:offer_create 
    }

    return 100;
  }
  
  public String getTxHistory2(String address, String marker, String coinType) {
    Response response = client.getTxHistory(address, marker);
    String str = response.readEntity(String.class);
    if(!checkTxHis(str)) return str;

    Gson gson = new Gson();

    TxHistory history = gson.fromJson(str,TxHistory.class);
    List<Object> orders = new ArrayList<>();

    List<Object> transactions = history.getTransactions();
    for (Object obj : transactions) {
      LinkedTreeMap<String, Object> data = (LinkedTreeMap) obj;
      Long date = formatDate(data);
      
      String sender = obj2String(data.get("sender"));
      String recipient = obj2String(data.get("recipient"));
      String otherAddr = history.getAccount().equals(sender)?recipient:sender;
      String hash      = obj2String(data.get("hash"));
      List<LinkedTreeMap<String, Object>> effects = (ArrayList) data.get("effects");
      for (LinkedTreeMap<String, Object> effect : effects) {
        int type = getType(data,effect);
        OrderInfo order = new OrderInfo();
        order.setDate(date);
        order.setType(type);
        order.setHash(hash);
        LinkedTreeMap<String, Object> amountInfo = (LinkedTreeMap) effect.get("amount");
        order.setAmount(obj2String(amountInfo.get("amount")));
        order.setCurrency(obj2String(amountInfo.get("currency")));
        if(Strings.isNullOrEmpty(otherAddr)){
          otherAddr = obj2String(effect.get("issuer"));
        }
        order.setOtherAddr(otherAddr);

        orders.add(order);
      }
    }

    history.setTransactions(orders);

    CommResp resp = new CommResp("success", history);

    return gson.toJson(resp);
  }

  private String obj2String (Object obj){

    return Objects.toString(obj,"");
  }

  private boolean checkTxHis(String str)
  {
    if(Strings.isNullOrEmpty(str))
    {
      logger.error("getTxHistory2x error resp is empty");
      return false;
    }
    if (str.contains("error_code")) {
      logger.error("getTxHistory2x error resp =" + str);
      return false;
    }
    return true;
  }

  //
  public WalletCommData getChartsLatest(String cur1, String issuer1, String cur2, String issuer2) {
    return client.getChartsLatest(cur1, issuer1, cur2, issuer2);
  }

  private void printError(WalletCommData info, String name) {

    logger.error(String.format("%s:status=%s,date=%s", name, info.getStatus(), info.getData().toString()));
  }

  private void updateRates() {

    Float cnyRate = getRate("CNY", coinMap.get("CNY").getIssuer(), "USD", coinMap.get("USD").getIssuer());
    cnyRate = cnyRate == 0f ? 6.95f : cnyRate;
    for (Map.Entry<String, CoinInfo> entry : coinMap.entrySet()) {
      CoinInfo info = entry.getValue();

      Float rate = getRate(info.getTargetCurrency(), info.getTargetIssuer(), info.getCurrency(), info.getIssuer());
      if ("CNY".equals(info.getTargetCurrency())) {// 人民币换算成美元
        rate = rate / cnyRate;
      }
      info.setRate(rate);
    }

  }

  /**
   * //价格默认以currency2位目标货币,currency1为价格货币.例如获取VBC价格,需要将CNY放在第一位,VBC放在第二位
   * @param targetCur
   * @param targetIssuer
   * @param cur
   * @param issuer
   * @return
   */
  private Float getRate(String targetCur, String targetIssuer, String cur, String issuer) {
    WalletCommData rateInfo = client.getChartsLatest(targetCur, targetIssuer, cur, issuer);
    if ("success".equals(rateInfo.getStatus())) {
      Map rateMap = (LinkedHashMap) rateInfo.getResult();
      Float rate = Float.parseFloat(Objects.toString(rateMap.get("value"),"0"));
      return rate;
    } else {
      printError(rateInfo, String.format("getRate api %s,%s,%s,%s", targetCur, targetIssuer, cur, issuer));
    }
    return 0.0f;
  }

  @Override
  public void start() throws Exception {
    long delay = 1000L;
    long period = 1000 * 10L;
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
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
