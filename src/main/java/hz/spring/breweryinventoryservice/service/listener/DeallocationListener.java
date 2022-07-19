package hz.spring.breweryinventoryservice.service.listener;

import hz.spring.breweryinventoryservice.config.JmsConfig;
import hz.spring.breweryinventoryservice.service.AllocationService;
import hz.spring.common.event.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocationListener {

    private final AllocationService allocationService;

    @JmsListener(destination = JmsConfig.DEALLOCATE_ORDER_QUEUE)
    public void listen(DeallocateOrderRequest request) {
        allocationService.deallocateOrder(request.getBeerOrderDTO());
    }
}
