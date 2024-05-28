package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persons extends Property{
    List<Person> persons;
}
