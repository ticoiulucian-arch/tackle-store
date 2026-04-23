package ro.tacklestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.tacklestore.dto.CreateCustomerRequest;
import ro.tacklestore.dto.CustomerDto;
import ro.tacklestore.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CreateCustomerRequest request);
}

