package warehouse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import warehouse.model.ProductData;
import warehouse.model.WarehouseData;
import warehouse.repository.ProductRepository;
import warehouse.repository.WarehouseDataRepository;

import java.util.List;

@RestController
public class WarehouseController {
    private final WarehouseDataRepository warehouseDataRepository;
    private final ProductRepository productRepository;

    public WarehouseController(WarehouseDataRepository warehouseDataRepository, ProductRepository productRepository) {
        this.warehouseDataRepository = warehouseDataRepository;
        this.productRepository = productRepository;
    }

    @PostMapping(path = "/warehouse")
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseData addWarehouse(@RequestBody WarehouseData warehouseData) {
        if (warehouseData.getWarehouseID() == null || warehouseData.getWarehouseID().isBlank()) {
            throw new IllegalArgumentException("warehouseID missing");
        }
        if (warehouseData.getId() == null || warehouseData.getId().isBlank()) {
            warehouseData.setId(warehouseData.getWarehouseID());
        }
        return warehouseDataRepository.save(warehouseData);
    }

    @GetMapping(path = "/warehouse")
    public List<WarehouseData> getWarehouses() {
        return warehouseDataRepository.findAll();
    }

    @GetMapping(path = "/warehouse/{id}")
    public WarehouseData getWarehouse(@PathVariable String id) {
        return warehouseDataRepository.findByWarehouseID(id)
                .orElseThrow(() -> new IllegalStateException("Warehouse not found for id=" + id));
    }

    @DeleteMapping(path = "/warehouse/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWarehouse(@PathVariable String id) {
        warehouseDataRepository.deleteByWarehouseID(id);
        productRepository.deleteByWarehouseID(id);
    }

    @PostMapping(path = "/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductData addOrUpdateProduct(@RequestBody ProductData productData) {
        if (productData.getWarehouseID() == null || productData.getWarehouseID().isBlank()) {
            throw new IllegalArgumentException("warehouseID missing");
        }
        if (productData.getProductID() == null || productData.getProductID().isBlank()) {
            throw new IllegalArgumentException("productID missing");
        }

        warehouseDataRepository.findByWarehouseID(productData.getWarehouseID())
                .orElseThrow(() -> new IllegalStateException("Warehouse not found for warehouseID=" + productData.getWarehouseID()));

        return productRepository.save(productData);
    }

    @GetMapping(path = "/product")
    public List<ProductData> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping(path = "/product/{id}")
    public List<ProductData> getProduct(@PathVariable String id) {
        return productRepository.findByProductID(id);
    }

    @DeleteMapping(path = "/product/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id, @RequestParam(required = false) String warehouseID) {
        if (warehouseID == null || warehouseID.isBlank()) {
            for (ProductData p : productRepository.findByProductID(id)) {
                productRepository.delete(p);
            }
        } else {
            productRepository.deleteByWarehouseIDAndProductID(warehouseID, id);
        }
    }
}

