package storage;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.gwtTests.History.History;
import org.gwtTests.hub.SettingsStorage;

public class StorageTests {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	private ScrapedValues createAdWrapper(String url) {
		ScrapedValues result = new ScrapedValues();
		result.add(ScrapedValue.create(ValueKind.title, "title"));
		result.add(ScrapedValue.create(ValueKind.prize, "0"));
		result.add(ScrapedValue.create(ValueKind.timestamp, "ts"));
		result.add(ScrapedValue.create(ValueKind.location, "lo"));
		result.add(ScrapedValue.create(ValueKind.url, url));
		result.add(ScrapedValue.create(ValueKind.description, "de"));
		result.add(ScrapedValue.create(ValueKind.phone, "0"));
		result.add(ScrapedValue.create(ValueKind.size, "0"));
		result.add(ScrapedValue.create(ValueKind.rooms, "0"));
		return result;
	}

	@Test
	public void testHistoryBulkStorage() {
		History h = History.instance();
		ArrayList<ScrapedValues> adsUrl1 = new ArrayList<ScrapedValues>();
		adsUrl1.add(createAdWrapper("a"));
		adsUrl1.add(createAdWrapper("b"));

		ArrayList<ScrapedValues> adsUrl2 = new ArrayList<ScrapedValues>();
		adsUrl2.add(createAdWrapper("c"));

		String urlId1 = "url1";
		String urlId2 = "url2";
		
		h.add(urlId1, adsUrl1);
		h.flush(urlId1);

		h.add(urlId2, adsUrl2);
		
		h.flush(urlId2);
		h.reset();
		
		assertTrue(h.size(urlId1) == 2);
		assertTrue(h.find(urlId1, "a"));
		assertTrue(h.find(urlId1, "b"));
		assertFalse(h.find(urlId1, "c"));

		h.clip(urlId1, 1);
		h.flush(urlId1);
		h.reset();
		
		assertFalse(h.find(urlId1, "b"));
		assertTrue(h.find(urlId1, "a"));

		assertTrue(h.size(urlId2) == 1);
		assertTrue(h.find(urlId2, "c"));

	}

	@Test
	public void testSettings() {
		SettingsStorage s = SettingsStorage.instance();

		s.set("k1", "t1", "a");
		s.set("k2", "t1", "ooh...");
		s.set("k2", "t1", "b");

		try {
			assertTrue(s.get("k1").equals("a"));
			@SuppressWarnings("unused")
			String v = s.get("k2");
			assertTrue(s.get("k2").equals("b"));
		} catch (Exception e) {
			fail(e.getMessage());
		}

		try {
			assertTrue(s.get("ooou") == "a");
			fail("exception expected");
		} catch (Exception e) {
		}

		s.del("k1");
		try {
			assertTrue(s.get("k1") == "a");
			fail("exception expected");
		} catch (Exception e) {
		}

		String url = "http://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote?&PRICE_FROM=100&areaId=900&PROPERTY_TYPE=3%3B16&PRICE_TO=400&periode=2&view=list";
		s.set("url_searchB", "url", url);
		try {
			String retrieved = s.get("url_searchB");
			assertTrue(retrieved.equals(url));
		} catch (Exception e) {
		}
	}

	private boolean findSetting(Collection<SimpleImmutableEntry<String, String>> settings, String settingWanted){
		for (SimpleImmutableEntry<String, String> setting : settings) 
			if (settingWanted.equals(setting.getKey()))
				return true;
				
		return false;
	}
	
	@Test
	public void testSettingsByType() {
		SettingsStorage s = SettingsStorage.instance();

		s.set("ll", "someType", "a");
		s.set("l1", "someType", "a");

		s.set("url0", "url", "a");
		s.set("url1", "url", "a");
		s.set("url2", "url", "a");
		s.set("url12a", "url", "a");

		assertTrue(s.getSettingsByType("lala").size() == 0);
		assertTrue(s.getSettingsByType("someType").size() == 2);

		List<SimpleImmutableEntry<String, String>> urlkeys = s.getSettingsByType("url");
		assertTrue(urlkeys.size() == 4);
		assertTrue(findSetting(urlkeys, "url2"));
		assertFalse(findSetting(urlkeys, "ll"));

	}
}
