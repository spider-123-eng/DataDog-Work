package com.datadog.metrics;

import java.io.Closeable;

/**
 * The transport layer for pushing metrics to datadog
 */
public interface Transport extends Closeable {

	/**
	 * Build a request context.
	 */
	public Request prepare(String jsonData) throws Exception;

	/**
	 * A request for batching of metrics to be pushed to datadog. The call order
	 * is expected to be: one or more of addGauge, addCounter -> send()
	 */
	public interface Request {

		/**
		 * Send the request to datadog
		 */
		void send(String jsonData) throws Exception;
	}
}
