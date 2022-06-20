package ru.rosroble.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.rosroble.model.ShopUnitType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "shop_units_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShopUnitStatisticUnitDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private int index;
    private UUID id;
    private String name;
    @Column(name = "parent_id")
    private UUID parentId;
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;
    private Long price;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date date;
}
