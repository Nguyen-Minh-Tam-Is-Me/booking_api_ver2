package meu.booking_rebuild_ver2.service.abstractions.Admin;

import meu.booking_rebuild_ver2.model.Admin.DTO.PriceDTO;
import meu.booking_rebuild_ver2.model.Admin.PriceModel;
import meu.booking_rebuild_ver2.response.Admin.PriceResponse;

import java.util.List;
import java.util.UUID;

public interface IPriceService {
    PriceResponse createPrice(PriceDTO priceDTO);
    PriceResponse getAllPrice();
    PriceResponse updatePrice(PriceDTO priceDTO);
    PriceResponse findByID(UUID id);
    PriceResponse deleteById(UUID id);
    PriceResponse getPriceByStatus(UUID id);
    PriceResponse getPriceByBusType(UUID id);
    PriceResponse getPriceByRoutesTime(UUID id);
}
