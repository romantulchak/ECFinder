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
@ECF
@Embeddable
public class CityStop{
  //fields
}
```
This annotation is required to prevent using this library for types other than @ElementCollection 

5. Method in your service
```java  
private List<ElementCollectionClass> getCityStops(Entity entity){
   return ecFinderInvoker.invoke(entity.getId(), "table_name", ElementCollectionClass.class);
}
```

# Technologies
1. #### Java 11
2. #### Spring boot
3. #### Spring JDBC

