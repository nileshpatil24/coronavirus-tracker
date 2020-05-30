package com.nilesh.coronavirustracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nilesh.coronavirustracker.model.LocationStats;

@Service
public class CoronaVirusDataService {

	private String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<LocationStats> allStats = new ArrayList<>();
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}

	
	/*
	 * byte data[] = null; File file = new File("data.csv");
	 */

	@PostConstruct
	@Scheduled(cron= "* * 1 * * *")
	public void fetchVirusData() throws IOException {
		
		 List<LocationStats> newStats = new ArrayList<>();
		 
		/*
		 * URL url = new URL(VIRUS_DATA_URL); HttpURLConnection con =
		 * (HttpURLConnection) url.openConnection(); con.setRequestMethod("GET");
		 * 
		 * int status = con.getResponseCode();
		 * 
		 * System.out.println(status);
		 * 
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(con.getInputStream())); String inputLine; StringBuffer
		 * content = new StringBuffer(); while ((inputLine = in.readLine()) != null) {
		 * content.append(inputLine); }
		 * 
		 * System.out.println(content.toString()); in.close();
		 */

		/*
		 * data = content.toString().getBytes();
		 * 
		 * 
		 * try (FileOutputStream fop = new FileOutputStream(file)) {
		 * 
		 * if (!file.exists()) { file.createNewFile(); }
		 * 
		 * System.out.println("Initializing write.....");
		 * 
		 * fop.write(data); fop.flush(); fop.close();
		 * 
		 * System.out.println("Finished writing CSV");
		 * 
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 */

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet request = new HttpGet(VIRUS_DATA_URL);

		CloseableHttpResponse response = httpClient.execute(request);

		// Get HttpResponse Status
		System.out.println(response.getStatusLine().toString());

		HttpEntity entity = response.getEntity();
		Header headers = entity.getContentType();
		
		String result = null;

		if (entity != null) {
			// return it as a String
			result = EntityUtils.toString(entity);
		}
		
		StringReader reader = new StringReader(result);

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
		for (CSVRecord record : records) {
			LocationStats locationStat = new LocationStats();
			locationStat.setState(record.get("Province/State")); 
			locationStat.setCountry(record.get("Country/Region"));
			
			int latestCases = Integer.parseInt(record.get(record.size() - 1));
			int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
			locationStat.setLatestTotalCases(latestCases);
			locationStat.setDiffPrevDay(latestCases - prevDayCases);
			newStats.add(locationStat);
		}
		
		this.allStats = newStats;
		reader.close();

	}
}
