package pl.koziolekweb.vendormachine.shop;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {

    @Mapping(target = "coins", source = "deposit")
    @Mapping(target = "totalCost", source = "total")
    BuyProductResponse fromLedger(Ledger ledger);
}
