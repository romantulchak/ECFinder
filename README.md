# ECFinder
 A library that allows you to easily select a collection of elements that is not a collection of entities, but is a collection of simple types marked ```@ElementCollection``` as a simple Entity.
 
# How to use
1. Download last version of Jar from Release page

2. Add Jar file to your project

3. Autowire ECFinderInvoker into your service
```java
@Autowired
private final ECFinderInvoker<ElementCollectionClass> ecFinderInvoker;
```
4. Mark your ElementCollectionClass by ```@ECF``` and ```@Embeddable```
```java  
@ECF(tableName "TABLE PREFIX") // @ECF(tableName "stops")  
@Embeddable
public class CityStop{
  //fields
}
```
This annotation is required to prevent using this library for types other than @ElementCollection and also allows you to set the prefix of the table that is ```@ElementCollection```

5. Entity
```java
@ECFEntity(tablePrefix = "trip")
@Entity
public class YourEntity {
```
When user will try to use this ```@Entity``` and ```@Embeddable``` class at the end library will try to query to table with name: "trip_stops"

6. Method in your service
```java  
private List<ElementCollectionClass> getCityStops(Entity entity){
   return ecFinderInvoker.invoke(entity.getId(), ElementCollectionClass.class, EntityClass.class);
}
```
7. Change ```@SpringBootApplication``` to ```@SpringBootApplication(scanBasePackages = {"com.mapperDTO"})```


# Technologies
1. #### Java 11
2. #### Spring boot
3. #### Spring JDBC

