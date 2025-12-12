//this interface is  for objects that can be searched
public interface Searchable {
    boolean matchesId(String id);          // searching by ID
    boolean matchesName(String name);       // searching by name (partial)
    boolean matchesType(String type);       // searching by type
}
