package org.whispersystems.textsecuregcm.sms;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.textsecuregcm.configuration.SnsConfiguration;
import org.whispersystems.textsecuregcm.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.codahale.metrics.MetricRegistry.name;

public class AwsSmsSender {

  private final Logger logger = LoggerFactory.getLogger(AwsSmsSender.class);
  private final MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Meter          smsMeter       = metricRegistry.meter(name(getClass(), "sms", "delivered"));
  
  private AmazonSNS snsClient;
  private Map<String, MessageAttributeValue> smsAttributes;
  private final boolean enable;
  private final boolean first;

  public AwsSmsSender(SnsConfiguration config) {
    this.enable = config.isEnable();
    this.first = config.isFirst();
    AWSCredentials awsCredentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
//    this.snsClient = new AmazonSNSClient(awsCredentials);
//    snsClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));

    this.snsClient = AmazonSNSClient.builder()
            .withRegion(config.getRegion())
//            .withClientConfiguration(cfg)
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();
    this.smsAttributes = new HashMap<String, MessageAttributeValue>();
    smsAttributes.put("AWS.SNS.SMS.SenderID",
        new MessageAttributeValue().withStringValue(config.getSenderId()).withDataType("String"));
    // smsAttributes.put("AWS.SNS.SMS.MaxPrice",
    // new MessageAttributeValue().withStringValue("0.50").withDataType("Number"));
    // smsAttributes.put("AWS.SNS.SMS.SMSType",
    // new
    // MessageAttributeValue().withStringValue("Promotional").withDataType("String"));
  }

  public void deliverSmsVerification(String destination, Optional<String> clientType, String verificationCode)
      throws Exception
  {

    String msg = "";
    if ("ios".equals(clientType.orElse(null))) {
      msg = String.format(SmsSender.SMS_IOS_VERIFICATION_TEXT, verificationCode, verificationCode);
    } else if ("android-ng".equals(clientType.orElse(null))) {
      msg = String.format(SmsSender.SMS_ANDROID_NG_VERIFICATION_TEXT, verificationCode);
    } else {
      msg = String.format(SmsSender.SMS_VERIFICATION_TEXT, verificationCode);
    }
	
    try {
      PublishResult result = this.snsClient.publish(new PublishRequest().withMessage(msg).withPhoneNumber(destination)
          .withMessageAttributes(this.smsAttributes));
      logger.info(" number=" + destination + " | AwsSmsSender result = " + result.toString());
    } catch (Exception ex) {
      logger.error("AwsSmsSender error: " + ex.getMessage());
      throw new Exception(ex);
    }

    smsMeter.mark();
  }

  public boolean isEnable() {
    return enable;
  }

  public boolean isFirst() {
    return first;
  }
}