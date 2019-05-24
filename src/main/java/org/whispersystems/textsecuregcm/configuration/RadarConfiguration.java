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
package org.whispersystems.textsecuregcm.configuration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;


public class RadarConfiguration {

  @NotEmpty
  @JsonProperty
  private String url;

  public String getUrl() {
    return url;
  }
  
  @NotEmpty
  @JsonProperty
  private String md5key;

  public String getMd5Key() {
    return md5key;
  }

  @JsonProperty
  @NotEmpty
  private List<String> coinIcons;

  public List<String> getCoinIcons(){
    return coinIcons;
  }

  @NotEmpty
  @JsonProperty
  private String targetCur1;

  public String getTargetCur1() {
    return targetCur1;
  }


  @NotEmpty
  @JsonProperty
  private String targetIssuer1;

  public String getTargetIssuer1() {
    return targetIssuer1;
  }

  @NotEmpty
  @JsonProperty
  private String targetCur2;

  public String getTargetCur2() {
    return targetCur2;
  }


  @NotEmpty
  @JsonProperty
  private String targetIssuer2;

  public String getTargetIssuer2() {
    return targetIssuer2;
  }

  @NotEmpty
  @JsonProperty
  private String rate2;

  public String getRate2() {
    return rate2;
  }

}
