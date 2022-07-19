package hz.spring.breweryinventoryservice.bootstrap;

import hz.spring.breweryinventoryservice.domain.BeerInventory;
import hz.spring.breweryinventoryservice.repository.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final UUID BEER_1_UUID = UUID.fromString("095c83ee-c1bf-4933-8389-21bb6cedcd3f");
    public static final UUID BEER_2_UUID = UUID.fromString("259a01d9-d16e-403a-98a3-00df8983f731");
    public static final UUID BEER_3_UUID = UUID.fromString("9ad82b04-9660-4683-ab21-e134cd400565");

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadInitInventory();
    }

    private void loadInitInventory() {
        if (beerInventoryRepository.count() > 0) return;

        beerInventoryRepository.save(BeerInventory
                .builder()
                .beerId(BEER_1_UUID)
                .upc(BEER_1_UPC)
                .quantityOnHand(50)
                .build());

        beerInventoryRepository.save(BeerInventory
                .builder()
                .beerId(BEER_2_UUID)
                .upc(BEER_2_UPC)
                .quantityOnHand(50)
                .build());

        beerInventoryRepository.saveAndFlush(BeerInventory
                .builder()
                .beerId(BEER_3_UUID)
                .upc(BEER_3_UPC)
                .quantityOnHand(50)
                .build());

        log.debug("Loaded Inventory. Record count: " + beerInventoryRepository.count());
    }
}
