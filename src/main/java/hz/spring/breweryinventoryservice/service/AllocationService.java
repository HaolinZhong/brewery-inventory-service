package hz.spring.breweryinventoryservice.service;

import hz.spring.common.model.BeerOrderDTO;

public interface AllocationService {

        Boolean allocateOrder(BeerOrderDTO beerOrderDTO);

}
