package com.example.climblabs.post.domain.content;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
public class Advantage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;

    @Builder
    public Advantage(String item){
        this.item = item;
    }

    public String getItem(){
        return item;
    }
}
