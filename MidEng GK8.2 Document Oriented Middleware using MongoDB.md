# MidEng GK8.2 Document Oriented Middleware using MongoDB

## Fragestellungen

- Nennen Sie 4 Vorteile eines NoSQL Repository im Gegensatz zu einem relationalen DBMS
  
  Horizontale Skalierbarkeit, Schema-Flexibilität, hohe Performance bei großen Datenmengen, bessere Eignung für unstrukturierte Daten

- Nennen Sie 4 Nachteile eines NoSQL Repository im Gegensatz zu einem relationalen DBMS
  
  Keine ACID-Garantien, keine Standardabfragesprache, schwache Join-Unterstützung, geringe Datenintegrität

- Welche Schwierigkeiten ergeben sich bei der Zusammenführung der Daten?

        Unterschiedliche Datenformate/Schemata, Keine einheitliche Abfragesprache,         Konsistenzprobleme (CAP), Duplikate & fehlende Fremdschlüssel

- Welche Arten von NoSQL Datenbanken gibt es + Vertreter?
  
  Key-Value Stores: Redis, Amazon DynamoDB
  
  Dokumentenorientierte Datenbanken: MongoDB, CouchDB
  
  Spaltenorientierte (Wide-Column) Datenbanken: Apache Cassandra, HBase
  
  Graphdatenbanken: Neo4j, ArangoDB
  
  Multi-Model-Datenbanken: ArangoDB, OrientDB

- Beschreiben Sie die Abkürzungen CA, CP und AP in Bezug auf das CAP Theorem

        **CA (Consistency + Availability):** Konsistente und verfügbare Daten, aber         keine Partitionstoleranz

        **CP (Consistency + Partition Tolerance):** Konsistenz + Ausfallsicherheit, aber evtl.         nicht immer verfügbar

        **AP (Availability + Partition Tolerance):** Immer verfügbar, aber evtl. inkonsistente         Daten

- Mit welchem Befehl koennen Sie den Lagerstand eines Produktes aller Lagerstandorte anzeigen.
  
  `db.products.find({ productCategory: "Lebensmittel"})`
  
  `db.products.find()`

- Mit welchem Befehl koennen Sie den Lagerstand eines Produktes eines bestimmten Lagerstandortes anzeigen.
  
  `db.products.find({ warehouseID: "4", productID: "P0019" })`

## Implementation

### GK

#### CRUD Operationen



**Post Warehouse**

```java
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
```

**Get Warehouse**

```java
@GetMapping(path = "/warehouse")
    public List<WarehouseData> getWarehouses() {
        return warehouseDataRepository.findAll();
    }
```

**Get Product**

```java
@GetMapping(path = "/product")
    public List<ProductData> getProducts() {
        return productRepository.findAll();
    }
```
