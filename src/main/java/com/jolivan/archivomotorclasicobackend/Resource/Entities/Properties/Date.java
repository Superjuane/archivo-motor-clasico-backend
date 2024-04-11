package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Date extends Property{
    ZonedDateTime date;
}
