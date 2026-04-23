package ro.tacklestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.tacklestore.dto.OrderDto;
import ro.tacklestore.model.Order;
import ro.tacklestore.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(order.getCustomer().getFirstName() + \" \" + order.getCustomer().getLastName())")
    OrderDto toDto(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "subtotal", expression = "java(item.getSubtotal())")
    OrderDto.OrderItemDto toOrderItemDto(OrderItem item);
}

