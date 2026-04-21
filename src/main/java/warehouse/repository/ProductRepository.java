package warehouse.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import warehouse.model.ProductData;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductData, String> {
    List<ProductData> findByWarehouseID(String warehouseID);
    List<ProductData> findByProductID(String productID);
    void deleteByWarehouseID(String warehouseID);
    void deleteByWarehouseIDAndProductID(String warehouseID, String productID);
}

