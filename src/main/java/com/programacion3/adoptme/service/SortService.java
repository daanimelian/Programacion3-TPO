package com.programacion3.adoptme.service;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import com.programacion3.adoptme.domain.Dog;


@Service
public class SortService {

    public void sortDogs(List<Dog> dogs, String criteria) {
        Comparator<Dog> comparator;

        switch (criteria.toLowerCase()) {
            case "priority":
                comparator = Comparator.comparingInt(Dog::getPriority).reversed(); // mayor prioridad primero
                break;
            case "age":
                comparator = Comparator.comparingInt(Dog::getAge);
                break;
            case "weight":
                comparator = Comparator.comparingDouble(Dog::getWeight);
                break;
            default:
                throw new IllegalArgumentException("Criterio de orden no válido: " + criteria);
        }

        dogs.sort(comparator); // usa TimSort (híbrido MergeSort + InsertionSort)
    }
}



