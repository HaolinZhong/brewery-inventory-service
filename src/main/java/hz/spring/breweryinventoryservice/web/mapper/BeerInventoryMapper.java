package hz.spring.breweryinventoryservice.web.mapper;

import hz.spring.breweryinventoryservice.domain.BeerInventory;
import hz.spring.breweryinventoryservice.web.model.BeerInventoryDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerInventoryMapper {
    BeerInventory DTOToBeerInventory(BeerInventoryDTO beerInventoryDTO);

    BeerInventoryDTO BeerInventoryToDTO(BeerInventory beerInventory);
}
