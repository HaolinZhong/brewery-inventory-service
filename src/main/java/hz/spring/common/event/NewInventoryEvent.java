package hz.spring.common.event;

import hz.spring.common.model.BeerDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {

    public NewInventoryEvent(BeerDTO beerDTO) {
        super(beerDTO);
    }
}
