package com.crypto.notification.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String symbol;

    @Column(name = "`signal`")   // escape reserved keyword
    private String signal;

    private Double price;

    @Enumerated(EnumType.STRING)
    private ChannelType channel;

    private String status; // SENT / FAILED

    @Column(name = "`timestamp`") // escape reserved keyword
    private Long timestamp;
}
