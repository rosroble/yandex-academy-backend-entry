package ru.rosroble.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rosroble.dto.ShopUnitDTO;
import ru.rosroble.dto.ShopUnitStatisticUnitDTO;

import javax.persistence.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shop_units")
public class ShopUnit {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "p_id")
    private ShopUnit parent;

    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    private Long price;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<ShopUnit> children = new ArrayList<>();

    public void addChild(ShopUnit unit) {
        if (children == null) children = new ArrayList<>();
        children.add(unit);
        unit.setParent(this);
    }

    public void removeChild(ShopUnit unit) {
        unit.setParent(null);
        children.remove(unit);
    }

    public ShopUnitDTO toShopUnitDTO() {
        String parentId = parent == null ? null : parent.getId();
        List<ShopUnitDTO> shopUnitDTOList = children.stream().map(ShopUnit::toShopUnitDTO).toList();
        if (shopUnitDTOList.isEmpty()) shopUnitDTOList = null;
        String dateToStr = date.toInstant().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        return new ShopUnitDTO(id, name, dateToStr, parentId, type, price, shopUnitDTOList);
    }

    public ShopUnitStatisticUnitDTO toShopUnitStatisticUnitDTO() {
        return new ShopUnitStatisticUnitDTO(id, name, parent == null ? null : parent.getId(), type, price, date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopUnit shopUnit = (ShopUnit) o;
        return id.equals(shopUnit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
