public class ArmorDecorator extends CharacterDecorator {
    private String armorName;
    private int extraDef;
    private int extraHp;

    public ArmorDecorator(HeroCharacter character, String armorName, int extraDef, int extraHp) {
        super(character);
        this.armorName = armorName;
        this.extraDef = extraDef;
        this.extraHp = extraHp;
    }

    @Override public String getDescription() { 
        return character.getDescription() + " | Armadura: " + armorName; 
    }

    @Override public String getAvatar() {
        return "🛡️" + character.getAvatar(); // PREPENDS SHIELD TO AVATAR
    }
    
    @Override public int getDefense() { return character.getDefense() + extraDef; }
    @Override public int getHp() { return character.getHp() + extraHp; }
}
