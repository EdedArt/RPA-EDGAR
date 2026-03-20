public class PowerDecorator extends CharacterDecorator {
    private String powerName;
    private int extraDmg;
    private int extraHp;

    public PowerDecorator(HeroCharacter character, String powerName, int extraDmg, int extraHp) {
        super(character);
        this.powerName = powerName;
        this.extraDmg = extraDmg;
        this.extraHp = extraHp;
    }

    @Override public String getDescription() { 
        return character.getDescription() + " | Poder: " + powerName; 
    }

    @Override public String getAvatar() {
        return character.getAvatar() + " ⚡";
    }
    
    @Override public int getDamage() { return character.getDamage() + extraDmg; }
    @Override public int getHp() { return character.getHp() + extraHp; }
}
