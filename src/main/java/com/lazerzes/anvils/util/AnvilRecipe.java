package com.lazerzes.anvils.util;
//created by lazerzes

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

import java.util.List;

public class AnvilRecipe {

    private final ItemStack left, right, output;
    private final int cost;
    public final boolean showInBook;

    public AnvilRecipe(ItemStack left, ItemStack right, ItemStack output, int cost, boolean showInBook){

        this.left = left;
        this.right = right;
        this.output = output;
        this.cost = cost;
        this.showInBook = showInBook;

    }

    public AnvilRecipe(ItemStack left, ItemStack right, Enchantment out, int enchantLevel, int cost, boolean showInBook){
        
        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK, 1);
        Items.ENCHANTED_BOOK.addEnchantment(output, new EnchantmentData(out, enchantLevel));

        this.left = left;
        this.right = right;
        this.output = output;
        this.cost = cost;
        this.showInBook = showInBook;

    }

    public AnvilRecipe(Enchantment leftIn, int leftInLevel, ItemStack right, Enchantment out, int enchantLevel, int cost, boolean showInBook){

        ItemStack left = new ItemStack(Items.ENCHANTED_BOOK, 1);
        Items.ENCHANTED_BOOK.addEnchantment(left, new EnchantmentData(leftIn, leftInLevel));

        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK, 1);
        Items.ENCHANTED_BOOK.addEnchantment(output, new EnchantmentData(out, enchantLevel));

        this.left = left;
        this.right = right;
        this.output = output;
        this.cost = cost;
        this.showInBook = showInBook;

    }

    public ItemStack getLeft(){ return left; }
    public ItemStack getRight(){ return right; }
    public ItemStack getOutput(){ return output; }
    public int getCost(){ return cost; }

    public boolean matchesRecipe(AnvilRecipe check){

        if(stacksSame(this.left, check.left) && stacksSame(this.right, check.right) ){
            return true;
        }

        return false;

    }

    private boolean stacksSame(ItemStack a, ItemStack b) {

        if(a == null || b == null){
            return false;
        }

        if(a.getItem() == b.getItem() && a.getItem() instanceof ItemPotion){
            List<PotionEffect> aEffects = PotionUtils.getEffectsFromStack(a);
            List<PotionEffect> bEffects = PotionUtils.getEffectsFromStack(b);
            if(aEffects.containsAll(bEffects)){
                return true;
            }else{
                return false;
            }
        }

        if(a.getItem() == b.getItem() && a.getMetadata() == b.getMetadata()) {

            if(a.stackSize == b.stackSize){

                if(a.getItem() instanceof ItemEnchantedBook){
                    NBTTagList aEnchants = Items.ENCHANTED_BOOK.getEnchantments(a), bEnchants = Items.ENCHANTED_BOOK.getEnchantments(b);

                    if(aEnchants == null || bEnchants == null){
                        return false;
                    }

                    if(aEnchants.tagCount() == bEnchants.tagCount() && aEnchants.tagCount() == 1){
                        NBTTagCompound aEnchant = aEnchants.getCompoundTagAt(0), bEnchant = bEnchants.getCompoundTagAt(0);
                        short aID = aEnchant.getShort("id"), aLvl = aEnchant.getShort("lvl"), bID = bEnchant.getShort("id"), bLvl = bEnchant.getShort("lvl");

                        if(aID == bID && aLvl == bLvl){
                            return true;
                        }

                    }

                }else if(!a.isItemEnchanted() && !b.isItemEnchanted()){
                    return true;
                }

            }

        }

        return false;

    }
}
