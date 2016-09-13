package com.boluozhai.snowflake.httpclient.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Permission;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.httpclient.HttpConnection;

public class HttpConnectionWrapper extends HttpConnection {

	private final HttpURLConnection inner;

	public HttpConnectionWrapper(HttpURLConnection in) {
		super(null);
		this.inner = in;
	}

	public int hashCode() {
		return inner.hashCode();
	}

	public String getHeaderFieldKey(int n) {
		return inner.getHeaderFieldKey(n);
	}

	public void setFixedLengthStreamingMode(int contentLength) {
		inner.setFixedLengthStreamingMode(contentLength);
	}

	public boolean equals(Object obj) {
		return inner.equals(obj);
	}

	public void setFixedLengthStreamingMode(long contentLength) {
		inner.setFixedLengthStreamingMode(contentLength);
	}

	public void setChunkedStreamingMode(int chunklen) {
		inner.setChunkedStreamingMode(chunklen);
	}

	public String getHeaderField(int n) {
		return inner.getHeaderField(n);
	}

	public void setInstanceFollowRedirects(boolean followRedirects) {
		inner.setInstanceFollowRedirects(followRedirects);
	}

	public void connect() throws IOException {
		inner.connect();
	}

	public boolean getInstanceFollowRedirects() {
		return inner.getInstanceFollowRedirects();
	}

	public void setRequestMethod(String method) throws ProtocolException {
		inner.setRequestMethod(method);
	}

	public void setConnectTimeout(int timeout) {
		inner.setConnectTimeout(timeout);
	}

	public int getConnectTimeout() {
		return inner.getConnectTimeout();
	}

	public String getRequestMethod() {
		return inner.getRequestMethod();
	}

	public int getResponseCode() throws IOException {
		return inner.getResponseCode();
	}

	public void setReadTimeout(int timeout) {
		inner.setReadTimeout(timeout);
	}

	public int getReadTimeout() {
		return inner.getReadTimeout();
	}

	public URL getURL() {
		return inner.getURL();
	}

	public int getContentLength() {
		return inner.getContentLength();
	}

	public String getResponseMessage() throws IOException {
		return inner.getResponseMessage();
	}

	public long getContentLengthLong() {
		return inner.getContentLengthLong();
	}

	public String getContentType() {
		return inner.getContentType();
	}

	public long getHeaderFieldDate(String name, long Default) {
		return inner.getHeaderFieldDate(name, Default);
	}

	public String getContentEncoding() {
		return inner.getContentEncoding();
	}

	public void disconnect() {
		inner.disconnect();
	}

	public boolean usingProxy() {
		return inner.usingProxy();
	}

	public long getExpiration() {
		return inner.getExpiration();
	}

	public Permission getPermission() throws IOException {
		return inner.getPermission();
	}

	public long getDate() {
		return inner.getDate();
	}

	public InputStream getErrorStream() {
		return inner.getErrorStream();
	}

	public long getLastModified() {
		return inner.getLastModified();
	}

	public String getHeaderField(String name) {
		return inner.getHeaderField(name);
	}

	public Map<String, List<String>> getHeaderFields() {
		return inner.getHeaderFields();
	}

	public int getHeaderFieldInt(String name, int Default) {
		return inner.getHeaderFieldInt(name, Default);
	}

	public long getHeaderFieldLong(String name, long Default) {
		return inner.getHeaderFieldLong(name, Default);
	}

	public Object getContent() throws IOException {
		return inner.getContent();
	}

	@SuppressWarnings("rawtypes")
	public Object getContent(Class[] classes) throws IOException {
		return inner.getContent(classes);
	}

	public InputStream getInputStream() throws IOException {
		return inner.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return inner.getOutputStream();
	}

	public String toString() {
		return inner.toString();
	}

	public void setDoInput(boolean doinput) {
		inner.setDoInput(doinput);
	}

	public boolean getDoInput() {
		return inner.getDoInput();
	}

	public void setDoOutput(boolean dooutput) {
		inner.setDoOutput(dooutput);
	}

	public boolean getDoOutput() {
		return inner.getDoOutput();
	}

	public void setAllowUserInteraction(boolean allowuserinteraction) {
		inner.setAllowUserInteraction(allowuserinteraction);
	}

	public boolean getAllowUserInteraction() {
		return inner.getAllowUserInteraction();
	}

	public void setUseCaches(boolean usecaches) {
		inner.setUseCaches(usecaches);
	}

	public boolean getUseCaches() {
		return inner.getUseCaches();
	}

	public void setIfModifiedSince(long ifmodifiedsince) {
		inner.setIfModifiedSince(ifmodifiedsince);
	}

	public long getIfModifiedSince() {
		return inner.getIfModifiedSince();
	}

	public boolean getDefaultUseCaches() {
		return inner.getDefaultUseCaches();
	}

	public void setDefaultUseCaches(boolean defaultusecaches) {
		inner.setDefaultUseCaches(defaultusecaches);
	}

	public void setRequestProperty(String key, String value) {
		inner.setRequestProperty(key, value);
	}

	public void addRequestProperty(String key, String value) {
		inner.addRequestProperty(key, value);
	}

	public String getRequestProperty(String key) {
		return inner.getRequestProperty(key);
	}

	public Map<String, List<String>> getRequestProperties() {
		return inner.getRequestProperties();
	}

	@Override
	public void close() throws IOException {
		inner.disconnect();
	}

}
