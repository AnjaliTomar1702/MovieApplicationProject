package com.niit.userauthentication.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private DatabaseUser databaseUser;

    public Image(byte[] image){
        this.image = image;
    }

    @Override
    public String toString(){
        return this.image.toString();
    }
}
