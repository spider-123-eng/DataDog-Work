package com.datadog.metrics;

import static org.apache.http.client.fluent.Request.Post;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class DataDogHttpTransport implements Transport {
	final static Logger logger = Logger.getLogger(DataDogHttpTransport.class);

	private final static String BASE_URL = "https://app.datadoghq.com/api/v1";
	private final String seriesUrl;
	private final int connectTimeout; // in milliseconds
	private final int socketTimeout; // in milliseconds
	private final HttpHost proxy;
	private static Boolean isMetrics;

	public DataDogHttpTransport(String apiKey, int connectTimeout, int socketTimeout, HttpHost proxy,
			Boolean isMetrics) {

		if (isMetrics) {
			this.seriesUrl = String.format("%s/series?api_key=%s", BASE_URL, apiKey);
		} else {
			this.seriesUrl = String.format("%s/events?api_key=%s", BASE_URL, apiKey);
		}
		this.connectTimeout = connectTimeout;
		this.socketTimeout = socketTimeout;
		this.proxy = proxy;
		this.isMetrics = isMetrics;
	}

	public static class Builder {
		String apiKey;
		int connectTimeout = 5000;
		int socketTimeout = 5000;
		HttpHost proxy;

		public Builder withApiKey(String key) {
			this.apiKey = key;
			return this;
		}

		public Builder withConnectTimeout(int milliseconds) {
			this.connectTimeout = milliseconds;
			return this;
		}

		public Builder withSocketTimeout(int milliseconds) {
			this.socketTimeout = milliseconds;
			return this;
		}

		public Builder withProxy(String proxyHost, int proxyPort) {
			this.proxy = new HttpHost(proxyHost, proxyPort);
			return this;
		}

		public DataDogHttpTransport build() {
			return new DataDogHttpTransport(apiKey, connectTimeout, socketTimeout, proxy, isMetrics);
		}
	}

	public Request prepare(String jsonData) throws Exception {
		return new HttpRequest(this, jsonData);
	}

	public void close() throws IOException {
	}

	public static class HttpRequest implements Transport.Request {

		protected final DataDogHttpTransport transport;

		public HttpRequest(DataDogHttpTransport transport, String jsonData) throws Exception {
			this.transport = transport;
			send(jsonData);
		}

		public void send(String jsonData) throws Exception {

			try {
				StringBuilder sb = new StringBuilder();
				sb.append("Sending HTTP POST request to ");
				sb.append(this.transport.seriesUrl);
				sb.append(", POST body is: \n");
				sb.append(jsonData);

				long start = System.currentTimeMillis();
				org.apache.http.client.fluent.Request request = Post(this.transport.seriesUrl).useExpectContinue()
						.connectTimeout(this.transport.connectTimeout).socketTimeout(this.transport.socketTimeout)
						.bodyString(jsonData, ContentType.APPLICATION_JSON);

				if (this.transport.proxy != null) {
					logger.info("proxy  : " + this.transport.proxy);
					request.viaProxy(this.transport.proxy);
				}

				Response response = request.execute();

				long elapsed = System.currentTimeMillis() - start;

				HttpResponse httpResponse = response.returnResponse();
				StringBuilder sb1 = new StringBuilder();

				sb1.append("Sent metrics to Datadog: ");
				sb1.append("  Timing: ").append(elapsed).append(" ms\n");
				sb1.append("  Status: ").append(httpResponse.getStatusLine().getStatusCode()).append("\n");

				String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				sb1.append("  Content: ").append(content);

				// logger.info(sb.toString() + "\n");
				// logger.info(sb1.toString() + "\n");
				// logger.info(httpResponse.toString() + "\n");

			} catch (Exception e) {
				logger.error("Error in HttpRequest.send() : " + e.getMessage());
			}

		}
	}

}
