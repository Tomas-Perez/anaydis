package anaydis.sort.practice.two;

/**
 * @author Tomas Perez Molina
 */
public class FullName {
    private String firstName;
    private String lastName;

    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }
}
