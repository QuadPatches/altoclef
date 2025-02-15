package adris.altoclef.util;

import net.minecraft.item.Item;

import java.util.Objects;

public class SmeltTarget {

    private ItemTarget _material;
    private ItemTarget _item;

    public SmeltTarget(ItemTarget item, ItemTarget material) {
        _item = item;
        _material = material;
        _material.targetCount = _item.targetCount;
    }

    public ItemTarget getItem() {
        return _item;
    }
    public ItemTarget getMaterial() { return _material; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmeltTarget that = (SmeltTarget) o;
        return Objects.equals(_material, that._material) && Objects.equals(_item, that._item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_material, _item);
    }
}
