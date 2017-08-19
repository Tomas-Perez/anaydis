package anaydis.sort.nameSorting;

import anaydis.sort.SorterProviderImpl;
import anaydis.sort.SorterType;
import anaydis.sort.provider.SorterProvider;

import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class fullNameSorting {
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
        provider.getSorterForType(SorterType.INSERTION).sort(Comparator.comparing(FullName::getFirstName), nameList);
        nameList.forEach(System.out::println);
        System.out.println("----------------");
        provider.getSorterForType(SorterType.INSERTION).sort(Comparator.comparing(FullName::getLastName), nameList);
        nameList.forEach(System.out::println);
    }
}