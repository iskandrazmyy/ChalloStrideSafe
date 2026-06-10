public class Athlete {
    private final String id;
    private final String name;
    private final int age;
    private final String status;

    public Athlete(String id, String name, int age, String status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getStatus() { return status; }
}