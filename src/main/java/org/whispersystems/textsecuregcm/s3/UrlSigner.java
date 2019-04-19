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
package org.whispersystems.textsecuregcm.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.whispersystems.textsecuregcm.configuration.AttachmentsConfiguration;

import java.net.URL;
import java.util.Date;

public class UrlSigner {

  private static final long DURATION = 60 * 60 * 1000;

  // private final AWSCredentials credentials;
  // private final String bucket;
  private final String bucketName;
  private final String clientRegion;
  private final String accessKey;
  private final String accessSecret;

  public UrlSigner(AttachmentsConfiguration config) {
    // this.credentials = new BasicAWSCredentials(config.getAccessKey(), config.getAccessSecret());
    // this.bucket = config.getBucket();
    this.clientRegion = config.getRegion();//"*** Client region ***";
    this.bucketName = config.getBucket();// "*** Bucket name ***";
    this.accessKey = config.getAccessKey();// "*** Source object key *** ";
    this.accessSecret = config.getAccessSecret();// "*** Destination object key ***";
  }

  public URL getPreSignedUrl(long attachmentId, HttpMethod method, boolean unaccelerated) {
    // AmazonS3                    client  = new AmazonS3Client(credentials);
    GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, String.valueOf(attachmentId), method);
    // AmazonS3 client = new AmazonS3Client(new BasicAWSCredentials(this.accessKey,this.accessSecret));
    AmazonS3 client = AmazonS3ClientBuilder.standard()
            .withRegion(clientRegion)
            .withCredentials(new ProfileCredentialsProvider())
            .build();  
    client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1)); // 此处根据自己的 s3 地区位置改变
    
    
    request.setExpiration(new Date(System.currentTimeMillis() + DURATION));
    request.setContentType("application/octet-stream");

    if (unaccelerated) {
      client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).build());
    } else {
      client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
    }

    return client.generatePresignedUrl(request);
  }

}
