package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.Data;

@Data
public class Competition extends Property {
    String name;
    String year;
    String country;
    String startDate;
    String finishDate;
}
