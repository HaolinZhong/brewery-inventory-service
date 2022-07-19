package hz.spring.breweryinventoryservice.service.listener;

import hz.spring.breweryinventoryservice.config.JmsConfig;
import hz.spring.breweryinventoryservice.service.AllocationService;
import hz.spring.common.event.AllocateBeerOrderRequest;
import hz.spring.common.event.AllocateBeerOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocationListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateBeerOrderRequest request) {
        AllocateBeerOrderResult.AllocateBeerOrderResultBuilder builder = AllocateBeerOrderResult.builder();
        builder.beerOrderDTO(request.getBeerOrderDTO());

        try {
            Boolean allocationResult = allocationService.allocateOrder(request.getBeerOrderDTO());
            if (allocationResult) {
                builder.pendingInventory(false);
            } else {
                builder.pendingInventory(true);
            }
            builder.allocationError(false);
        } catch (Exception e) {
            log.error("Allocation failed for Order Id: " + request.getBeerOrderDTO().getId());
            builder.allocationError(true);
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, builder.build());
    }

}
