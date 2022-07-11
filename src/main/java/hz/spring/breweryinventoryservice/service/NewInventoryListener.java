package hz.spring.breweryinventoryservice.service;

import hz.spring.breweryinventoryservice.config.JmsConfig;
import hz.spring.breweryinventoryservice.domain.BeerInventory;
import hz.spring.breweryinventoryservice.repository.BeerInventoryRepository;
import hz.spring.common.model.BeerDTO;
import hz.spring.common.event.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewInventoryListener {

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent event) {

        log.debug("Got Inventory: " + event.toString());

        BeerDTO beerDTO = event.getBeerDTO();

        beerInventoryRepository.save(
          BeerInventory.builder()
                  .beerId(beerDTO.getId())
                  .upc(beerDTO.getUpc())
                  .quantityOnHand(beerDTO.getQuantityOnHand())
                  .build()
        );
    }

}
