package anaydis.practice.two;

import anaydis.sort.SorterProviderImpl;
import anaydis.sort.SorterType;
import anaydis.sort.provider.SorterProvider;
import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class FullNameSorting {
    private static class StableDemo{
        public static void main(String[] args) {
            SorterProvider provider = new SorterProviderImpl();
            List<String> firstNames = Arrays.asList("Waylon", "Justine", "Abdullah", "Marcus", "Thalia", "Mathias", "Eddie", "Angela", "Lia", "Hadassah", "Joanna", "Jonathon");
            List<String> lastNames = Arrays.asList("Quinn", "Osborne", "Chandler", "Schaefer", "Levy", "Lucero", "Lamb", "Walker", "Flores", "Chambers");
            List<FullName> nameList = new ArrayList<>(20);
            Random r = new Random();
            for (int i = 0; i < 20; i++) {
                String firstName = firstNames.get(r.nextInt(10));
                String lastName = lastNames.get(r.nextInt(10));
                nameList.add(new FullName(firstName, lastName));
            }
            nameList.forEach(System.out::println);
            System.out.println("----------------");
            provider.getSorterForType(SorterType.QUICK).sort(Comparator.comparing(FullName::getFirstName), nameList);
            nameList.forEach(System.out::println);
            System.out.println("----------------");
            provider.getSorterForType(SorterType.QUICK).sort(Comparator.comparing(FullName::getLastName), nameList);
            nameList.forEach(System.out::println);
        }
    }

    private static class StableComparison{
        public static void main(String[] args) {
            SorterProvider provider = new SorterProviderImpl();
            List<String> firstNames = Arrays.asList("Waylon", "Justine", "Abdullah", "Marcus", "Thalia", "Mathias", "Eddie", "Angela", "Lia", "Hadassah", "Joanna", "Jonathon");
            List<String> lastNames = Arrays.asList("Quinn", "Osborne", "Chandler", "Schaefer", "Levy", "Lucero", "Lamb", "Walker", "Flores", "Chambers");
            List<FullName> nameList = new ArrayList<>(20);
            Random r = new Random();
            for (int i = 0; i < 20; i++) {
                String firstName = firstNames.get(r.nextInt(10));
                String lastName = lastNames.get(r.nextInt(10));
                nameList.add(new FullName(firstName, lastName));
            }
            List<SorterType> sorterTypes = Arrays.asList(SorterType.INSERTION, SorterType.QUICK, SorterType.SHELL);
            for (SorterType sorterType :
                    sorterTypes) {
                List<FullName> copy = new ArrayList<>(nameList);
                System.out.println(sorterType);
                provider.getSorterForType(sorterType).sort(Comparator.comparing(FullName::getFirstName), copy);
                provider.getSorterForType(sorterType).sort(Comparator.comparing(FullName::getLastName), copy);
                copy.forEach(System.out::println);
                System.out.println("---------------------\n");
            }
        }
    }


    private static class FullName {
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
}