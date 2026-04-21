package warehouse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;
import warehouse.model.ProductData;
import warehouse.model.WarehouseData;

import java.util.Random;

@SpringBootApplication
public class Application {

	@Value("${server.port:8080}")
	private int serverPort;

	private final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void seed() {
		String baseUrl = "http://localhost:" + serverPort;

		WarehouseData[] existing = restTemplate.getForObject(baseUrl + "/warehouse", WarehouseData[].class);
		if (existing != null) {
			for (WarehouseData w : existing) {
				if (w != null && w.getWarehouseID() != null) {
					restTemplate.delete(baseUrl + "/warehouse/" + w.getWarehouseID());
				}
			}
		}

		String[] warehouseIDs = {"1", "2", "3", "4", "5"};
		String[] names = {"Linz Bahnhof", "Salzburg Hbf", "Graz Hbf", "Innsbruck West", "Wien Mitte"};
		int[] postalCodes = {4010, 5020, 8010, 6020, 1030};
		String[] cities = {"Linz", "Salzburg", "Graz", "Innsbruck", "Wien"};

		for (int i = 0; i < warehouseIDs.length; i++) {
			WarehouseData w = new WarehouseData();
			w.setId(warehouseIDs[i]);
			w.setWarehouseID(warehouseIDs[i]);
			w.setWarehouseName(names[i]);
			w.setTimestamp("2022-01-02 01:00:00");
			w.setWarehousePostalCode(postalCodes[i]);
			w.setWarehouseCity(cities[i]);
			w.setWarehouseCountrz("Austria");
			restTemplate.postForEntity(baseUrl + "/warehouse", w, WarehouseData.class);
		}

		String[] categories = {"Getraenk", "Waschmittel", "Tierfutter", "Reinigung", "Lebensmittel", "Elektronik"};
		int productCount = 300;
		Random rand = new Random(42);

		for (int i = 1; i <= productCount; i++) {
			String warehouseID = warehouseIDs[(i - 1) % warehouseIDs.length];
			String category = categories[(i - 1) % categories.length];

			ProductData p = new ProductData();
			p.setWarehouseID(warehouseID);
			p.setProductID(String.format("P%04d", i));
			p.setProductName(category + " Produkt " + i);
			p.setProductCategory(category);
			p.setProductQuantity(10 + rand.nextInt(10000));

			restTemplate.postForEntity(baseUrl + "/product", p, ProductData.class);
		}
	}

}
