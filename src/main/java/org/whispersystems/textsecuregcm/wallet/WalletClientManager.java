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

public class WalletClientManager {
  private final WalletClient client;

  public WalletClientManager(WalletClient client) {
    this.client = client;
  }

  // 3.1 根据手机获取账号列表
  public String getAccounts(String phoneCode, String phoneNumber) {
    return client.getAccounts(phoneCode, phoneNumber);
  }

  // 3.2 根据昵称/email/钱包查询账号
  public String getWalletinfo(String accountName) {
    return client.getWalletinfo(accountName);
  }

  // 3.3 注册
  public String register(String phoneCode, String phoneNumber) {
    return client.register(phoneCode, phoneNumber);
  }

  // 3.4 用户余额
  public String getBalance(String addr) {
    return client.getBalance(addr);
  }

  // 3.5 申请手机短信
  public String getSmsCode(String address,String phoneCode,String phoneNumber) {
    return client.getSmsCode(address, phoneCode, phoneNumber);
  }
  
  //
  public String makeTx(String address,String destination,String currency,String amount,String password,String issuer){
    return client.makeTx(address, destination, currency, amount, password, issuer);
  }

  //
  public String setGoogleAuth(String address, int flag, String password) {
    return client.setGoogleAuth(address, flag, password);
  }

  //
  public String confirmGoogleAuth(String address, String verifyCode) {
    return client.confirmGoogleAuth(address, verifyCode);
  }

  //
  public String getTxHistory(String address,String marker) {
    return client.getTxHistory(address,marker);
  }

  //
  public String getChartsLatest(String cur1,String issuer1,String cur2,String issuer2) {
    return client.getChartsLatest(cur1, issuer1, cur2, issuer2);
  }

}
