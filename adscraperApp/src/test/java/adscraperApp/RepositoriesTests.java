package adscraperApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import app.storage.Repos;
import app.storage.RepositoryItem;
import junit.framework.Assert;

import java.util.List;

import org.cg.adscraper.factory.*;
import org.cg.util.debug.DebugUtilities;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config-test.xml")
public class RepositoriesTests {

	@Autowired
	IStorageFactory factory;
	@Autowired
	Repos repos;

	@Before
	public void setUp() {
		StorageFactory.setUp(factory);
		resetRepos(factory, repos);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testRepos() {
		List<RepositoryItem> items = repos.getItems();
		Assert.assertEquals(4, items.size());
		for (RepositoryItem r : repos.getItems())
			System.out.println(String.format("repo %s item type %s size %d", r.repo.getClass().getName(), r._class.getName(), r.size));
	}
	
	private void resetRepos(IStorageFactory factory, Repos r){
		r.getItems().stream().forEach(x -> x.repo.deleteAll());
		factory.getHistoricalDetailStorage().store(DebugUtilities.getTestAd());
		factory.getHistoryStorage().store("ui", DebugUtilities.getTestAd());
		factory.getSettingsStorage().set("k", "t", "v");
		factory.createKeyTypeValueStorage().of("t1", "v2").save("v1");
	}
}
