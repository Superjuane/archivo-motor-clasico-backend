package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person extends Property{
    String name;
    String alias;
}
