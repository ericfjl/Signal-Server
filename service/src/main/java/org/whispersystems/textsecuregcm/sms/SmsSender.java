/**
 * Copyright (C) 2013 Open WhisperSystems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.whispersystems.textsecuregcm.sms;


import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.textsecuregcm.util.Util;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SmsSender {

  private final Logger logger = LoggerFactory.getLogger(SmsSender.class);
  // static final String SMS_IOS_VERIFICATION_TEXT        = "Your radar verification code: %s\n\nOr tap: radar://verify/%s";
  static final String SMS_IOS_VERIFICATION_TEXT        = "Your radar verification code: %s\n\nOr tap: radar://verify/%s";
  static final String SMS_ANDROID_NG_VERIFICATION_TEXT = "<#> Your radar verification code: %s\n\ndoDiFGKPO1r";
  static final String SMS_VERIFICATION_TEXT            = "Your radar verification code: %s";



  private final TwilioSmsSender twilioSender;
  private final AwsSmsSender awsSender;

  public SmsSender(TwilioSmsSender twilioSender, AwsSmsSender awsSender)
  {
    this.twilioSender = twilioSender;
    this.awsSender = awsSender;
  }

  public void deliverSmsVerification(String destination, Optional<String> clientType, String verificationCode, Util.Sms sms) {
    // Fix up mexico numbers to 'mobile' format just for SMS delivery.
    if (destination.startsWith("+52") && !destination.startsWith("+521")) {
      destination = "+521" + destination.substring(3);
    }

    try {
      switch (sms){
        case TWILIO:
          twilioSender.deliverSmsVerification(destination, clientType, verificationCode);
          break;
        case AWS:
        default:
          awsSender.deliverSmsVerification(destination, clientType, verificationCode);
          break;
      }
    } catch (Exception e) {
      logger.info("SMS Failed: " + e.getMessage());
    }
  }

  public Optional<Util.Sms> getDefaultSms(){
    if (awsSender.isFirst()){
      logger.info("defaultSms:AWS");
      return Optional.of(Util.Sms.AWS);
    }else {
      logger.info("defaultSms:TWILIO");
      return Optional.of(Util.Sms.TWILIO);
    }
  }

  public void deliverVoxVerification(String destination, String verificationCode, Optional<String> locale) {
    twilioSender.deliverVoxVerification(destination, verificationCode, locale);
  }

  public void sendSMSMessage(AmazonSNSClient snsClient, String message, String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                        .withMessage(message)
                        .withPhoneNumber(phoneNumber)
                        .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
  }
}
