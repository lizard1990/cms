/*
 *   Copyright 2014 Webpagebytes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.webpagebytes.cms;

import java.util.Map;

class URLMatcherResult {
private String urlRequest;

private String urlPattern;
private Map<String, String> patternParams;
public String getUrlPattern() {
	return urlPattern;
}
public void setUrlPattern(String urlPattern) {
	this.urlPattern = urlPattern;
}
public Map<String, String> getPatternParams() {
	return patternParams;
}
public void setPatternParams(Map<String, String> patternParams) {
	this.patternParams = patternParams;
}
public String getUrlRequest() {
	return urlRequest;
}
public void setUrlRequest(String urlRequest) {
	this.urlRequest = urlRequest;
}


}
