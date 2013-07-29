package com.bharath;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * User: bharadwaj
 * Date: 29/07/13
 * Time: 12:09 PM
 */
public class Request implements Serializable {

    private String httpUri;
    private String httpMethod;
    private String httpPath;
    private String httpQuery;
    private int responseCode;
    private String httpCharacterEncoding;
    private String httpContentType;
    private String httpContentEncoding;
    private String httpProtocolVersion;

    public String toString() {
        JSONObject jsonObj = new JSONObject(this);
        return jsonObj.toString();
    }

    public String getHttpUri() {
        return httpUri;
    }

    public void setHttpUri(String httpUri) {
        this.httpUri = httpUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public String getHttpQuery() {
        return httpQuery;
    }

    public void setHttpQuery(String httpQuery) {
        this.httpQuery = httpQuery;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getHttpCharacterEncoding() {
        return httpCharacterEncoding;
    }

    public void setHttpCharacterEncoding(String httpCharacterEncoding) {
        this.httpCharacterEncoding = httpCharacterEncoding;
    }

    public String getHttpContentType() {
        return httpContentType;
    }

    public void setHttpContentType(String httpContentType) {
        this.httpContentType = httpContentType;
    }

    public String getHttpContentEncoding() {
        return httpContentEncoding;
    }

    public void setHttpContentEncoding(String httpContentEncoding) {
        this.httpContentEncoding = httpContentEncoding;
    }

    public String getHttpProtocolVersion() {
        return httpProtocolVersion;
    }

    public void setHttpProtocolVersion(String httpProtocolVersion) {
        this.httpProtocolVersion = httpProtocolVersion;
    }
}
