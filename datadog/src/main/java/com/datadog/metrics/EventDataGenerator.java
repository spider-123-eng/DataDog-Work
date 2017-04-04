package com.datadog.metrics;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventDataGenerator {

	final static Logger logger = Logger.getLogger(EventDataGenerator.class);
	public static Long SLEEP = 300000l; // sleep for 5 minute

	public static void constructEvent() throws Exception {
		Event event = new Event();
		List<String> tags = new ArrayList<String>();
		Long time = System.currentTimeMillis() / 1000;

		tags.add("type" + ":" + "demoEvent");
		tags.add("job" + ":" + "EventDataGenerator");
		tags.add("status" + ":" + "success");
		event.setTags(tags);
		event.setPriority("normal"); // 'normal' or 'low'
		event.setAlert_type("info"); // "error", "warning", "info" or "success".
		event.setHost(InetAddress.getLocalHost().getHostAddress());
		event.setDate_happened(time);
		event.setTitle("Did you hear the news today ?");
		event.setText("This is a demo application !");
		pushEventsToDataDog(event);
	}

	public static void main(String[] args) throws InterruptedException {

		while (true) {
			try {

				constructEvent();

				Thread.sleep(SLEEP);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to push data to the DataDog API.
	 */
	public static void pushEventsToDataDog(Event event) throws Exception {
		try {
			if (event != null) {

				// Convert object to JSON string
				ObjectMapper jmapper = new ObjectMapper();
				String jsonOutString = jmapper.writeValueAsString(event);
				System.out.println("Event Final JsonOutput : " + jsonOutString + "\n");

				String apikey = "947d12f46dead405bf019033434f0cba";

				DataDogHttpTransport http = new DataDogHttpTransport(apikey, 5000, 5000, null, false);
				http.prepare(jsonOutString);

			}
		} catch (Exception e) {
			System.out.println("Error in EventDataGenerator.pushEventsToDataDog : " + e.getMessage());
		}
	}
}
