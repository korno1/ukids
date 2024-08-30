package com.modernfamily.ukids.domain.tree.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TreeType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treeTypeId;

    @Column(length = 45, nullable = false)
    private String name;
}
