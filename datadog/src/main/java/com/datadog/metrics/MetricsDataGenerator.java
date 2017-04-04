package com.datadog.metrics;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetricsDataGenerator {
	final static Logger logger = Logger.getLogger(MetricsDataGenerator.class);

	static List<Series> seriesList = new ArrayList<Series>();
	static List<String> tags = new ArrayList<String>();
	static Series series = new Series();
	static // get Runtime instance
	Runtime instance = Runtime.getRuntime();
	static int mb = 1024 * 1024;
	public static Long SLEEP = 300000l; // sleep for 5 minute

	public static void main(String[] args) throws InterruptedException {

		while (true) {
			seriesList = new ArrayList<Series>();
			series = new Series();
			try {
				getMaxMemory();
				getAvailableMemory();
				getUsedMemory();
				getFreeMemory();
				getTotalMemory();

				Thread.sleep(SLEEP);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to constructing the series object.
	 */
	public static void constructSerieObject(List<String> tags, Long metricValue, String metricName) throws Exception {

		List<Long> points = new ArrayList<Long>();
		List<List<Long>> finalpoints = new ArrayList<>();

		// constructing the series object

		series.setType("gauge");
		series.setHost(InetAddress.getLocalHost().getHostAddress());
		series.setMetric(metricName);
		tags.add("environment" + ":" + "dev");
		series.setTags(tags);
		Long time = System.currentTimeMillis() / 1000;
		points.add(time);
		points.add(metricValue);
		finalpoints.add(points);
		series.setPoints(finalpoints);
		seriesList.add(series);

		// push the metrics to DataDog API
		pushToDataDog(seriesList);

	}

	public static void getMaxMemory() throws Exception {
		tags = new ArrayList<String>();
		long maxMem = (instance.maxMemory() / mb);
		tags.add("Heap_utilization_statistics" + ":" + "Max_Memory_in_MB");
		constructSerieObject(tags, maxMem, "heap.max.memory");
	}

	public static void getTotalMemory() throws Exception {
		tags = new ArrayList<String>();
		long totMem = (instance.totalMemory() / mb);
		tags.add("Heap_utilization_statistics" + ":" + "Total_Memory_in_MB");
		constructSerieObject(tags, totMem, "heap.total.memory");
	}

	public static void getFreeMemory() throws Exception {
		tags = new ArrayList<String>();
		long freeMem = (instance.freeMemory() / mb);
		tags.add("Heap_utilization_statistics" + ":" + "Free_Memory_in_MB");
		constructSerieObject(tags, freeMem, "heap.free.memory");
	}

	public static void getUsedMemory() throws Exception {
		tags = new ArrayList<String>();
		long usedMem = ((instance.totalMemory() - instance.freeMemory()) / mb);
		tags.add("Heap_utilization_statistics" + ":" + "Used_Memory_in_MB");
		constructSerieObject(tags, usedMem, "heap.used.memory");
	}

	public static void getAvailableMemory() throws Exception {
		tags = new ArrayList<String>();
		long availMem = instance.availableProcessors();
		tags.add("Heap_utilization_statistics" + ":" + "available_Memory_in_MB");
		constructSerieObject(tags, availMem, "heap.available.memory");
	}

	/**
	 * This method is used to push data to the DataDog API.
	 */
	public static void pushToDataDog(List<Series> seriesList) throws Exception {
		try {
			if (seriesList.size() > 0) {
				MetricsOutPutJson jsonOutPut = new MetricsOutPutJson();
				jsonOutPut.setSeries(seriesList);

				// Convert object to JSON string
				ObjectMapper jmapper = new ObjectMapper();
				String jsonOutString = jmapper.writeValueAsString(jsonOutPut);
				System.out.println("Metrics Final JsonOutput : " + jsonOutString + "\n");

				String apikey = "947d12f46dead405bf019033434f0";

				DataDogHttpTransport http = new DataDogHttpTransport(apikey, 5000, 5000, null, true);
				http.prepare(jsonOutString);

			}
		} catch (Exception e) {
			System.out.println("Error in MetricsDataGenerator.pushToDataDog : " + e.getMessage());
		}
	}

}
