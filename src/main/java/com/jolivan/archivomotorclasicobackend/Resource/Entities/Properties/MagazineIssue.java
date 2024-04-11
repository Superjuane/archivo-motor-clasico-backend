package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MagazineIssue extends Property {
    String name;
    String title;
    Integer number;
    ZonedDateTime date;
    String country;
}
