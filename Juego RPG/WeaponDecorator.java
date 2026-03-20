public class WeaponDecorator extends CharacterDecorator {
    private String weaponName;
    private int extraDmg;
    private int penaltyHp;

    public WeaponDecorator(HeroCharacter character, String weaponName, int extraDmg, int penaltyHp) {
        super(character);
        this.weaponName = weaponName;
        this.extraDmg = extraDmg;
        this.penaltyHp = penaltyHp;
    }

    @Override public String getDescription() { 
        return character.getDescription() + " | Arma: " + weaponName; 
    }

    @Override public String getAvatar() {
        return character.getAvatar() + " 🗡️";
    }
    
    @Override public int getDamage() { return character.getDamage() + extraDmg; }
    @Override public int getHp() { return character.getHp() + penaltyHp; }
}
