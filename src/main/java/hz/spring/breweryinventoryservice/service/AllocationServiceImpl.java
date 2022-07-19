package hz.spring.breweryinventoryservice.service;

import hz.spring.breweryinventoryservice.domain.BeerInventory;
import hz.spring.breweryinventoryservice.repository.BeerInventoryRepository;
import hz.spring.common.model.BeerOrderDTO;
import hz.spring.common.model.BeerOrderLineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDTO beerOrderDTO) {
        log.debug("Allocating orderId: " + beerOrderDTO.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        beerOrderDTO.getBeerOrderLines().forEach(line -> {

            Integer orderedQty = line.getOrderQuantity() != null ? line.getOrderQuantity() : 0;
            Integer allocatedQty = line.getQuantityAllocated() != null ? line.getQuantityAllocated() : 0;

            if (orderedQty > allocatedQty) {
                allocateBeerOrderLine(line);
            }

            totalOrdered.set(totalOrdered.get() + line.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + (line.getQuantityAllocated() != null ? line.getQuantityAllocated() : 0));
        });

        log.debug("Total Ordered: " + totalOrdered.get() + ", Total Allocated: " + totalAllocated.get());

        return totalOrdered.get() == totalAllocated.get();
    }

    @Override
    public void deallocateOrder(BeerOrderDTO beerOrderDTO) {
        beerOrderDTO.getBeerOrderLines().forEach(line -> {
            BeerInventory inventory = BeerInventory.builder()
                    .beerId(line.getId())
                    .upc(line.getUpc())
                    .quantityOnHand(line.getQuantityAllocated())
                    .build();

            BeerInventory savedInventory = beerInventoryRepository.save(inventory);

            log.debug("Saved Inventory for beer upc: " + savedInventory.getUpc() + "inventory id: " + savedInventory.getId());
        });
    }

    private void allocateBeerOrderLine(BeerOrderLineDTO line) {
        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(line.getUpc());

        beerInventoryList.forEach(beerInventory -> {
            int inventory = beerInventory.getQuantityOnHand() != null ? beerInventory.getQuantityOnHand() : 0;
            int orderedQty = line.getOrderQuantity() != null ? line.getOrderQuantity() : 0;
            int allocatedQty = line.getQuantityAllocated() != null ? line.getQuantityAllocated() : 0;
            int qtyToAllocate = orderedQty - allocatedQty;

            if (inventory >= qtyToAllocate) {
                inventory -= qtyToAllocate;
                line.setQuantityAllocated(orderedQty);
                beerInventory.setQuantityOnHand(inventory);

                beerInventoryRepository.save(beerInventory);

            } else if (inventory > 0) {
                line.setQuantityAllocated(allocatedQty + inventory);
                beerInventory.setQuantityOnHand(0);

                beerInventoryRepository.delete(beerInventory);
            }

        });

    }
}
